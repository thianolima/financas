package br.com.thianolima.core.usecase;

import br.com.thianolima.core.dto.FaturaItemDto;
import br.com.thianolima.core.provider.BuscarFaturaPorCartaoIdEAnoMes;
import br.com.thianolima.core.provider.CarregarFatura;
import br.com.thianolima.core.provider.ProduzirComandoNovaDespesa;
import br.com.thianolima.core.provider.SalvarFatura;
import br.com.thianolima.model.Fatura;
import br.com.thianolima.model.FaturaSituacaoEnum;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ProcessarComandoNovaFaturaUseCase {

    private final CarregarFatura carregarFatura;
    private final BuscarFaturaPorCartaoIdEAnoMes buscarFaturaPorCartaoIdEAnoMes;
    private final SalvarFatura salvarFatura;
    private final ProduzirComandoNovaDespesa produzirComandoNovaDespesa;

    public void executar(
            Long usuarioId,
            Long cartaoId,
            String anoMes,
            String s3Bucket,
            String s3Key
    ) throws IOException {
        validarDuplicidadeDaFatura(cartaoId, anoMes);
        var despesasCsv = carregarFatura.executar(s3Bucket, s3Key);
        var fatura = salvarFatura.executar(
                Fatura.builder()
                    .dataCriacao(LocalDateTime.now())
                    .situacao(FaturaSituacaoEnum.PROCESSANDO)
                    .quantidadeDespesas(despesasCsv.size())
                    .cartaoId(cartaoId)
                    .usuarioId(usuarioId)
                    .anoMes(anoMes)
                    .s3Bucket(s3Bucket)
                    .s3Key(s3Key)
                    .build()
        );
        despesasCsv.forEach(despesaCsv -> {
                if(!isDesconto(despesaCsv.getValor())) {
                    produzirComandoNovaDespesa.executar(
                            FaturaItemDto.builder()
                                    .sequencia(despesaCsv.getSequencia())
                                    .data(despesaCsv.getData())
                                    .descricao(despesaCsv.getDescricao())
                                    .valor(despesaCsv.getValor())
                                    .faturaId(fatura.getId())
                                    .cartaoId(fatura.getCartaoId())
                                    .usuarioId(fatura.getUsuarioId())
                                    .build()
                    );
                }
        });
    }

    private Boolean isDesconto(String valor){
        return Double.parseDouble(valor) <= 0.00;
    }

    private void validarDuplicidadeDaFatura(Long cartaoId, String anoMes){
        if(buscarFaturaPorCartaoIdEAnoMes.executar(cartaoId, anoMes).isPresent()){
            throw new RuntimeException("Fatura ja importada para esse anoMes");
        }
    }

}
