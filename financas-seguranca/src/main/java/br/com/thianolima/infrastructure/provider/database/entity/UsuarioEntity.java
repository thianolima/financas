package br.com.thianolima.infrastructure.provider.database.entity;

import br.com.thianolima.model.PerfilEnum;
import br.com.thianolima.model.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_usuarios")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    private String senha;

    @Enumerated(EnumType.STRING)
    private PerfilEnum perfil = PerfilEnum.BASICO;

    public Boolean isAdmin() {
        return this.perfil == PerfilEnum.ADMIN;
    }

    public Usuario toModel() {
        return Usuario.builder()
                .id(this.id)
                .nome(this.nome)
                .email(this.email)
                .senha(this.senha)
                .perfil(this.perfil)
                .build();
    }
}
