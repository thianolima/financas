package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarFaturaPorId;
import br.com.thianolima.core.provider.ProduzirRetornoNovaFatura;
import br.com.thianolima.core.provider.SalvarDespesa;
import br.com.thianolima.model.Despesa;
import br.com.thianolima.model.Fatura;

public class ProcessarDespesaFaturaUseCase {

    private final ClassificarDespesaUseCase classificarDespesaUseCase;
    private final ProduzirRetornoNovaFatura produzirRetornoNovaFatura;
    private final BuscarFaturaPorId buscarFaturaPorId;
    private final SalvarDespesa salvarDespesa;

    public ProcessarDespesaFaturaUseCase(
            ClassificarDespesaUseCase classificarDespesaUseCase,
            ProduzirRetornoNovaFatura produzirRetornoNovaFatura,
            BuscarFaturaPorId buscarFaturaPorId,
            SalvarDespesa salvarDespesa
    ) {
        this.classificarDespesaUseCase = classificarDespesaUseCase;
        this.produzirRetornoNovaFatura = produzirRetornoNovaFatura;
        this.buscarFaturaPorId = buscarFaturaPorId;
        this.salvarDespesa = salvarDespesa;
    }

    public void executar(Despesa despesa){
        var fatura = buscarFaturaPorId.executar(despesa.getFaturaId())
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        var despesaClassificada = classificarDespesaUseCase.executar(despesa);

        if(isUltimaDespesaDaFatura(despesaClassificada, fatura)) {
            produzirRetornoNovaFatura.executar(fatura.getId());
        }

        salvarDespesa.executar(despesaClassificada);
    }

    private boolean isUltimaDespesaDaFatura(Despesa despesa, Fatura fatura){
        return fatura.getQuantidadeDespesas().equals(despesa.getSequencia());
    }

}

