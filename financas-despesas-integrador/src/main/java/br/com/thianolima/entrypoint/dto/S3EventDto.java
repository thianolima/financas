package br.com.thianolima.entrypoint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class S3EventDto {

    @JsonProperty("Records")
    private List<S3RecordDto> records;








}