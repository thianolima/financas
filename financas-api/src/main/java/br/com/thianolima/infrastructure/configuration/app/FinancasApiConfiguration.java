package br.com.thianolima.infrastructure.configuration.app;

import br.com.thianolima.core.provider.database.*;
import br.com.thianolima.core.provider.message.ProduzirComandoProcessarRegras;
import br.com.thianolima.core.provider.storage.CriarUrlPreAssinadaS3;
import br.com.thianolima.core.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinancasApiConfiguration {

    @Bean
    public UploadFaturaCartaoUseCase criarUploarFaturaCartaoUseCase(
            CriarUrlPreAssinadaS3 criarUrlPreAssinadaS3,
            BuscarCartaoPorId buscarCartaoPorId
    ){
        return new UploadFaturaCartaoUseCase(
                criarUrlPreAssinadaS3,
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

    @Bean
    public ExcluirDespesaUseCase criarExcluirDespesaUseCase(
            ExcluirDespesa excluirDespesa,
            BuscarDespesaPorId buscarDespesaPorId
    ){
        return new ExcluirDespesaUseCase(
                excluirDespesa,
                buscarDespesaPorId
        );
    }

    @Bean
    public InserirRegraUseCase criarRegraUseCase(
            SalvarRegra salvarRegra,
            BuscarRegraPorTermoBusca buscarRegraPorTermoBusc
    ){
        return new InserirRegraUseCase(
                salvarRegra,
                buscarRegraPorTermoBusc
        );
    }

    @Bean
    public PorcessarRegrasEmLoteUseCase criarPorcessarRegrasEmLoteUseCase(
            ValidarDespesasPertecemUsuario validarDespesasPertecemUsuario,
            ProduzirComandoProcessarRegras produzirComandoProcessarRegras
    ){
        return  new PorcessarRegrasEmLoteUseCase(
                validarDespesasPertecemUsuario,
                produzirComandoProcessarRegras
        );
    }

    @Bean
    public InserirCartaoUseCase criarInserirCartaoUseCase(
            SalvarCartao salvarCartao,
            BuscarCartoesPorUsuario buscarCartoesPorUsuario
    ){
        return new InserirCartaoUseCase(
                salvarCartao,
                buscarCartoesPorUsuario
        );
    }

    @Bean
    public AlterarCartaoUseCase criarAlterarCartaoUseCase(
            SalvarCartao salvarCartao,
            BuscarCartoesPorUsuario buscarCartoesPorUsuario,
            BuscarCartaoPorId buscarCartaoPorId
    ){
        return new AlterarCartaoUseCase(
                salvarCartao,
                buscarCartoesPorUsuario,
                buscarCartaoPorId
        );
    }

    @Bean
    public ExcluirCartaoUseCase criarExcluirCartao(
            BuscarCartaoPorId buscarCartaoPorId,
            ExcluirCartao excluirCartao,
            BuscarDespesasPorCartao buscarDespesasPorCartao
    ){
        return new ExcluirCartaoUseCase(
                buscarCartaoPorId,
                excluirCartao,
                buscarDespesasPorCartao
        );
    }

    @Bean
    public InserirTagUseCase criarInserirTagUseCase(
            SalvarTag salvarTag,
            BuscarTagPorNome buscarTagPorNome
    ){
        return new InserirTagUseCase(
                salvarTag,
                buscarTagPorNome
        );
    }

    @Bean
    public AlterarTagUseCase criarAlterarTagUseCase(
            SalvarTag salvarTag,
            BuscarTagPorNome buscarTagPorNome
    ){
        return new AlterarTagUseCase(
                salvarTag,
                buscarTagPorNome
        );
    }
}
