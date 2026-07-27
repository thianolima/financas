package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarCartaoPorId;
import br.com.thianolima.core.provider.database.BuscarCartoesPorUsuario;
import br.com.thianolima.core.provider.database.SalvarCartao;
import br.com.thianolima.model.Cartao;

public class AlterarCartaoUseCase {

    private final SalvarCartao salvarCartao;
    private final BuscarCartoesPorUsuario buscarCartoesPorUsuario;
    private final BuscarCartaoPorId buscarCartaoPorId;

    public AlterarCartaoUseCase(
            SalvarCartao salvarCartao,
            BuscarCartoesPorUsuario buscarCartoesPorUsuario,
            BuscarCartaoPorId buscarCartaoPorId
    ) {
        this.salvarCartao = salvarCartao;
        this.buscarCartoesPorUsuario = buscarCartoesPorUsuario;
        this.buscarCartaoPorId = buscarCartaoPorId;
    }

    public void executar(Cartao cartao){
        validarAlteracao(cartao);
        salvarCartao.executar(cartao);
    }

    private void validarAlteracao(Cartao novoCartao){
        var cartaoSalvo = buscarCartaoPorId.executar(novoCartao.getId(), novoCartao.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        var alterouNumeroFinal = !novoCartao.getNumeroFinal().equals(cartaoSalvo.getNumeroFinal());
        var alterouBandeira = !novoCartao.getBandeira().equals(cartaoSalvo.getBandeira());

        if(alterouBandeira || alterouNumeroFinal)
            validarDuplicadeDados(novoCartao);
    }

    private void validarDuplicadeDados(Cartao novoCartao){
        var cartoes = buscarCartoesPorUsuario.executar(novoCartao.getUsuarioId());
        boolean cartaodDuplicado = cartoes.stream()
                .anyMatch(cartao ->
                        cartao.getNumeroFinal().equals(novoCartao.getNumeroFinal()) &&
                                cartao.getBandeira().equals(novoCartao.getBandeira())
                );

        if (cartaodDuplicado)
            throw new RuntimeException("Cartao já cadastrado");
    }
}
