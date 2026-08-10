package br.com.thianolima.entrypoint.dto;

import lombok.Data;


public record S3RecordDto (
    String awsRegion,
    String eventTime,
    String eventName,
    S3DetailDto s3
){}