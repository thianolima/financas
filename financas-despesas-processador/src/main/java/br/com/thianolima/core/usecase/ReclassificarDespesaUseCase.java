package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarDespesaPorFaturaIdESequencia;
import br.com.thianolima.core.provider.SalvarDespesa;

public class ReclassificarDespesaUseCase {

    private final ClassificarDespesaUseCase classificarDespesaUseCase;
    private final SalvarDespesa salvarDespesa;
    private final BuscarDespesaPorFaturaIdESequencia buscarDespesaPorFaturaIdESequencia;

    public ReclassificarDespesaUseCase(
            ClassificarDespesaUseCase classificarDespesaUseCase,
            SalvarDespesa salvarDespesa,
            BuscarDespesaPorFaturaIdESequencia buscarDespesaPorFaturaIdESequencia
    ) {
        this.classificarDespesaUseCase = classificarDespesaUseCase;
        this.salvarDespesa = salvarDespesa;
        this.buscarDespesaPorFaturaIdESequencia = buscarDespesaPorFaturaIdESequencia;
    }

    public void executar(Long faturaId, Integer sequencia ){
        var despesaSalva = buscarDespesaPorFaturaIdESequencia.executar(sequencia, faturaId)
                .orElseThrow(() -> new RuntimeException("Despesan nao encontrada!"));

        var despesaClassificada = classificarDespesaUseCase.executar(despesaSalva);

        salvarDespesa.executar(despesaClassificada);
    }
}
