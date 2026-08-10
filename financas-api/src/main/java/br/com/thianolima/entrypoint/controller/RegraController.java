package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.core.usecase.InserirRegraUseCase;
import br.com.thianolima.entrypoint.request.RegraRequest;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/regras")
public class RegraController {

    private final Tracer tracer;
    private final InserirRegraUseCase inserirRegraUseCase;

    public RegraController(
            Tracer tracer,
            InserirRegraUseCase inserirRegraUseCase
    ) {
        this.tracer = tracer;
        this.inserirRegraUseCase = inserirRegraUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> inserirRegraRapida(
            @RequestBody @Valid RegraRequest request,
            JwtAuthenticationToken token
    ){
        ScopedSpan span = tracer.startScopedSpan("despesas-reclassificar");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var regra = request.toModel();
            regra.setUsuarioId(usuarioId);
            inserirRegraUseCase.executar(regra);
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
