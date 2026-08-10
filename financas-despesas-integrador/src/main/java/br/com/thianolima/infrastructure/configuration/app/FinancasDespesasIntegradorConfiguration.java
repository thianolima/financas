package br.com.thianolima.infrastructure.configuration.app;

import br.com.thianolima.core.provider.*;
import br.com.thianolima.core.usecase.ProcessarFaturaExcelUseCase;
import br.com.thianolima.core.usecase.ProcessarRetornoNovaFaturaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinancasDespesasIntegradorConfiguration {

    @Bean
    ProcessarFaturaExcelUseCase criarProcessarComandoNovaFaturaExcelUseCase(
        CarregarFaturaExcel carregarFaturaExcel,
        ProduzirComandoNovaDespesa produzirComandoNovaDespesa,
        SalvarFatura salvarFatura,
        BuscarCartaoPorId buscarCartaoPorId,
        BuscarFaturaPorCartaoIdEAnoMes buscarFaturaPorCartaoIdEAnoMes
    ){
        return new ProcessarFaturaExcelUseCase(
                carregarFaturaExcel,
                produzirComandoNovaDespesa,
                salvarFatura,
                buscarCartaoPorId,
                buscarFaturaPorCartaoIdEAnoMes
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
