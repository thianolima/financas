package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.core.usecase.BuscarNotificacoesUseCase;
import br.com.thianolima.entrypoint.controller.response.NotificacaoResponse;
import br.com.thianolima.infrastructure.sse.SseService;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final Tracer tracer;
    private final SseService sseService;
    private final BuscarNotificacoesUseCase buscarNotificacoesUseCase;

    public NotificacaoController(
            Tracer tracer,
            SseService sseService,
            BuscarNotificacoesUseCase buscarNotificacoesUseCase
    ) {
        this.tracer = tracer;
        this.sseService = sseService;
        this.buscarNotificacoesUseCase = buscarNotificacoesUseCase;
    }

    @GetMapping("/stream")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public SseEmitter criarConexao(
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("notificacoes-stream");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            return sseService.criarConexao(usuarioId);
        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            span.end();
        }
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public List<NotificacaoResponse> listar(
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("notificacoes-listar");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            return buscarNotificacoesUseCase.executar(usuarioId).stream()
                    .map(NotificacaoResponse::new)
                    .collect(Collectors.toList());
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
