package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarRegraPorTermo;
import br.com.thianolima.model.Despesa;

import java.util.Optional;

public class ClassificarDespesaPorRegraUseCase {

    private final BuscarRegraPorTermo buscarRegraPorTermo;
    private static final Long ID_CATEGORIA_OUTROS = 0L;

    public ClassificarDespesaPorRegraUseCase(
            BuscarRegraPorTermo buscarRegraPorTermo
    ) {
        this.buscarRegraPorTermo = buscarRegraPorTermo;
    }

    public Optional<Despesa> executar(Despesa despesa){
        buscarRegraPorTermo.executar(
                despesa.getDescricaoOriginal(),
                despesa.getUsuarioId()
        )
        .ifPresentOrElse(
                regra -> {
                    despesa.setCategoriaId(regra.getCategoriaId());
                    despesa.setDescricaoProcessada(regra.getDescricao());
                },
                () -> {
                    despesa.setCategoriaId(ID_CATEGORIA_OUTROS);
                    despesa.setDescricaoProcessada(despesa.getDescricaoOriginal());
                }
        );
        return Optional.of(despesa);
    }
}
