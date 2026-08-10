package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarRegraPorTermoBusca;
import br.com.thianolima.core.provider.database.SalvarRegra;
import br.com.thianolima.model.Regra;

public class InserirRegraUseCase {

    private final SalvarRegra salvarRegra;
    private final BuscarRegraPorTermoBusca buscarRegraPorTermoBusca;

    public InserirRegraUseCase(
            SalvarRegra salvarRegra,
            BuscarRegraPorTermoBusca buscarRegraPorTermoBusca
    ) {
        this.salvarRegra = salvarRegra;
        this.buscarRegraPorTermoBusca = buscarRegraPorTermoBusca;
    }

    public void executar(Regra regra){
        validarTermos(regra);
        salvarRegra.executar(regra);
    }

    private void validarTermos(Regra regra){
        regra.getTermos().forEach(regraTermo -> {
            var regraSalva = buscarRegraPorTermoBusca.executar(regraTermo.getTermoBusca(), regra.getUsuarioId());
            if(regraSalva.isPresent())
                throw new RuntimeException("Termo já utilizado na regra:" + regra.getId());
        });
    }

}
