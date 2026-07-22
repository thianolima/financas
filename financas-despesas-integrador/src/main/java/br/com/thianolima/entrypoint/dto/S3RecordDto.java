package br.com.thianolima.entrypoint.dto;

import lombok.Data;

@Data
public class S3RecordDto {
    private String awsRegion;
    private String eventTime;
    private String eventName;
    private S3DetailDto s3;
}