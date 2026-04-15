package br.com.thianolima.entrypoint.controller;

import br.com.thianolima.core.usecase.UploadFaturaCartaoUseCase;
import br.com.thianolima.entrypoint.request.FaturaRequest;
import br.com.thianolima.entrypoint.response.FaturaPreAssinadaResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaturaController {

    private UploadFaturaCartaoUseCase uploadFaturaCartaoUseCase;

    FaturaController(
            UploadFaturaCartaoUseCase uploadFaturaCartaoUseCase
    ){
        this.uploadFaturaCartaoUseCase = uploadFaturaCartaoUseCase;
    }

    @PostMapping("/cartao/{id}/fatura/upload")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BASICO')")
    public ResponseEntity<FaturaPreAssinadaResponse> upload(
            @PathVariable(value = "id") Long cartaoId,
            @RequestBody @Valid FaturaRequest request,
            JwtAuthenticationToken token
    ) {
        var urlPreAssinada = uploadFaturaCartaoUseCase.executar(
                cartaoId,
                extrairUsuarioIdDoToken(token),
                request.getAnoMes(),
                request.getNomeArquivo()
        );

        return ResponseEntity.ok(
                FaturaPreAssinadaResponse.builder()
                        .url(urlPreAssinada)
                        .build()
        );
    }

    private Long extrairUsuarioIdDoToken(JwtAuthenticationToken token){
       return Long.parseLong(token.getTokenAttributes().get("sub").toString());
    }
}
