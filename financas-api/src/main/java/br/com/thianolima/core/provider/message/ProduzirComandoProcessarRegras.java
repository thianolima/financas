package br.com.thianolima.core.provider.message;

import br.com.thianolima.core.projection.ComandoProcessarRegrasProjection;

public interface ProduzirComandoProcessarRegras {

    void executar(ComandoProcessarRegrasProjection comandoProcessarRegrasProjection);
}
