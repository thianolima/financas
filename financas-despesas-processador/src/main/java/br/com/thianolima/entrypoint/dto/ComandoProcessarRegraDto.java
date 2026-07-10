package br.com.thianolima.entrypoint.dto;

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
}
