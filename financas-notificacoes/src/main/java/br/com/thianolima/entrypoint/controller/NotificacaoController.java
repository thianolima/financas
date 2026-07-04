package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.infrastructure.service.ConexaoSseService;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final Tracer tracer;
    private final ConexaoSseService conexaoSseService;

    public NotificacaoController(
            Tracer tracer,
            ConexaoSseService conexaoSseService
    ) {
        this.tracer = tracer;
        this.conexaoSseService = conexaoSseService;
    }

    @GetMapping("/stream")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public SseEmitter criarConexao(
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("notificacoes-stream");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            return conexaoSseService.criarConexao(usuarioId);
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
