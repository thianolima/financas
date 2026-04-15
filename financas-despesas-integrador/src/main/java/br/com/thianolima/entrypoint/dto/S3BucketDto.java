package br.com.thianolima.entrypoint.dto;

import lombok.Data;

@Data
public class S3BucketDto {
    private String name;
    private String arn;
}