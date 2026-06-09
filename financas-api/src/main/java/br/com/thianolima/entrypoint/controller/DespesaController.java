package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.core.model.TipoDespesaEnum;
import br.com.thianolima.core.usecase.BuscarDespesasPorUsuarioUseCase;
import br.com.thianolima.entrypoint.request.ReclassificarRequest;
import br.com.thianolima.entrypoint.response.DespesaPaginadaResponse;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@Slf4j
@RestController()
@RequestMapping("/despesas")
public class DespesaController {

    private final Tracer tracer;
    private final BuscarDespesasPorUsuarioUseCase buscarDespesasPorUsuarioUseCase;

    public DespesaController(
            Tracer tracer,
            BuscarDespesasPorUsuarioUseCase buscarDespesasPorUsuarioUseCase
    ) {
        this.tracer = tracer;
        this.buscarDespesasPorUsuarioUseCase = buscarDespesasPorUsuarioUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<DespesaPaginadaResponse> listar(
            JwtAuthenticationToken token,
            @RequestParam(value = "anomes", required = true) @DateTimeFormat(pattern = "yyyyMM") YearMonth anoMes,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "20") int tamanho,
            @RequestParam(value = "cartao", required = false) Long cartaoId,
            @RequestParam(value = "categoria", required = false) Long categoriaId,
            @RequestParam(value = "tipo", required = false) TipoDespesaEnum tipo
    ){
        ScopedSpan span = tracer.startScopedSpan("despesas-listar");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var resultado = buscarDespesasPorUsuarioUseCase.executar(
                    usuarioId,
                    anoMes,
                    pagina,
                    tamanho,
                    cartaoId,
                    categoriaId,
                    tipo
            );
            return ResponseEntity.ok(new DespesaPaginadaResponse(resultado));
        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            span.end();
        }
    }

    @PostMapping("/despesas/reclassificar")
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
