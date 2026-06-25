package br.com.thianolima.core.usecase;

import br.com.thianolima.core.model.ComandoProcessarRegras;
import br.com.thianolima.core.provider.database.ValidarDespesasPertecemUsuario;
import br.com.thianolima.core.provider.message.ProduzirComandoProcessarRegras;

import java.util.List;

public class PorcessarRegrasEmLoteUseCase {

    private final ValidarDespesasPertecemUsuario validarDespesasPertecemUsuario;
    private final ProduzirComandoProcessarRegras produzirComandoProcessarRegras;

    public PorcessarRegrasEmLoteUseCase(
            ValidarDespesasPertecemUsuario validarDespesasPertecemUsuario,
            ProduzirComandoProcessarRegras produzirComandoProcessarRegras
    ) {
        this.validarDespesasPertecemUsuario = validarDespesasPertecemUsuario;
        this.produzirComandoProcessarRegras = produzirComandoProcessarRegras;
    }

    public void executar(
            List<Long> despesasIds,
            Long usuarioId
    ) {
        if (!validarDespesasPertecemUsuario.executar(despesasIds, usuarioId))
            new RuntimeException("Uma ou mais despesas informadas não pertencem ao usuário autenticado !");

        int sequencialFinal = despesasIds.size();
        int sequencialAtual = 1;
        while (sequencialAtual <= despesasIds.size()){
            var comandoProcessarRegra = new ComandoProcessarRegras(
                    despesasIds.get(sequencialAtual-1),
                    usuarioId,
                    sequencialAtual,
                    sequencialFinal
            );
            produzirComandoProcessarRegras.executar(comandoProcessarRegra);
            sequencialAtual++;
        }
    }
}
