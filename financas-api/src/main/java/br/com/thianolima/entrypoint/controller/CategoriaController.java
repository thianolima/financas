package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.core.usecase.BuscarCategoriasPorUsuarioUseCase;
import br.com.thianolima.entrypoint.response.CartaoResponse;
import br.com.thianolima.entrypoint.response.CategoriaResponse;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final Tracer tracer;
    private final BuscarCategoriasPorUsuarioUseCase buscarCategoriasPorUsuarioUseCase;

    public CategoriaController(
            Tracer tracer,
            BuscarCategoriasPorUsuarioUseCase buscarCategoriasPorUsuarioUseCase
    ) {
        this.tracer = tracer;
        this.buscarCategoriasPorUsuarioUseCase = buscarCategoriasPorUsuarioUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> listar(
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("categorias-por-usuario");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var resultado = buscarCategoriasPorUsuarioUseCase.executar(usuarioId);
            var response = !resultado.isEmpty() ? resultado.stream().map(CategoriaResponse::new).toList() : List.of();
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
