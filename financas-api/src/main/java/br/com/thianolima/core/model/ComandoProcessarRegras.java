package br.com.thianolima.core.model;

public record ComandoProcessarRegras (
        Long usuarioId,
        Long despesaId,
        Integer sequencialAtual,
        Integer sequencialFinal
) {}