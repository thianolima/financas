package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.entrypoint.request.ReclassificarRequest;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DespesaController {

    private final Tracer tracer;

    public DespesaController(
            Tracer tracer
    ) {
        this.tracer = tracer;
    }


    @PostMapping("/desepesas/reclassificar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> reclassificar(
        @RequestBody @Valid ReclassificarRequest request,
        JwtAuthenticationToken token
    ){
        ScopedSpan span = tracer.startScopedSpan("despesas-reclassificar");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            return ResponseEntity.ok().build();
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
