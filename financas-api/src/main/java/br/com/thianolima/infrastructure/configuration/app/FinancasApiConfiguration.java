package br.com.thianolima.infrastructure.configuration.app;

import br.com.thianolima.core.provider.database.BuscarCartaoPorId;
import br.com.thianolima.core.provider.storage.CriarUrlPreAssinadaFatura;
import br.com.thianolima.core.usecase.UploadFaturaCartaoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinancasApiConfiguration {

    @Bean
    public UploadFaturaCartaoUseCase criarUploarFaturaCartaoUseCase(
            CriarUrlPreAssinadaFatura criarUrlPreAssinadaFatura,
            BuscarCartaoPorId buscarCartaoPorId
    ){
        return new UploadFaturaCartaoUseCase(
                criarUrlPreAssinadaFatura,
                buscarCartaoPorId
        );
    }
}
