package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.core.usecase.GerarProjecaoDespesasUseCase;
import br.com.thianolima.entrypoint.response.ProjecaoDespesaMensalResponse;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/projecoes")
public class ProjecaoController {

    private final Tracer tracer;
    private final GerarProjecaoDespesasUseCase gerarProjecaoParcelasMensalUseCase;

    public ProjecaoController(
            Tracer tracer,
            GerarProjecaoDespesasUseCase gerarProjecaoParcelasMensalUseCase)
    {
        this.tracer = tracer;
        this.gerarProjecaoParcelasMensalUseCase = gerarProjecaoParcelasMensalUseCase;
    }

    @GetMapping("/despesas")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<?> despesas(
            @RequestParam(value = "meses", defaultValue = "6") Integer mesesProjetados,
            JwtAuthenticationToken token
    ){
        ScopedSpan span = tracer.startScopedSpan("projecoes-despesas");
        try{
            var usuarioId = extrairUsuarioIdDoToken(token);
            var resultado = gerarProjecaoParcelasMensalUseCase.executar(usuarioId, mesesProjetados);
            var response = !resultado.isEmpty() ? new ProjecaoDespesaMensalResponse(resultado) : resultado;
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
