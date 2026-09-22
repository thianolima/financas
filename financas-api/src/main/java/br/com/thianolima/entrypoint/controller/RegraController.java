package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.core.provider.database.BuscarRegrasPorUsuario;
import br.com.thianolima.core.usecase.AlterarRegraUseCase;
import br.com.thianolima.core.usecase.BuscarRegrasPorUsuarioUseCase;
import br.com.thianolima.core.usecase.InserirRegraUseCase;
import br.com.thianolima.entrypoint.request.RegraRapidaRequest;
import br.com.thianolima.entrypoint.request.RegraRequest;
import br.com.thianolima.entrypoint.response.CartaoResponse;
import br.com.thianolima.entrypoint.response.RegraResponse;
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
@RequestMapping("/regras")
public class  RegraController {

    private final Tracer tracer;
    private final InserirRegraUseCase inserirRegraUseCase;
    private final AlterarRegraUseCase alterarRegraUseCase;
    private final BuscarRegrasPorUsuarioUseCase buscarRegrasPorUsuario;

    public RegraController(
            Tracer tracer,
            InserirRegraUseCase inserirRegraUseCase,
            AlterarRegraUseCase alterarRegraUseCase,
            BuscarRegrasPorUsuarioUseCase buscarRegrasPorUsuario
    ) {
        this.tracer = tracer;
        this.inserirRegraUseCase = inserirRegraUseCase;
        this.alterarRegraUseCase = alterarRegraUseCase;
        this.buscarRegrasPorUsuario = buscarRegrasPorUsuario;
    }

//    @PostMapping
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
//    public ResponseEntity<?> inserirRegraRapida(
//            @RequestBody @Valid RegraRapidaRequest request,
//            JwtAuthenticationToken token
//    ){
//        ScopedSpan span = tracer.startScopedSpan("regra-inserir-rapida");
//        try{
//            var usuarioId = extrairUsuarioIdDoToken(token);
//            var regra = request.toModel();
//            regra.setUsuarioId(usuarioId);
//            inserirRegraUseCase.executar(regra);
//            return ResponseEntity.ok().build();
//        } catch (Exception exception) {
//            log.error("Erro: {}", exception.getMessage());
//            throw new RuntimeException(exception);
//        } finally {
//            span.end();
//        }
//    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> inserir(
            @RequestBody @Valid RegraRequest request,
            JwtAuthenticationToken token
    ){
        ScopedSpan span = tracer.startScopedSpan("regra-inserir");
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> alterar(
            @PathVariable(value = "id") Long regraId,
            @RequestBody @Valid RegraRequest request,
            JwtAuthenticationToken token
    ){
        ScopedSpan span = tracer.startScopedSpan("regra-alterar");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var regra = request.toModel();
            regra.setUsuarioId(usuarioId);
            regra.setId(regraId);
            alterarRegraUseCase.executar(regra);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            span.end();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> listar(
            JwtAuthenticationToken token
    ) {
        ScopedSpan span = tracer.startScopedSpan("regra-por-usuario");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var resultado = buscarRegrasPorUsuario.executar(usuarioId);
            var response = !resultado.isEmpty() ? resultado.stream().map(RegraResponse::new).toList() : List.of();
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
