package br.com.thianolima.infrastructure.provider.database.entity;

import br.com.thianolima.model.Categoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_categorias")
@Builder
public class CategoriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    Long id;

    @Column(name = "usuario_id")
    Long usuarioId;

    String nome;
    Boolean incluirProjecao;

    public CategoriaEntity(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
        this.usuarioId = categoria.getUsuarioId();
        this.incluirProjecao = categoria.getIncluirProjecao();
    }

    public Categoria toModel() {
        return Categoria.builder()
                .id(id)
                .nome(nome)
                .incluirProjecao(incluirProjecao)
                .usuarioId(usuarioId)
                .build();
    }
}
