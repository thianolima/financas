package br.com.thianolima.infrastructure.provider.storage;

import br.com.thianolima.core.dto.DespesaCsvDto;
import br.com.thianolima.core.provider.CarregarFatura;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CarregarFaturaImpl implements CarregarFatura {

    private final S3Client s3Client;

    public CarregarFaturaImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<DespesaCsvDto> executar(
            String s3Bucket,
            String s3Key
    ) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Bucket)
                .key(s3Key)
                .build();

        var inputStream = s3Client.getObject(getObjectRequest);
        var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        var bufferedReader = new BufferedReader(inputStreamReader);

        String cabecalho = bufferedReader.readLine();
        validarCabecalhoDoArquivo(cabecalho);
        var sequencia = new AtomicInteger(1);

        return bufferedReader.lines()
                .filter(linha -> !linha.isBlank())
                .map(linha -> conveterCsvParaDespesaCsvDto(linha, sequencia.getAndIncrement()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void validarCabecalhoDoArquivo(String cabecalho){
        var colunas = Arrays.asList("data", "lançamento", "valor");
        if (!colunas.stream().allMatch(coluna -> cabecalho.contains(coluna))){
            throw new RuntimeException("Arquivo CSV com cabeçalho inválido");
        }
    }

    private DespesaCsvDto conveterCsvParaDespesaCsvDto(String linha, Integer sequencia){
        var splitLinha = linha.split(",");
        return DespesaCsvDto.builder()
                .sequencia(sequencia)
                .data(splitLinha[0])
                .descricao(splitLinha[1])
                .valor(splitLinha[2])
                .build();
    }
}
