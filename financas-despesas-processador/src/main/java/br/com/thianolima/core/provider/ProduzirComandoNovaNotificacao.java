package br.com.thianolima.core.provider;

public interface ProduzirComandoNovaNotificacao {
    void executar(Long usuarioId, String tipo, String mensagem);
}
