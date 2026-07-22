package br.com.thianolima.core.provider;

import br.com.thianolima.model.Despesa;

import java.util.List;

public interface CarregarFaturaExcel {

    List<Despesa> executar(Long usuarioId, String s3Bucket, String s3Key);

}
