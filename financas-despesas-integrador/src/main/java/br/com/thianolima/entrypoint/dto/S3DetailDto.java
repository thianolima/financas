package br.com.thianolima.entrypoint.dto;

public record S3DetailDto (
    S3BucketDto bucket,
    S3ObjectDto object
){}