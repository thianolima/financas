package br.com.thianolima.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Regra {
    Long id;
    Long usuarioId;
    Long categoriaId;
    String descricao;
    List<RegraTermo> termos;
}
