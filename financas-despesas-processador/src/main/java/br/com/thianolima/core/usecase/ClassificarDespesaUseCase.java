package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarDespesaRecorrente;
import br.com.thianolima.core.provider.BuscarParcelaAnterior;
import br.com.thianolima.model.Despesa;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassificarDespesaUseCase {

    private final BuscarParcelaAnterior buscarParcelaAnterior;
    private final BuscarDespesaRecorrente buscarDespesaRecorrente;
    private final ClassificarDespesaPorRegraUseCase classificarDespesaPorRegraUseCase;

    private static final Pattern PARCELA_PATTERN = Pattern.compile("(\\d+)/(\\d+)$");

    public ClassificarDespesaUseCase(
            BuscarParcelaAnterior buscarParcelaAnterior,
            BuscarDespesaRecorrente buscarDespesaRecorrente,
            ClassificarDespesaPorRegraUseCase classificarDespesaPorRegraUseCase
    ) {
        this.buscarParcelaAnterior = buscarParcelaAnterior;
        this.buscarDespesaRecorrente = buscarDespesaRecorrente;
        this.classificarDespesaPorRegraUseCase = classificarDespesaPorRegraUseCase;
    }

    public Despesa executar(Despesa despesa){
        despesa.setParcelaAtual(
                extrairParcelaAtual(despesa.getDescricaoOriginal())
        );

        despesa.setTotalParcelas(
                extrairTotalParcelas(despesa.getDescricaoOriginal())
        );

        buscarDadosParcelaAnterior(despesa)
                .or(() -> buscarDadosDespesaRecorrente(despesa))
                .or(() -> categorizarDespesa(despesa))
                .ifPresent(depesaSalva -> {
                    despesa.setCategoriaId(depesaSalva.getCategoriaId());
                    despesa.setDescricaoProcessada(depesaSalva.getDescricaoProcessada());
                    despesa.setObservacao(depesaSalva.getObservacao());
                    despesa.setRecorrente(depesaSalva.getRecorrente());
                });

        return despesa;
    }

    private Optional<Despesa> buscarDadosParcelaAnterior(Despesa novaDespesa){
        if (novaDespesa.isParcelado() && !novaDespesa.isPrimeiraParcela()){
            return buscarParcelaAnterior.executar(novaDespesa);
        }
        return Optional.empty();
    }

    private Optional<Despesa> buscarDadosDespesaRecorrente(Despesa novaDespesa){
        return buscarDespesaRecorrente.executar(
                novaDespesa.getDescricaoOriginal(),
                novaDespesa.getValor(),
                novaDespesa.getCartaoId()
        );
    }

    private Integer extrairParcelaAtual(String descricao){
        Matcher matcher = PARCELA_PATTERN.matcher(descricao.trim());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private Integer extrairTotalParcelas(String descricao){
        Matcher matcher = PARCELA_PATTERN.matcher(descricao.trim());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(2));
        }
        return 0;
    }

    private Optional<Despesa> categorizarDespesa(Despesa despesa){
        return classificarDespesaPorRegraUseCase.executar(despesa);
    }
}
