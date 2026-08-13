package br.com.thianolima.entrypoint.controller;


import br.com.thianolima.core.usecase.GerarDashboardUseCase;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final Tracer tracer;
    private final GerarDashboardUseCase gerarDashboardUseCase;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> listar(
            JwtAuthenticationToken token,
            @RequestParam(value = "datareferencia", required = false) LocalDate datareferencia
    ) {
        ScopedSpan span = tracer.startScopedSpan("dashboard");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var response = gerarDashboardUseCase.executar(
                    datareferencia != null ? datareferencia : LocalDate.now(),
                    usuarioId
            );
            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            span.end();
        }
    }

    private Long extrairUsuarioIdDoToken(JwtAuthenticationToken token){
        return Long.parseLong(token.getTokenAttributes().get("sub").toString());
    }
}
