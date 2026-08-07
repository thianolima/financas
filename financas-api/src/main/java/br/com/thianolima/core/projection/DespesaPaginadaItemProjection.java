package br.com.thianolima.core.projection;

import br.com.thianolima.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DespesaPaginadaItemProjection {
    Long id;
    Long cartaoId;
    String cartaoNome;
    Long categoriaId;
    String cartaoCor;
    String categoriaNome;
    String descricao;
    Integer parcelaAtual;
    Integer totalParcelas;
    LocalDate dataDespesa;
    LocalDate dataVencimento;
    BigDecimal valor;
    String observacao;
    Boolean recorrente;
    List<String> tags;

    public boolean isParcelado(){
        return parcelaAtual > 0 && totalParcelas > 0;
    }

    public boolean isRecorrente(){
        return recorrente;
    }

    public boolean isAvulso(){
        return !isRecorrente() && !isParcelado();
    }
}