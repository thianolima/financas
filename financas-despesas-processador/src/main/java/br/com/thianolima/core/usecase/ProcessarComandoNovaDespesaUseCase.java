package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarDespesaRecorrente;
import br.com.thianolima.core.provider.BuscarParcelaAnterior;
import br.com.thianolima.core.provider.ProduzirComandoNovaNotificacao;
import br.com.thianolima.core.provider.SalvarDespesa;
import br.com.thianolima.model.Despesa;

import java.util.Optional;

public class ProcessarComandoNovaDespesaUseCase {

    private final ClassificarDespesaPorRegraUseCase classificarDespesaPorRegraUseCase;
    private final SalvarDespesa salvarDespesa;
    private final ProduzirComandoNovaNotificacao produzirComandoNovaNotificacao;
    private final BuscarDespesaRecorrente buscarDespesaRecorrente;
    private final BuscarParcelaAnterior buscarParcelaAnterior;

    public ProcessarComandoNovaDespesaUseCase(
            ClassificarDespesaPorRegraUseCase classificarDespesaPorRegraUseCase,
            SalvarDespesa salvarDespesa,
            ProduzirComandoNovaNotificacao produzirComandoNovaNotificacao,
            BuscarDespesaRecorrente buscarDespesaRecorrente,
            BuscarParcelaAnterior buscarParcelaAnterior
    ) {
        this.classificarDespesaPorRegraUseCase = classificarDespesaPorRegraUseCase;
        this.buscarDespesaRecorrente = buscarDespesaRecorrente;
        this.buscarParcelaAnterior = buscarParcelaAnterior;
        this.salvarDespesa = salvarDespesa;
        this.produzirComandoNovaNotificacao = produzirComandoNovaNotificacao;
    }

    public void executar(
            Despesa despesa,
            int sequencialAtual,
            int sequencialFinal
    ){
        buscarDadosDespesaParcelada(despesa)
                .or(() -> buscarDadosDespesaRecorrente(despesa))
                .or(() -> classificarDespesaPorRegraUseCase.executar(despesa))
                .ifPresent(novaDespesa -> {
                    despesa.setObservacao(novaDespesa.getObservacao());
                    despesa.setRecorrente(novaDespesa.getRecorrente());
                    despesa.setCategoriaId(novaDespesa.getCategoriaId());
                    despesa.setDescricaoProcessada(novaDespesa.getDescricaoProcessada());
                });
        salvarDespesa.executar(despesa);

        if(sequencialAtual == sequencialFinal){
            produzirComandoNovaNotificacao.executar(
                    despesa.getUsuarioId(),
                    "ProcessarComandoNovaDespesa",
                    "Importação da fatura do cartão de credito feita com sucesso!"
            );
        }
    }

    private Optional<Despesa> buscarDadosDespesaRecorrente(Despesa novaDespesa){
        return buscarDespesaRecorrente.executar(
                novaDespesa.getDescricaoOriginal(),
                novaDespesa.getValor(),
                novaDespesa.getCartaoId()
        );
    }

    private Optional<Despesa> buscarDadosDespesaParcelada(Despesa despesa) {
        if (despesa.isParcelado() && !despesa.isPrimeiraParcela()) {
            var parcelaAnterior = buscarParcelaAnterior.executar(despesa);
            parcelaAnterior.ifPresent(despesaHistorico -> {
                despesa.setObservacao(despesaHistorico.getObservacao());
                despesa.setRecorrente(despesaHistorico.getRecorrente());
                despesa.setCategoriaId(despesaHistorico.getCategoriaId());
                despesa.setDescricaoProcessada(despesaHistorico.getDescricaoProcessada());
            });
            return parcelaAnterior.isPresent() ? Optional.of(despesa) : Optional.empty();
        }
        return Optional.empty();
    }
}
