package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarParcelaAnterior;
import br.com.thianolima.core.provider.ProduzirComandoNovaNotificacao;
import br.com.thianolima.core.provider.SalvarDespesa;
import br.com.thianolima.model.Despesa;

public class ProcessarComandoNovaDespesaUseCase {

    private final ClassificarDespesaPorRegraUseCase classificarDespesaPorRegraUseCase;
    private final SalvarDespesa salvarDespesa;
    private final ProduzirComandoNovaNotificacao produzirComandoNovaNotificacao;
    private final BuscarParcelaAnterior buscarParcelaAnterior;

    public ProcessarComandoNovaDespesaUseCase(
            ClassificarDespesaPorRegraUseCase classificarDespesaPorRegraUseCase,
            SalvarDespesa salvarDespesa,
            ProduzirComandoNovaNotificacao produzirComandoNovaNotificacao,
            BuscarParcelaAnterior buscarParcelaAnterior
    ) {
        this.classificarDespesaPorRegraUseCase = classificarDespesaPorRegraUseCase;
        this.buscarParcelaAnterior = buscarParcelaAnterior;
        this.salvarDespesa = salvarDespesa;
        this.produzirComandoNovaNotificacao = produzirComandoNovaNotificacao;
    }

    public void executar(
            Despesa despesa,
            int sequencialAtual,
            int sequencialFinal
    ){
        var novaDespesa = classificarDespesaPorRegraUseCase.executar(despesa);
        if (novaDespesa.isParcelado() && !novaDespesa.isPrimeiraParcela()) {
            buscarParcelaAnterior.executar(despesa)
                    .ifPresent(despesaHistorico -> {
                        novaDespesa.setObservacao(despesaHistorico.getObservacao());
                        novaDespesa.setRecorrente(despesaHistorico.getRecorrente());
                    });
        }
        salvarDespesa.executar(novaDespesa);

        if(sequencialAtual == sequencialFinal){
            produzirComandoNovaNotificacao.executar(
                    despesa.getUsuarioId(),
                    "ProcessarComandoNovaDespesa",
                    "Importação dA fatura do cartão de credito feita com sucesso!"
            );
        }
    }
}
