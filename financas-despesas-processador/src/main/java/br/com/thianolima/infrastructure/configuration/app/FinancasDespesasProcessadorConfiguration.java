package br.com.thianolima.infrastructure.configuration.app;

import br.com.thianolima.core.provider.*;
import br.com.thianolima.core.usecase.ProcessarComandoNovaDespesaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinancasDespesasProcessadorConfiguration {

    @Bean
    ProcessarComandoNovaDespesaUseCase criarProcessarComandoNovaDespesaUseCase(
            BuscarFaturaPorId buscarFaturaPorId,
            SalvarDespesa salvarDespesa,
            BuscarFornecedoresPorUsuarioId buscarFornecedoresPorUsuarioId,
            BuscarParcelaAnterior buscarParcelaAnterior,
            BuscarDespesaRecorrente buscarDespesaRecorrente,
            ProduzirRetornoNovaFatura produzirRetornoNovaFatura
    ){
        return new ProcessarComandoNovaDespesaUseCase(
                buscarFaturaPorId,
                salvarDespesa,
                buscarFornecedoresPorUsuarioId,
                buscarParcelaAnterior,
                buscarDespesaRecorrente,
                produzirRetornoNovaFatura
        );
    }
}
