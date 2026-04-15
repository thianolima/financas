package br.com.thianolima.core.provider;

import br.com.thianolima.core.dto.DespesaCsvDto;

import java.io.IOException;
import java.util.List;

public interface CarregarFatura {

    List<DespesaCsvDto> executar(String s3Bucket, String s3Key) throws IOException;
}
