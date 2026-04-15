package br.com.thianolima.core.provider;

import br.com.thianolima.core.dto.DespesaCsvDto;

public interface ProduzirComandoNovaDespesa {
    boolean executar(DespesaCsvDto despesa);
}
