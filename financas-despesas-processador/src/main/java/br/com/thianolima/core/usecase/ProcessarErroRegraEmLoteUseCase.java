package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarDespesaPorId;
import br.com.thianolima.core.provider.ProduzirComandoNovaNotificacao;

public class ProcessarErroRegraEmLoteUseCase {

    private final ProduzirComandoNovaNotificacao produzirComandoNovaNotificacao;
    private final BuscarDespesaPorId buscarDespesaPorId;


    public ProcessarErroRegraEmLoteUseCase(
            ProduzirComandoNovaNotificacao produzirComandoNovaNotificacao,
            BuscarDespesaPorId buscarDespesaPorId
    ) {
        this.produzirComandoNovaNotificacao = produzirComandoNovaNotificacao;
        this.buscarDespesaPorId = buscarDespesaPorId;
    }

    public void executar(
            Long usuarioId,
            Long despesaId
    ){
        buscarDespesaPorId.executar(despesaId, usuarioId)
                .ifPresentOrElse(
                        despesaSalva -> {
                            String mensagem = String.format(
                                    "Ocorreu um erro ao aplicar as regras de categorização na despesa {} no valor {} da data {}",
                                    despesaSalva.getDescricaoProcessada(),
                                    despesaSalva.getValor(),
                                    despesaSalva.getDataDespesa()
                            );
                            produzirComandoNovaNotificacao.executar(
                                    usuarioId,
                                    "ProcessarRegrasEmLote",
                                    mensagem
                            );
                        },
                        () -> {
                            throw new RuntimeException("ERRRO: A despesa "+ despesaId +" não pertence ao usuário autenticado!");
                        }
                );
    }
}
