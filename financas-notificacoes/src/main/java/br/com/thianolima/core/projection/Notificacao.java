package br.com.thianolima.core.projection;

public record Notificacao (
    String id,
    Long usuarioId,
    String tipo,
    String dataHoraCriacao,
    String mensagem
) {}
