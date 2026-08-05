package br.com.thianolima.entrypoint.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProcessarRegrasRequest(
        @NotNull
        List<Long> despesasIds
) {}

