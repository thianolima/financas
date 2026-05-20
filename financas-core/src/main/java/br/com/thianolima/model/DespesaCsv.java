package br.com.thianolima.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DespesaCsv {
    Integer sequencia;
    String dataDespesa;
    String descricao;
    String valor;
}
