package br.com.thianolima.infrastructure.provider.sqs.dto;

import br.com.thianolima.core.model.ComandoProcessarRegras;


public record ComandoProcessarRegraDto (
    Long usuarioId,
    Long despesaId,
    int sequencialAtual,
    int sequencialFinal
){
    public ComandoProcessarRegraDto(ComandoProcessarRegras comandoProcessarRegras){
        this(
                comandoProcessarRegras.usuarioId(),
                comandoProcessarRegras.despesaId(),
                comandoProcessarRegras.sequencialAtual(),
                comandoProcessarRegras.sequencialFinal()
        );
    }
}
