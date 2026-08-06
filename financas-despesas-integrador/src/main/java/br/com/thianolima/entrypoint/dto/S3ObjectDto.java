package br.com.thianolima.entrypoint.dto;

public record S3ObjectDto(
    String key,
    Long size
){}