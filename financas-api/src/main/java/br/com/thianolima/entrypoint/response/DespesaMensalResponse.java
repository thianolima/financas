package br.com.thianolima.entrypoint.response;

import br.com.thianolima.model.DespesaMensal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class DespesaMensalResponse {
    List<DespesaMensalItemResponse> data;

    public record DespesaMensalItemResponse(
            String anoMes,
            BigDecimal valorTotal
    ) {}

    public DespesaMensalResponse(List<DespesaMensal> despesas) {
        this.data = despesas.stream().map(despesa ->
                new DespesaMensalItemResponse(
                    despesa.periodo().format(DateTimeFormatter.ofPattern("yyyyMM")),
                    despesa.valorTotal()
                )
        ).toList();
    }
}
