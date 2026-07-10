package br.com.thianolima.core.model;

import lombok.AllArgsConstructor;

public record Notificacao (
    String id,
    Long usuarioId,
    String tipo,
    String dataHoraCriacao,
    String mensagem
) {}
