package br.com.thianolima.infrastructure.provider.sqs.dto;

import br.com.thianolima.core.projection.ComandoProcessarRegrasProjection;

public record ComandoProcessarRegraDto(
        Long usuarioId,
        Long despesaId,
        int sequencialAtual,
        int sequencialFinal
) {

    public ComandoProcessarRegraDto(ComandoProcessarRegrasProjection comandoProcessarRegrasProjection){
        this(
                comandoProcessarRegrasProjection.usuarioId(),
                comandoProcessarRegrasProjection.despesaId(),
                comandoProcessarRegrasProjection.sequencialAtual(),
                comandoProcessarRegrasProjection.sequencialFinal()
        );
    }
}
