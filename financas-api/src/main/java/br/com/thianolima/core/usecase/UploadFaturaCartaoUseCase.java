package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarCartaoPorId;
import br.com.thianolima.core.provider.storage.CriarUrlPreAssinadaFatura;

public class UploadFaturaCartaoUseCase {

    private CriarUrlPreAssinadaFatura criarUrlPreAssinadaFatura;
    private BuscarCartaoPorId buscarCartaoPorId;

    public UploadFaturaCartaoUseCase(
            CriarUrlPreAssinadaFatura criarUrlPreAssinadaFatura,
            BuscarCartaoPorId buscarCartaoPorId
    ){
        this.criarUrlPreAssinadaFatura = criarUrlPreAssinadaFatura;
        this.buscarCartaoPorId = buscarCartaoPorId;
    }

    public String executar(
            Long cartaoId,
            Long usuarioId,
            String anoMes,
            String nomeArquivo
    ){
        var cartao = buscarCartaoPorId.executar(cartaoId)
                .orElseThrow(() -> new RuntimeException());

        if(!cartao.isUsuario(usuarioId))
            throw new RuntimeException("Usuario nao pertencete ao cartao");

        return criarUrlPreAssinadaFatura.executar(cartaoId, usuarioId, anoMes, nomeArquivo);
    }
}
