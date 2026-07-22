package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarCartaoPorId;
import br.com.thianolima.core.provider.storage.CriarUrlPreAssinadaS3;

public class UploadFaturaCartaoUseCase {

    private CriarUrlPreAssinadaS3 criarUrlPreAssinadaS3;
    private BuscarCartaoPorId buscarCartaoPorId;

    public UploadFaturaCartaoUseCase(
            CriarUrlPreAssinadaS3 criarUrlPreAssinadaS3,
            BuscarCartaoPorId buscarCartaoPorId
    ){
        this.criarUrlPreAssinadaS3 = criarUrlPreAssinadaS3;
        this.buscarCartaoPorId = buscarCartaoPorId;
    }

    public String executar(
            Long cartaoId,
            Long usuarioId,
            String anoMes,
            String nomeArquivo
    ) {
        if (!cartaoPertenceUsuario(cartaoId, usuarioId)) {
            throw new RuntimeException("Cartao nao pertence ao usuario");
        }
        return criarUrlPreAssinadaS3.executar(cartaoId, usuarioId, anoMes, nomeArquivo);
    }

    private Boolean cartaoPertenceUsuario(
            Long cartaoId,
            Long usuarioId
    ){
        return buscarCartaoPorId.executar(cartaoId, usuarioId).isPresent();
    }
}
