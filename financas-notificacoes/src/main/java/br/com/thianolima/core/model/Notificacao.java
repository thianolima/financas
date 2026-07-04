package br.com.thianolima.core.model;

public record Notificacao (
    String id,
    Long usuarioId,
    String tipo,
    String dataHoraCriacao,
    String mensagem
) {}
