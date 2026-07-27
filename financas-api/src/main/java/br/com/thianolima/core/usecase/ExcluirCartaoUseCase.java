package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarCartaoPorId;
import br.com.thianolima.core.provider.database.BuscarDespesasPorCartao;
import br.com.thianolima.core.provider.database.ExcluirCartao;
import br.com.thianolima.model.Despesa;

import java.util.List;

public class ExcluirCartaoUseCase {

    private final BuscarCartaoPorId buscarCartaoPorId;
    private final ExcluirCartao excluirCartao;
    private final BuscarDespesasPorCartao buscarDespesasPorCartao;

    public ExcluirCartaoUseCase(
            BuscarCartaoPorId buscarCartaoPorId,
            ExcluirCartao excluirCartao,
            BuscarDespesasPorCartao buscarDespesasPorCartao
    ) {
        this.buscarCartaoPorId = buscarCartaoPorId;
        this.excluirCartao = excluirCartao;
        this.buscarDespesasPorCartao = buscarDespesasPorCartao;
    }

    public void executar(
        Long cartaoId,
        Long usuarioId
    ){
        buscarCartaoPorId.executar(cartaoId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        List<Despesa> despesas = buscarDespesasPorCartao.executar(cartaoId);
        if(despesas.size() > 0)
            throw new RuntimeException("Nao se pode excluir um cartao de credito com despesas vinculadas");

        excluirCartao.executar(cartaoId);
    }
}
