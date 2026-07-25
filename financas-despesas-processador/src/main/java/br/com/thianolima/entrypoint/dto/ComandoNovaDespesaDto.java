package br.com.thianolima.entrypoint.dto;

import br.com.thianolima.model.Despesa;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComandoNovaDespesaDto {
    private Long usuarioId;
    private Long cartaoId;
    private Long faturaId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataDespesa;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataVencimento;

    private String descricao;
    private BigDecimal valor;
    private Integer parcelaAtual;
    private Integer totalParcelas;
    int sequencialAtual;
    int sequencialFinal;

    public Despesa toDespesa() {
        return Despesa.builder()
                .usuarioId(this.usuarioId)
                .cartaoId(this.cartaoId)
                .faturaId(this.faturaId)
                .dataDespesa(this.dataDespesa)
                .dataVencimento(this.dataVencimento)
                .descricaoOriginal(this.descricao)
                .valor(this.valor)
                .parcelaAtual(this.parcelaAtual != null ? parcelaAtual : 0)
                .totalParcelas(this.totalParcelas != null ? totalParcelas : 0)
                .recorrente(false)
                .build();
    }
}
