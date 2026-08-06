package br.com.thianolima.core.provider;

import br.com.thianolima.model.Despesa;

public interface ProduzirComandoNovaDespesa {
    void executar(Despesa despesa, int sequenciaAtual, int sequenciaFinal);
}
