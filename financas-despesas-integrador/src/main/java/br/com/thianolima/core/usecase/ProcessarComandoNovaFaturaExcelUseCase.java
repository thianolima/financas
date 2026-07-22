package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.*;
import br.com.thianolima.model.Despesa;
import br.com.thianolima.model.Fatura;
import br.com.thianolima.model.FaturaSituacaoEnum;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class ProcessarComandoNovaFaturaExcelUseCase {

    private final CarregarFaturaExcel carregarFaturaExcel;
    private final ProduzirComandoNovaDespesa produzirComandoNovaDespesa;
    private final SalvarFatura salvarFatura;
    private final BuscarCartaoPorId buscarCartaoPorId;
    private final BuscarFaturaPorCartaoIdEAnoMes buscarFaturaPorCartaoIdEAnoMes;

    public void executar(
            Long usuarioId,
            Long cartaoId,
            String anoMes,
            String s3Bucket,
            String s3Key
    ) {
        validarCartao(usuarioId, cartaoId, anoMes);

        var despesasExcel = carregarFaturaExcel.executar(usuarioId, s3Bucket, s3Key);

        if (despesasExcel.isEmpty()) {
            throw new RuntimeException("Arquivo excel sem despesas");
        }

        // Extrair id dos cartoes utilizados nas despesas da fatura
        var cartoesIdFatura = despesasExcel.stream()
                .map(Despesa::getCartaoId)
                .collect(Collectors.toSet());

        // Salvar fatura para cada cartao utilizado
        HashMap<Long, Long> faturaIdPorCartao = new HashMap<>();
        cartoesIdFatura.forEach(cartaoIdFatura -> {
            validarCartao(usuarioId, cartaoIdFatura, anoMes);
            var fatura = salvarFatura.executar(
                    Fatura.builder()
                            .dataCriacao(LocalDateTime.now())
                            .dataVencimento(despesasExcel.getFirst().getDataVencimento())
                            .situacao(FaturaSituacaoEnum.PROCESSANDO)
                            .quantidadeDespesas(despesasExcel.size())
                            .cartaoId(cartaoIdFatura)
                            .usuarioId(usuarioId)
                            .anoMes(anoMes)
                            .s3Bucket(s3Bucket)
                            .s3Key(s3Key)
                            .build()
            );
            faturaIdPorCartao.put(cartaoIdFatura, fatura.getId());
        });

        IntStream.range(0, despesasExcel.size()).forEach(i -> {
            var novaDespesa = despesasExcel.get(i);
            int sequenciaAtual = i + 1;
            int sequenciaFinal = despesasExcel.size();
            var isDesconto = novaDespesa.getValor().compareTo(BigDecimal.ZERO) <= 0;

            if (!isDesconto) {
                novaDespesa.setFaturaId(faturaIdPorCartao.get(novaDespesa.getCartaoId()));
                produzirComandoNovaDespesa.executar(novaDespesa, sequenciaAtual, sequenciaFinal);
            }
        });
    }

    private void validarCartao(Long usuarioId, Long cartaoId, String anoMes){
        //TODO: Alterar busca de cartao de credito por cartaoId e usuarioId
        buscarCartaoPorId.executar(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartao nao encontrado"));

        buscarFaturaPorCartaoIdEAnoMes.executar(cartaoId, anoMes)
                .ifPresent(fatura -> {
                    throw new RuntimeException("Fatura ja importada para esse ano e mes");
                });
    }
}
