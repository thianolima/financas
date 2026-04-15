package br.com.thianolima.infrastructure.provider.security;

import br.com.thianolima.core.provider.CodificarSenhaUsuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CodificarSenhaUsuarioImpl implements CodificarSenhaUsuario {

    private BCryptPasswordEncoder passwordEncoder;

    public CodificarSenhaUsuarioImpl(
            BCryptPasswordEncoder passwordEncoder
    ){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String executar(String senha) {
        return passwordEncoder.encode(senha);
    }
}
