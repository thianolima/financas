package br.com.thianolima.entrypoint.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FaturaRequest {
    @NotEmpty
    private String anoMes;
    @NotEmpty
    private String nomeArquivo;
}

