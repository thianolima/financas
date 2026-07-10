package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarParcelaAnterior;
import br.com.thianolima.model.Despesa;

public class EnriquecerDespesaPorHistoricoUseCase {

    private final BuscarParcelaAnterior buscarParcelaAnterior;

    public EnriquecerDespesaPorHistoricoUseCase(
            BuscarParcelaAnterior buscarParcelaAnterior
    ) {
        this.buscarParcelaAnterior = buscarParcelaAnterior;
    }

    public Despesa executar(Despesa despesa) {
        if (despesa.isParcelado() && !despesa.isPrimeiraParcela()) {
            buscarParcelaAnterior.executar(
                    despesa.getDataDespesa(),
                    despesa.getValor(),
                    despesa.getCartaoId(),
                    despesa.getParcelaAnterior()
            )
            .ifPresent(despesaHistorico -> {
                despesa.setObservacao(despesaHistorico.getObservacao());
            });
            return despesa;
        }
        return  despesa;
    }
}
