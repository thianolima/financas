package br.com.thianolima.infrastructure.configuration.app;

import br.com.thianolima.core.provider.*;
import br.com.thianolima.core.usecase.ProcessarComandoNovaFaturaUseCase;
import br.com.thianolima.core.usecase.ProcessarRetornoNovaFaturaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinancasDespesasIntegradorConfiguration {

    @Bean
    ProcessarComandoNovaFaturaUseCase criarComandoNovaFaturaUseCase(
            CarregarFatura carregarFaturaCsv,
            BuscarFaturaPorCartaoIdEAnoMes buscarFaturaPorCartaoIdEAnoMes,
            SalvarFatura salvarFatura,
            ProduzirComandoNovaDespesa produzirComandoNovaDespesa,
            BuscarCartaoPorId buscarCartaoPorId
    ){
        return new ProcessarComandoNovaFaturaUseCase(
                carregarFaturaCsv,
                buscarFaturaPorCartaoIdEAnoMes,
                salvarFatura,
                produzirComandoNovaDespesa,
                buscarCartaoPorId
        );
    }

    @Bean
    ProcessarRetornoNovaFaturaUseCase criarProcessarRetornoNovaFaturaUseCase(
            BuscarFaturaPorId buscarFaturaPorId,
            SalvarFatura salvarFatura
    ) {
        return new ProcessarRetornoNovaFaturaUseCase(
                salvarFatura,
                buscarFaturaPorId
        );
    }
}
