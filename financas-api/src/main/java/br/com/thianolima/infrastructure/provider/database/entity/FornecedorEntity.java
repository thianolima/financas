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

    @Column(name = "usuario_id")
    Long usuarioId;

    @Column(name = "categoria_id")
    Long cartegoriaId;

    String nome;

    String palavrasChave;

}
