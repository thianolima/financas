package br.com.thianolima.entrypoint.dto;

import lombok.Data;

@Data
public class S3DetailDto {
    private S3BucketDto bucket;
    private S3ObjectDto object;
}