package br.com.thianolima.infrastructure.configuration.app;

import br.com.thianolima.core.provider.database.*;
import br.com.thianolima.core.provider.storage.CriarUrlPreAssinadaFatura;
import br.com.thianolima.core.usecase.*;
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

    @Bean
    public GerarProjecaoDespesasUseCase criarGerarProjecaoParcelasMensalUseCase(
            BuscarParcelasAtivasDeCartao buscarParcelasAtivasDeCartao,
            BuscarDespesasRecorrenteDeCartao buscarDespesasRecorrenteDeCartao,
            BuscarDespesasFuturas buscarDespesasFuturasPorUsuario,
            BuscarProjecaoDespesasPorCategoria buscarProjecaoDespesasPorCategoria
    ){
        return new GerarProjecaoDespesasUseCase(
                buscarParcelasAtivasDeCartao,
                buscarDespesasRecorrenteDeCartao,
                buscarDespesasFuturasPorUsuario,
                buscarProjecaoDespesasPorCategoria
        );
    }

    @Bean
    public BuscarCartoesPorUsuarioUseCase criarBuscarCartoesPorUsuarioUsecase(
            BuscarCartoesPorUsuario buscarCartoesPorUsuario
    ){
        return new BuscarCartoesPorUsuarioUseCase(buscarCartoesPorUsuario);
    }

    @Bean
    public BuscarDespesasPorUsuarioUseCase criarBuscarDespesasPorUsuarioUseCase(
            BuscarDespesasPorUsuario buscarDespesasPorUsuario
    ){
        return new BuscarDespesasPorUsuarioUseCase(buscarDespesasPorUsuario);
    }

    @Bean
    public BuscarCategoriasPorUsuarioUseCase criarBuscarCategoriasPorUsuarioUseCase(
            BuscarCategoriasPorUsuario buscarCategoriasPorUsuario
    ){
        return new BuscarCategoriasPorUsuarioUseCase(buscarCategoriasPorUsuario);
    }

    @Bean
    public AlterarDespesaUsecase criarAlterarDespesaUsecase(
            SalvarDespesa salvarDespesa,
            BuscarDespesaPorId buscarDespesaPorId,
            BuscarCartaoPorId buscarCartaoPorId,
            BuscarCategoriaPorId buscarCategoriaPorId
    ){
        return new AlterarDespesaUsecase(
                salvarDespesa,
                buscarDespesaPorId,
                buscarCartaoPorId,
                buscarCategoriaPorId
        );
    }
}
