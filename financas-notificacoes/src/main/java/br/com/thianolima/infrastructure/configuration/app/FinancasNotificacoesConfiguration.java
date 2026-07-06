package br.com.thianolima.infrastructure.configuration.app;

import br.com.thianolima.core.provider.BuscarNotificacoes;
import br.com.thianolima.core.provider.SalvarNotificacao;
import br.com.thianolima.core.usecase.BuscarNotificacoesUseCase;
import br.com.thianolima.core.usecase.CriarNovaNotificacaoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinancasNotificacoesConfiguration {

    @Bean
    public CriarNovaNotificacaoUseCase criarNovaNotificacaoUseCase(
            SalvarNotificacao salvarNotificacao
    ){
        return new CriarNovaNotificacaoUseCase(
                salvarNotificacao
        );
    }

    @Bean
    public BuscarNotificacoesUseCase criarBuscarNotificacoesUseCase(
            BuscarNotificacoes buscarNotificacoes
    ){
        return new BuscarNotificacoesUseCase(
                buscarNotificacoes
        );
    }

}
