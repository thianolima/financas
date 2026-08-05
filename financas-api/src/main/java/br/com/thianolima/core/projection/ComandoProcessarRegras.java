package br.com.thianolima.core.projection;

public record ComandoProcessarRegras (
        Long usuarioId,
        Long despesaId,
        Integer sequencialAtual,
        Integer sequencialFinal
) {}