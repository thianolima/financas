package br.com.thianolima.infrastructure.provider.security;

import br.com.thianolima.core.provider.ValidarSenhaUsuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ValidarSenhaUsuarioImpl implements ValidarSenhaUsuario {

    private BCryptPasswordEncoder passwordEncoder;

    public ValidarSenhaUsuarioImpl(
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Boolean executar(String senha, String senhaSalva) {
        return passwordEncoder.matches(senha, senhaSalva);
    }
}
