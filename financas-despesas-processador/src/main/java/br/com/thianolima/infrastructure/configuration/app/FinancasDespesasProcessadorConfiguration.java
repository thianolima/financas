package br.com.thianolima.infrastructure.configuration.app;

import br.com.thianolima.core.provider.*;
import br.com.thianolima.core.usecase.ClassificarDespesaUseCase;
import br.com.thianolima.core.usecase.ProcessarDespesaFaturaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinancasDespesasProcessadorConfiguration {

    @Bean
    ClassificarDespesaUseCase criarClassificarDespesaUseCase(
            BuscarFornecedoresPorUsuarioId buscarFornecedoresPorUsuarioId,
            BuscarParcelaAnterior buscarParcelaAnterior,
            BuscarDespesaRecorrente buscarDespesaRecorrente
    ){
        return new ClassificarDespesaUseCase(
                buscarFornecedoresPorUsuarioId,
                buscarParcelaAnterior,
                buscarDespesaRecorrente
        );
    }

    @Bean
    ProcessarDespesaFaturaUseCase criarProcessarDespesaFaturaUseCase(
            ClassificarDespesaUseCase classificarDespesaUseCase,
            ProduzirRetornoNovaFatura produzirRetornoNovaFatura,
            BuscarFaturaPorId buscarFaturaPorId,
            SalvarDespesa salvarDespesa
    ){
        return new ProcessarDespesaFaturaUseCase(
                classificarDespesaUseCase,
                produzirRetornoNovaFatura,
                buscarFaturaPorId,
                salvarDespesa
        );
    }
}
