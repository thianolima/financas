package br.com.thianolima.core.provider.storage;

public interface CriarUrlPreAssinadaS3 {

    String executar(Long idCartao, Long idUsuario, String anoMes, String nomeArquivo);
}
