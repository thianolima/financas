package br.com.thianolima.entrypoint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record S3EventDto(
        @JsonProperty("Records")
        List<S3RecordDto> records
){}