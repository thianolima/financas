package br.com.thianolima.entrypoint.response;

import br.com.thianolima.core.model.DespesaMensal;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class DespesaMensalResponse {
    private final List<DespesaMensalItemResponse> data;

    public record DespesaMensalItemResponse(
            String anoMes,
            BigDecimal valorTotal
    ) {}

    public DespesaMensalResponse(List<DespesaMensal> despesas) {
        this.data = despesas.stream().map(despesa ->
                new DespesaMensalItemResponse(
                    despesa.getAnoMes().format(DateTimeFormatter.ofPattern("yyyyMM")),
                    despesa.getValorTotal()
                )
        ).toList();
    }
}
