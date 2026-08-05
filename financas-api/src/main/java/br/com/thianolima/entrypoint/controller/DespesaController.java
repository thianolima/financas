package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.core.projection.TipoDespesaEnum;
import br.com.thianolima.core.usecase.AlterarDespesaUsecase;
import br.com.thianolima.core.usecase.BuscarDespesasPorUsuarioUseCase;
import br.com.thianolima.core.usecase.ExcluirDespesaUseCase;
import br.com.thianolima.core.usecase.PorcessarRegrasEmLoteUseCase;
import br.com.thianolima.entrypoint.request.DespesaRequest;
import br.com.thianolima.entrypoint.request.ProcessarRegrasRequest;
import br.com.thianolima.entrypoint.response.DespesaPaginadaResponse;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
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
    private final AlterarDespesaUsecase alterarDespesaUsecase;
    private final ExcluirDespesaUseCase excluirDespesaUseCase;
    private final PorcessarRegrasEmLoteUseCase processarRegrasEmLoteUseCase;


    public DespesaController(
            Tracer tracer,
            BuscarDespesasPorUsuarioUseCase buscarDespesasPorUsuarioUseCase,
            AlterarDespesaUsecase alterarDespesaUsecase,
            ExcluirDespesaUseCase excluirDespesaUseCase,
            PorcessarRegrasEmLoteUseCase processarRegrasEmLoteUseCase
    ) {
        this.tracer = tracer;
        this.buscarDespesasPorUsuarioUseCase = buscarDespesasPorUsuarioUseCase;
        this.alterarDespesaUsecase = alterarDespesaUsecase;
        this.excluirDespesaUseCase = excluirDespesaUseCase;
        this.processarRegrasEmLoteUseCase = processarRegrasEmLoteUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<DespesaPaginadaResponse> listar(
            JwtAuthenticationToken token,
            @RequestParam(value = "anomes") @DateTimeFormat(pattern = "yyyyMM") YearMonth anoMes,
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> alterar(
            @PathVariable(value = "id") Long despesaId,
            @RequestBody @Valid DespesaRequest request,
            JwtAuthenticationToken token
    ){
        ScopedSpan span = tracer.startScopedSpan("despesas-alterar");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var despesa = request.toModel();
            despesa.setUsuarioId(usuarioId);
            despesa.setId(despesaId);
            alterarDespesaUsecase.executar(despesa);
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
            @PathVariable(value = "id") Long despesaId,
            JwtAuthenticationToken token
    ){
        ScopedSpan span = tracer.startScopedSpan("despesas-excluir");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            excluirDespesaUseCase.executar(despesaId, usuarioId);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            log.error("Erro: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } finally {
            span.end();
        }
    }

    @PostMapping("/processar-regras")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> processarRegras(
        @RequestBody @Valid ProcessarRegrasRequest request,
        JwtAuthenticationToken token
    ){
        ScopedSpan span = tracer.startScopedSpan("despesas-processar-regras");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            processarRegrasEmLoteUseCase.executar(request.despesasIds(), usuarioId);
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
