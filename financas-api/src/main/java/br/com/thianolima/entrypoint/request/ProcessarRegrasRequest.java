package br.com.thianolima.entrypoint.request;

import software.amazon.awssdk.annotations.NotNull;

import java.util.List;

public record ProcessarRegrasRequest (
    @NotNull
    List<Long> despesasIds
) {}

