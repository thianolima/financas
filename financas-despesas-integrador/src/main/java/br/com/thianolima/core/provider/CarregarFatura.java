package br.com.thianolima.core.provider;

import br.com.thianolima.model.DespesaCsv;

import java.io.IOException;
import java.util.List;

public interface CarregarFatura {

    List<DespesaCsv> executar(String s3Bucket, String s3Key) throws IOException;
}
