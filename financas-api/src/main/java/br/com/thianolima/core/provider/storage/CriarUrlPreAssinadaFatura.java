package br.com.thianolima.core.provider.storage;

public interface CriarUrlPreAssinadaFatura {

    String executar(Long idCartao, Long idUsuario, String anoMes, String nomeArquivo);
}
