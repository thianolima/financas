package br.com.thianolima.core.provider.message;

import br.com.thianolima.core.projection.ComandoProcessarRegras;

public interface ProduzirComandoProcessarRegras {

    void executar(ComandoProcessarRegras comandoProcessarRegras);
}
