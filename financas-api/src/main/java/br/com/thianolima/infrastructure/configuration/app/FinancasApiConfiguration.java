package br.com.thianolima.infrastructure.configuration.app;

import br.com.thianolima.core.provider.database.*;
import br.com.thianolima.core.provider.storage.CriarUrlPreAssinadaFatura;
import br.com.thianolima.core.usecase.BuscarCartoesPorUsuarioUseCase;
import br.com.thianolima.core.usecase.GerarProjecaoDespesasUseCase;
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
}
