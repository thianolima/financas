package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarDespesaPorId;
import br.com.thianolima.core.provider.database.ExcluirDespesa;

public class ExcluirDespesaUseCase {

    private final ExcluirDespesa excluirDespesa;
    private final BuscarDespesaPorId buscarDespesaPorId;

    public ExcluirDespesaUseCase(
            ExcluirDespesa excluirDespesa, BuscarDespesaPorId buscarDespesaPorId
    ) {
        this.excluirDespesa = excluirDespesa;
        this.buscarDespesaPorId = buscarDespesaPorId;
    }

    public void executar(Long despesaId, Long usuarioId){
        var despesa = buscarDespesaPorId.executar(despesaId,usuarioId)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));

        excluirDespesa.executar(despesaId);
    }
}
