package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarDespesaPorId;
import br.com.thianolima.core.provider.ProduzirRetornoProcessarRegras;
import br.com.thianolima.core.provider.SalvarDespesa;

public class ProcessarRegrasEmLoteUseCase {

    private final BuscarDespesaPorId buscarDespesaPorId;
    private final ClassificarDespesaPorRegraUseCase classificarDespesaPorRegraUseCase;
    private final SalvarDespesa salvarDespesa;
    private final ProduzirRetornoProcessarRegras produzirRetornoProcessarRegras;

    public ProcessarRegrasEmLoteUseCase(
            BuscarDespesaPorId buscarDespesaPorId,
            ClassificarDespesaPorRegraUseCase classificarDespesaPorRegraUseCase,
            SalvarDespesa salvarDespesa,
            ProduzirRetornoProcessarRegras produzirRetornoProcessarRegras
    ) {
        this.buscarDespesaPorId = buscarDespesaPorId;
        this.classificarDespesaPorRegraUseCase = classificarDespesaPorRegraUseCase;
        this.salvarDespesa = salvarDespesa;
        this.produzirRetornoProcessarRegras = produzirRetornoProcessarRegras;
    }

    public void executar(
        Long usuarioId,
        Long despesaId,
        int sequencialAtual,
        int sequencialFinal
    ){
        buscarDespesaPorId.executar(despesaId, usuarioId)
            .ifPresentOrElse(
                    despesaSalva -> {
                        var despesaClassificada = classificarDespesaPorRegraUseCase.executar(despesaSalva);
                        salvarDespesa.executar(despesaClassificada);
                        if(sequencialAtual == sequencialFinal){
                            //produzirRetornoProcessarRegras
                        }
                    },
                    () -> {
                        throw new RuntimeException("A despesa "+ despesaId +" não pertence ao usuário autenticado!");
                    }
            );
    }
}
