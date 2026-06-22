package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarCartaoPorId;
import br.com.thianolima.core.provider.database.BuscarCategoriaPorId;
import br.com.thianolima.core.provider.database.BuscarDespesaPorId;
import br.com.thianolima.core.provider.database.SalvarDespesa;
import br.com.thianolima.model.Despesa;

public class AlterarDespesaUsecase {

    private final SalvarDespesa salvarDespesa;
    private final BuscarDespesaPorId buscarDespesaPorId;
    private final BuscarCartaoPorId buscarCartaoPorId;
    private final BuscarCategoriaPorId buscarCategoriaPorId;

    public AlterarDespesaUsecase(
            SalvarDespesa salvarDespesa,
            BuscarDespesaPorId buscarDespesaPorId,
            BuscarCartaoPorId buscarCartaoPorId,
            BuscarCategoriaPorId buscarCategoriaPorId
    ) {
        this.salvarDespesa = salvarDespesa;
        this.buscarDespesaPorId = buscarDespesaPorId;
        this.buscarCartaoPorId = buscarCartaoPorId;
        this.buscarCategoriaPorId = buscarCategoriaPorId;
    }

    public void executar(Despesa despesa) {
        var despesaSalva = existeDespesa(despesa.getId(), despesa.getUsuarioId());
        existeCartao(despesa.getCartaoId(), despesa.getUsuarioId());
        existeCategoria(despesa.getCategoriaId(), despesa.getUsuarioId());
        salvarDespesa.executar(
                validarDados(despesaSalva, despesa)
        );
    }

    private void existeCategoria(Long categoriaId, Long usuarioId){
        buscarCategoriaPorId.executar(categoriaId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    private void existeCartao(Long cartaoId, Long usuarioId){
        buscarCartaoPorId.executar(cartaoId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));
    }

    private Despesa existeDespesa(Long despesaId, Long usuarioId){
        return buscarDespesaPorId.executar(despesaId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
    }

    private Despesa validarDados(Despesa despesaSalva, Despesa despesaNova){
        // CAMPOS LIVRES PARA ALTERACAO
        despesaSalva.setDescricaoProcessada(despesaNova.getDescricaoProcessada());
        despesaSalva.setCategoriaId(despesaNova.getCategoriaId());
        despesaSalva.setObservacao(despesaNova.getObservacao());
        despesaSalva.setRecorrente(despesaNova.getRecorrente());

        if(despesaSalva.isOrigemFatura()){
           return despesaSalva;
        }

        // CAMPOS EXCLUSIVOS PARA DESPESAS QUE NAO ORIGINARAM DE UM FATURA
        despesaSalva.setCartaoId(despesaNova.getCartaoId());
        despesaSalva.setParcelaAtual(despesaNova.getParcelaAtual());
        despesaSalva.setTotalParcelas(despesaNova.getTotalParcelas());
        despesaSalva.setDataDespesa(despesaNova.getDataDespesa());
        despesaSalva.setDataVencimento(despesaNova.getDataVencimento());
        despesaSalva.setValor(despesaNova.getValor());

        return despesaSalva;
    }
}
