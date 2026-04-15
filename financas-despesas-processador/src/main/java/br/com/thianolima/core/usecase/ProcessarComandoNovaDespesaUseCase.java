
package br.com.thianolima.core.usecase;

import br.com.thianolima.core.dto.DespesaCsvDto;
import br.com.thianolima.core.provider.*;
import br.com.thianolima.model.Despesa;
import br.com.thianolima.model.Fatura;
import br.com.thianolima.model.Fornecedor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ProcessarComandoNovaDespesaUseCase {

    private final BuscarFaturaPorId buscarFaturaPorId;
    private final SalvarDespesa salvarDespesa;
    private final BuscarFornecedoresPorUsuarioId buscarFornecedoresPorUsuarioId;
    private final BuscarParcelaAnterior buscarParcelaAnterior;
    private final BuscarDespesaRecorrente buscarDespesaRecorrente;
    private final ProduzirRetornoNovaFatura produzirRetornoNovaFatura;
    private static final Pattern PARCELA_PATTERN = Pattern.compile("(\\d+)/(\\d+)$");

    public ProcessarComandoNovaDespesaUseCase(
            BuscarFaturaPorId buscarFaturaPorId,
            SalvarDespesa salvarDespesa,
            BuscarFornecedoresPorUsuarioId buscarFornecedoresPorUsuarioId,
            BuscarParcelaAnterior buscarParcelaAnterior,
            BuscarDespesaRecorrente buscarDespesaRecorrente,
            ProduzirRetornoNovaFatura produzirRetornoNovaFatura
    ) {
        this.buscarFaturaPorId = buscarFaturaPorId;
        this.salvarDespesa = salvarDespesa;
        this.buscarFornecedoresPorUsuarioId = buscarFornecedoresPorUsuarioId;
        this.buscarParcelaAnterior = buscarParcelaAnterior;
        this.buscarDespesaRecorrente = buscarDespesaRecorrente;
        this.produzirRetornoNovaFatura = produzirRetornoNovaFatura;
    }

    public void executar(
            DespesaCsvDto despesaCsvDto
    ){
        var fatura = buscarFaturaPorId.executar(despesaCsvDto.getFaturaId())
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        var novaDespesa = criarNovaDespesa(despesaCsvDto, fatura);

        buscarDadosParcelaAnterior(novaDespesa)
        .or(() -> buscarDadosDespesaRecorrente(novaDespesa))
        .or(() -> categorizarDespesa(novaDespesa))
        .ifPresent(depesaSalva -> {
            novaDespesa.setCategoriaId(depesaSalva.getCategoriaId());
            novaDespesa.setDescricaoProcessada(depesaSalva.getDescricaoProcessada());
            novaDespesa.setFornecedorId(depesaSalva.getFornecedorId());
            novaDespesa.setObservacao(depesaSalva.getObservacao());
            novaDespesa.setRecorrente(depesaSalva.getRecorrente());
        });

        salvarDespesa.executar(novaDespesa);

        if(isUltimaDespesaProcessada(novaDespesa, fatura))
            produzirRetornoNovaFatura.executar(fatura.getId());
    }

    private boolean isUltimaDespesaProcessada(Despesa despesa, Fatura fatura){
        return fatura.getQuantidadeDespesas().equals(despesa.getSequencia());
    }

    private Despesa criarNovaDespesa(DespesaCsvDto despesaCsvDto, Fatura fatura){
        var parcelAtual = extrairParcelaAtual(despesaCsvDto.getDescricao());
        var totalParcelas = extrairTotalParcelas(despesaCsvDto.getDescricao());
        return Despesa.builder()
                .faturaId(fatura.getId())
                .usuarioId(fatura.getUsuarioId())
                .cartaoId(fatura.getCartaoId())
                .valor(new BigDecimal(despesaCsvDto.getValor()))
                .parcelaAtual(parcelAtual)
                .totalParcelas(totalParcelas)
                .descricaoOriginal(despesaCsvDto.getDescricao())
                .descricaoProcessada(despesaCsvDto.getDescricao())
                .sequencia(despesaCsvDto.getSequencia())
                .dataDespesa(LocalDate.parse(despesaCsvDto.getData()))
                .recorrente(false)
                .build();
    }

    private Optional<Despesa> buscarDadosParcelaAnterior(Despesa novaDespesa){
        if (novaDespesa.isParcelado() && !novaDespesa.isPrimeiraParcela()){
            return buscarParcelaAnterior.executar(
                    novaDespesa.getDataDespesa(),
                    novaDespesa.getValor(),
                    novaDespesa.getCartaoId(),
                    novaDespesa.getParcelaAnterior()
            );
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
        var fornecedores = buscarFornecedoresPorUsuarioId.executar(despesa.getUsuarioId());
        String descricaoOriginal = despesa.getDescricaoOriginal().toUpperCase();
        return fornecedores.stream()
                .flatMap(fornecedor -> Arrays.stream(fornecedor.getPalavrasChave().split(","))
                        .map(palavra -> new FornecedorMatch(palavra.trim().toUpperCase(), fornecedor))
                )
                .sorted(Comparator.comparingInt((FornecedorMatch m) -> m.palavraChave().length()).reversed())
                .filter(match -> descricaoOriginal.contains(match.palavraChave()))
                .map(FornecedorMatch::fornecedor)
                .findFirst()
                .map(f -> Despesa.builder()
                        .categoriaId(f.getCategoriaId())
                        .descricaoProcessada(f.getNome())
                        .fornecedorId(f.getId())
                        .recorrente(false)
                        .build()
                );
    }

    private record FornecedorMatch(String palavraChave, Fornecedor fornecedor) {}
}

