package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarFaturaPorId;
import br.com.thianolima.core.provider.SalvarFatura;
import br.com.thianolima.model.FaturaSituacaoEnum;

import java.time.LocalDateTime;

public class ProcessarRetornoNovaFaturaUseCase {

    private final SalvarFatura salvarFatura;
    private final BuscarFaturaPorId buscarFaturaPorId;

    public ProcessarRetornoNovaFaturaUseCase(
            SalvarFatura salvarFatura,
            BuscarFaturaPorId buscarFaturaPorId
    ) {
        this.salvarFatura = salvarFatura;
        this.buscarFaturaPorId = buscarFaturaPorId;
    }

    public void executar(
            Long faturaId,
            FaturaSituacaoEnum situacao,
            LocalDateTime dataConclusao
    ){
        buscarFaturaPorId.executar(faturaId).ifPresent(f -> {
            f.setDataConclusao(dataConclusao);
            f.setSituacao(situacao);
            salvarFatura.executar(f);
        });
    }
}
