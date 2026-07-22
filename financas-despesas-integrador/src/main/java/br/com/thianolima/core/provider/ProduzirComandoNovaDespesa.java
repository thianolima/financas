package br.com.thianolima.core.provider;

import br.com.thianolima.core.dto.FaturaItemDto;
import br.com.thianolima.model.Despesa;

public interface ProduzirComandoNovaDespesa {
    boolean executar(FaturaItemDto faturaItem);
    void executar(Despesa despesa, int sequenciaAtual, int sequenciaFinal);
}
