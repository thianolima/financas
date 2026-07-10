package br.com.thianolima.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegraTermo {
    Long id;
    Long regraId;
    String termoBusca;
}
