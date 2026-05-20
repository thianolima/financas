package br.com.thianolima.entrypoint.dto;

import br.com.thianolima.model.Despesa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaturaItemDto {
    Integer sequencia;
    String dataDespesa;
    String dataVencimento;
    String descricao;
    String valor;
    Long faturaId;
    Long usuarioId;
    Long cartaoId;

    public Despesa toDespesa() {
        return Despesa.builder()
                .faturaId(this.getFaturaId())
                .usuarioId(this.getUsuarioId())
                .cartaoId(this.getCartaoId())
                .valor(new BigDecimal(this.getValor()))
                .descricaoOriginal(this.getDescricao())
                .descricaoProcessada(this.getDescricao())
                .sequencia(this.getSequencia())
                .dataDespesa(LocalDate.parse(this.getDataDespesa()))
                .dataVencimento(LocalDate.parse(this.getDataVencimento()))
                .recorrente(false)
                .build();
    }
}
