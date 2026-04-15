package br.com.thianolima.infrastructure.configuration.app;

import br.com.thianolima.core.provider.BuscarUsuarioPorEmail;
import br.com.thianolima.core.provider.GerarTokenAcesso;
import br.com.thianolima.core.provider.ValidarSenhaUsuario;
import br.com.thianolima.core.usecase.AutenticarUsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinancasSegurancaConfiguration {

    @Bean
    public AutenticarUsuarioUseCase criarAutenticarUsuarioUseCase(
            BuscarUsuarioPorEmail buscarUsuarioPorEmail,
            ValidarSenhaUsuario validarSenhaUsuario,
            GerarTokenAcesso gerarTokenAcesso

    ) {
        return new AutenticarUsuarioUseCase(
                buscarUsuarioPorEmail,
                validarSenhaUsuario,
                gerarTokenAcesso
        ) ;
    }
}
