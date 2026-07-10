package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarDespesaPorId;
import br.com.thianolima.core.provider.ProduzirComandoNovaNotificacao;
import br.com.thianolima.core.provider.SalvarDespesa;

public class ProcessarRegrasEmLoteUseCase {

    private final BuscarDespesaPorId buscarDespesaPorId;
    private final ClassificarDespesaPorRegraUseCase classificarDespesaPorRegraUseCase;
    private final SalvarDespesa salvarDespesa;
    private final ProduzirComandoNovaNotificacao produzirComandoNovaNotificacao;

    public ProcessarRegrasEmLoteUseCase(
            BuscarDespesaPorId buscarDespesaPorId,
            ClassificarDespesaPorRegraUseCase classificarDespesaPorRegraUseCase,
            SalvarDespesa salvarDespesa,
            ProduzirComandoNovaNotificacao produzirComandoNovaNotificacao
    ) {
        this.buscarDespesaPorId = buscarDespesaPorId;
        this.classificarDespesaPorRegraUseCase = classificarDespesaPorRegraUseCase;
        this.salvarDespesa = salvarDespesa;
        this.produzirComandoNovaNotificacao = produzirComandoNovaNotificacao;
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
                            produzirComandoNovaNotificacao.executar(
                                    usuarioId,
                                    "ProcessarRegrasEmLote",
                                    "Termino da aplicação de regras de categorização nas despesas enviadas em lote!"
                            );
                        }
                    },
                    () -> {
                        throw new RuntimeException("ERRRO: A despesa "+ despesaId +" não pertence ao usuário autenticado!");
                    }
            );
    }
}
