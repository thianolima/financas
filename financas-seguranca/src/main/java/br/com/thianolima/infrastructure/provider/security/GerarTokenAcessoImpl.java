package br.com.thianolima.infrastructure.provider.security;

import br.com.thianolima.core.provider.GerarTokenAcesso;
import br.com.thianolima.model.Usuario;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class GerarTokenAcessoImpl implements GerarTokenAcesso {

    private final JwtEncoder jwtEncoder;
    private final Long horasParaExpirar = 8l;

    public GerarTokenAcessoImpl(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String executar(Usuario usuario) {
        var now = Instant.now();
        var expiresAt = now.plusSeconds(TimeUnit.HOURS.toSeconds(horasParaExpirar));
        var claims = JwtClaimsSet.builder()
                .issuer("financas-seguranca")
                .subject(usuario.getId().toString())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("role", usuario.getPerfil())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
