package br.com.thianolima.entrypoint.controller;


import br.com.thianolima.core.usecase.AutenticarUsuarioUseCase;
import br.com.thianolima.entrypoint.request.TokenRequest;
import br.com.thianolima.entrypoint.response.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    public TokenController(
           AutenticarUsuarioUseCase autenticarUsuarioUseCase
    ) {
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
    }

    @PostMapping()
    public ResponseEntity<TokenResponse> autenticar(
            @Valid @RequestBody TokenRequest request
    ) {
        var token = autenticarUsuarioUseCase.executar(
                request.getEmail(),
                request.getSenha()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new TokenResponse(
                                token,
                                LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
                        )
                );
    }

}
