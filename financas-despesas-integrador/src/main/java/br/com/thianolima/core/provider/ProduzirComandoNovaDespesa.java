package br.com.thianolima.core.provider;

import br.com.thianolima.core.dto.FaturaItemDto;

public interface ProduzirComandoNovaDespesa {
    boolean executar(FaturaItemDto faturaItem);
}
