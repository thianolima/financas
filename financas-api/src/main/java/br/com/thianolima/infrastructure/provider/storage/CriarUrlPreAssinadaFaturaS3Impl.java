package br.com.thianolima.infrastructure.provider.storage;

import br.com.thianolima.core.provider.storage.CriarUrlPreAssinadaFatura;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Slf4j
@Service
public class CriarUrlPreAssinadaFaturaS3Impl implements CriarUrlPreAssinadaFatura {

    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket.fatura}")
    private String bucket;

    public CriarUrlPreAssinadaFaturaS3Impl(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    @Override
    public String executar(Long idCartao, Long idUsuario, String anoMes, String nomeArquivo) {
        try {
            var objectKey = idUsuario + "/" + idCartao + "/" + anoMes + "/" + nomeArquivo;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
//                    .contentType("text/csv")
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(12))
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(presignRequest);

            return presignedPutObjectRequest.url().toString();
        } catch (S3Exception exception){
            log.error("Erro ao criar url pre assinada no S3", exception);
            throw exception;
        }
    }
}
