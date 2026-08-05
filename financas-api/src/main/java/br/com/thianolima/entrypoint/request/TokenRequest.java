package br.com.thianolima.entrypoint.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRequest {
    @NotBlank(message = "{NotBlank.TokenRequest.email}")
    private String email;

    @NotBlank(message = "{NotBlank.TokenRequest.senha}")
    private String senha;
}
