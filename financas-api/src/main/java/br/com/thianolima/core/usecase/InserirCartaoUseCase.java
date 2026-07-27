package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarCartoesPorUsuario;
import br.com.thianolima.core.provider.database.SalvarCartao;
import br.com.thianolima.model.Cartao;

public class InserirCartaoUseCase {

    private final SalvarCartao salvarCartao;
    private final BuscarCartoesPorUsuario buscarCartoesPorUsuario;

    public InserirCartaoUseCase(
            SalvarCartao salvarCartao,
            BuscarCartoesPorUsuario buscarCartoesPorUsuario
    ) {
        this.salvarCartao = salvarCartao;
        this.buscarCartoesPorUsuario = buscarCartoesPorUsuario;
    }

    public void executar(Cartao cartao){
        validarDuplicidade(cartao);
        salvarCartao.executar(cartao);
    }

    private void validarDuplicidade(Cartao cartao){
        var cartoes = buscarCartoesPorUsuario.executar(cartao.getUsuarioId());

        boolean cartaodDuplicado = cartoes.stream()
                .anyMatch(cartaoSalvo ->
                        cartaoSalvo.getNumeroFinal().equals(cartao.getNumeroFinal()) &&
                        cartaoSalvo.getBandeira().equals(cartao.getBandeira())
                );

        if (cartaodDuplicado)
            throw new RuntimeException("Cartao já cadastrado");
    }
}
