package br.com.thianolima.infrastructure.provider.message;

import br.com.thianolima.core.model.ComandoProcessarRegras;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComandoProcessarRegraDto {
    Long usuarioId;
    Long despesaId;
    int sequencialAtual;
    int sequencialFinal;

    public ComandoProcessarRegraDto(ComandoProcessarRegras comandoProcessarRegras){
        this.usuarioId = comandoProcessarRegras.usuarioId();
        this.despesaId = comandoProcessarRegras.despesaId();
        this.sequencialAtual = comandoProcessarRegras.sequencialAtual();
        this.sequencialFinal = comandoProcessarRegras.sequencialFinal();
    }
}
