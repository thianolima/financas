package br.com.thianolima.entrypoint.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest (
    @NotBlank(message = "{NotBlank.TokenRequest.email}")
    String email,

    @NotBlank(message = "{NotBlank.TokenRequest.senha}")
    String senha
){}
