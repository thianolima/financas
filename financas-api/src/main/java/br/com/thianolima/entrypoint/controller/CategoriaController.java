package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.core.provider.database.BuscarCategoriaPorId;
import br.com.thianolima.core.usecase.*;
import br.com.thianolima.entrypoint.request.CategoriaRequest;
import br.com.thianolima.entrypoint.response.CategoriaResponse;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final Tracer tracer;
    private final BuscarCategoriasPorUsuarioUseCase buscarCategoriasPorUsuarioUseCase;
    private final BuscarCategoriaPorId buscarCategoriaPorId;
    private final InserirCategoriaUseCase inserirCategoriaUseCase;
    private final AlterarCategoriaUseCase alterarCategoriaUseCase;
    private final ExcluirCategoriaUseCase excluirCategoriaUseCase;

    public CategoriaController(
            Tracer tracer,
            BuscarCategoriasPorUsuarioUseCase buscarCategoriasPorUsuarioUseCase,
            BuscarCategoriaPorId buscarCategoriaPorId,
            InserirCategoriaUseCase inserirCategoriaUseCase,
            AlterarCategoriaUseCase alterarCategoriaUseCase,
            ExcluirCategoriaUseCase excluirCategoriaUseCase
    ) {
        this.tracer = tracer;
        this.buscarCategoriasPorUsuarioUseCase = buscarCategoriasPorUsuarioUseCase;
        this.buscarCategoriaPorId = buscarCategoriaPorId;
        this.inserirCategoriaUseCase = inserirCategoriaUseCase;
        this.alterarCategoriaUseCase = alterarCategoriaUseCase;
        this.excluirCategoriaUseCase = excluirCategoriaUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> listar(
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("categorias-por-usuario");
        try {
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

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> buscarPorId(
            @PathVariable(value = "id") Long categoriaId,
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("categoria-por-id");
        try {
            var usuarioId = extrairUsuarioIdDoToken(token);
            var resultado = buscarCategoriaPorId.executar(categoriaId, usuarioId)
                    .orElseThrow(() -> new RuntimeException("Categoria nao encontrada"));
            return ResponseEntity.ok(new CategoriaResponse(resultado));
        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            span.end();
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> inserir(
            @Valid @RequestBody CategoriaRequest request,
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("categorias-inserir");
        try {
            var usuarioId = extrairUsuarioIdDoToken(token);
            var categoria = request.toModel();
            categoria.setUsuarioId(usuarioId);
            inserirCategoriaUseCase.executar(categoria);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            span.end();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> alterar(
            @PathVariable(value = "id") Long categoriaId,
            @Valid @RequestBody CategoriaRequest request,
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("categorias-alterar");
        try {
            var usuarioId = extrairUsuarioIdDoToken(token);
            var categoria = request.toModel();
            categoria.setId(categoriaId);
            categoria.setUsuarioId(usuarioId);
            alterarCategoriaUseCase.executar(categoria);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            span.end();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> excluir(
            @PathVariable(value = "id") Long categoriaId,
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("categorias-excluir");
        try {
            var usuarioId = extrairUsuarioIdDoToken(token);
            excluirCategoriaUseCase.executar(categoriaId, usuarioId);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            span.end();
        }
    }

    private Long extrairUsuarioIdDoToken(JwtAuthenticationToken token) {
        return Long.parseLong(token.getTokenAttributes().get("sub").toString());
    }
}
