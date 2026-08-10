package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.core.provider.database.BuscarLimiteUtilizadoCartao;
import br.com.thianolima.core.usecase.*;
import br.com.thianolima.entrypoint.request.CartaoRequest;
import br.com.thianolima.entrypoint.response.CartaoLimiteResponse;
import br.com.thianolima.entrypoint.response.CartaoResponse;
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
@RequestMapping("/cartoes")
public class CartaoController {

    private final Tracer tracer;
    private final BuscarCartoesPorUsuarioUseCase buscarCartoesPorUsuarioUseCase;
    private final InserirCartaoUseCase inserirCartaoUseCase;
    private final AlterarCartaoUseCase alterarCartaoUseCase;
    private final ExcluirCartaoUseCase excluirCartaoUseCase;
    private final BuscarLimiteUtilizadoCartaoUseCase buscarLimiteUtilizadoCartaoUseCase;

    public CartaoController(
            Tracer tracer,
            BuscarCartoesPorUsuarioUseCase buscarCartoesPorUsuarioUseCase,
            InserirCartaoUseCase inserirCartaoUseCase,
            AlterarCartaoUseCase alterarCartaoUseCase,
            ExcluirCartaoUseCase excluirCartaoUseCase,
            BuscarLimiteUtilizadoCartaoUseCase buscarLimiteUtilizadoCartaoUseCase
    ) {
        this.tracer = tracer;
        this.buscarCartoesPorUsuarioUseCase = buscarCartoesPorUsuarioUseCase;
        this.inserirCartaoUseCase = inserirCartaoUseCase;
        this.alterarCartaoUseCase = alterarCartaoUseCase;
        this.excluirCartaoUseCase = excluirCartaoUseCase;
        this.buscarLimiteUtilizadoCartaoUseCase = buscarLimiteUtilizadoCartaoUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> listar(
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("cartoes-por-usuario");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var resultado = buscarCartoesPorUsuarioUseCase.executar(usuarioId);
            var response = !resultado.isEmpty() ? resultado.stream().map(CartaoResponse::new).toList() : List.of();
            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            span.end();
        }
    }

    @GetMapping("{id}/limite")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> limite(
            @PathVariable(value = "id") Long cartaoId,
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("cartoes-limite");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var resultado = buscarLimiteUtilizadoCartaoUseCase.executar(cartaoId, usuarioId);
            var response =
                    !resultado.isEmpty()
                    ? resultado.stream().map(limite ->
                            new CartaoLimiteResponse(
                                    limite.getCartaoId(),
                                    limite.getValorLimite(),
                                    limite.getValorLimiteUtilizado()
                            )
                    ).findFirst()
                    : List.of();
            return ResponseEntity.ok(response);
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
            @Valid @RequestBody CartaoRequest request,
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("cartoes-inserir");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var novoCartao = request.toModel();
            novoCartao.setUsuarioId(usuarioId);
            inserirCartaoUseCase.executar(novoCartao);
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
            @PathVariable(value = "id") Long cartaoId,
            @Valid @RequestBody CartaoRequest request,
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("cartoes-alterar");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var novoCartao = request.toModel();
            novoCartao.setId(cartaoId);
            novoCartao.setUsuarioId(usuarioId);
            alterarCartaoUseCase.executar(novoCartao);
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
            excluirCartaoUseCase.executar(cartaoId, usuarioId);
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
