package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.BuscarUsuarioPorEmail;
import br.com.thianolima.core.provider.GerarTokenAcesso;
import br.com.thianolima.core.provider.ValidarSenhaUsuario;

public class AutenticarUsuarioUseCase {

    private BuscarUsuarioPorEmail buscarUsuarioPorEmail;
    private ValidarSenhaUsuario validarSenhaUsuario;
    private GerarTokenAcesso gerarTokenAcesso;

    public AutenticarUsuarioUseCase(
            BuscarUsuarioPorEmail buscarUsuarioPorEmail,
            ValidarSenhaUsuario validarSenhaUsuario,
            GerarTokenAcesso gerarTokenAcesso

    ) {
        this.buscarUsuarioPorEmail = buscarUsuarioPorEmail;
        this.validarSenhaUsuario = validarSenhaUsuario;
        this.gerarTokenAcesso = gerarTokenAcesso;
    }

    public String executar(String email, String senha){
        var usuario = buscarUsuarioPorEmail.execute(email).orElseThrow(RuntimeException::new);

//        if(!validarSenhaUsuario.executar(senha, usuario.getSenha()))
//            throw new RuntimeException();

        return gerarTokenAcesso.executar(usuario);
    }
}
