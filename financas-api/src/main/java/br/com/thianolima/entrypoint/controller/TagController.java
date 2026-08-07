package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.core.usecase.AlterarTagUseCase;
import br.com.thianolima.core.usecase.InserirTagUseCase;
import br.com.thianolima.entrypoint.request.TagRequest;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final Tracer tracer;
    private final InserirTagUseCase inserirTagUseCase;
    private final AlterarTagUseCase alterarTagUseCase;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> listar(
            @RequestParam(value = "incluirSaldoLimite", defaultValue = "false") Boolean incluirSaldoLimite,
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("cartoes-por-usuario");
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

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> inserir(
            @Valid @RequestBody TagRequest request,
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("cartoes-inserir");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var novaTag = request.toModel();
            novaTag.setUsuarioId(usuarioId);
            inserirTagUseCase.executar(novaTag);
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
            @PathVariable(value = "id") Long tagId,
            @Valid @RequestBody TagRequest request,
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("cartoes-alterar");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var novaTag = request.toModel();
            novaTag.setId(tagId);
            novaTag.setUsuarioId(usuarioId);
            alterarTagUseCase.executar(novaTag);
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
            @PathVariable(value = "id") Long cartaoId,
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("cartoes-alterar");
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
