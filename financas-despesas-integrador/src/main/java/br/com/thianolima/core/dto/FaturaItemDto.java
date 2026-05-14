package br.com.thianolima.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaturaItemDto {
    Integer sequencia;
    String data;
    String descricao;
    String valor;
    Long faturaId;
    Long usuarioId;
    Long cartaoId;
}
