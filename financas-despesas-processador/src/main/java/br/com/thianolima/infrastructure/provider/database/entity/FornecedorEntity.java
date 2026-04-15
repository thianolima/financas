package br.com.thianolima.infrastructure.provider.database.entity;

import br.com.thianolima.model.Fornecedor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_fornecedores")
@Builder
public class FornecedorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fornecedor_id")
    Long id;
    Long usuarioId;
    Long categoriaId;
    String nome;
    String palavrasChave;

    public Fornecedor toModel(){
        return Fornecedor.builder()
                .id(this.id)
                .usuarioId(this.usuarioId)
                .categoriaId(this.categoriaId)
                .nome(this.nome)
                .palavrasChave(this.palavrasChave)
                .build();
    }
}
