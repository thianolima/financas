package br.com.thianolima.infrastructure.sse;

import br.com.thianolima.core.projection.Notificacao;
import br.com.thianolima.core.provider.EnviarNotificacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
public class EnviarNotificacaoImpl implements EnviarNotificacao {

    private final SseService sseService;

    public EnviarNotificacaoImpl(SseService sseService) {
        this.sseService = sseService;
    }

    @Override
    public void executar(Notificacao notificacao) {
        sseService.getConexaoUsuario(notificacao.usuarioId()).ifPresentOrElse(emitter -> {
            try {
                emitter.send(
                        SseEmitter.event()
                            .name("NOVA_NOTIFICACAO")
                            .data(notificacao)
                );
                log.info("Sucesso - NOVA NOTIFICACAO para o usuário {}", notificacao.usuarioId());
            } catch (Exception e) {
                log.error("Erro - NOVA NOTIFICACAO para o usuário {}, {}", notificacao.usuarioId(), e.getMessage());
            }
        },
            () -> log.error("Nao encontrado comunicacao SSE ativa para o usuario: {}", notificacao.usuarioId())
        );
    }
}
