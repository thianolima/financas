package br.com.thianolima.entrypoint.dto;

import lombok.Data;

@Data
public class S3ObjectDto {
    private String key;
    private Long size;
}