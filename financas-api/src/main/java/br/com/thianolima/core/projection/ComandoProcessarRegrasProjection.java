package br.com.thianolima.core.projection;

public record ComandoProcessarRegrasProjection(
        Long usuarioId,
        Long despesaId,
        Integer sequencialAtual,
        Integer sequencialFinal
) {}