package br.com.thianolima.infrastructure.provider.sqs.dto;

import software.amazon.awssdk.annotations.NotNull;

public record NotificacaoDto (
    @NotNull
    Long usuarioId,
    @NotNull
    String tipo,
    @NotNull
    String mensagem
){}
