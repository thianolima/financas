package br.com.thianolima.infrastructure.provider.database.entity;

import br.com.thianolima.model.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_tags")
@Builder
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    Long id;

    String nome;

    @Column(name = "usuario_id")
    Long usuarioId;

    public TagEntity(Tag tag) {
        this.id = tag.getId();
        this.nome = tag.getNome();
        this.usuarioId = tag.getUsuarioId();
    }

    public Tag toModel(){
        return new Tag(
                this.id,
                this.nome,
                this.usuarioId
        );
    }
}
