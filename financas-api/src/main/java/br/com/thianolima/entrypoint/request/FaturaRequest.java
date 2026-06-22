package br.com.thianolima.entrypoint.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FaturaRequest {
    private String anoMes;
    private String nomeArquivo;
}

