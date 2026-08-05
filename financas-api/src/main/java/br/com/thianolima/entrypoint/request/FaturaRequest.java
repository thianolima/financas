package br.com.thianolima.entrypoint.request;

import jakarta.validation.constraints.NotEmpty;


public record FaturaRequest (
    @NotEmpty
    String anoMes,
    @NotEmpty
    String nomeArquivo
) {}

