package br.com.thianolima.infrastructure.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SseService {
    private final Map<Long, SseEmitter> conexoesAtivas = new ConcurrentHashMap<>();
    private static final Long TIMEOUT_20_SEGUNDOS = 20000L;
    private final ScheduledExecutorService heartbeatExecutor =
            Executors.newScheduledThreadPool(0, Thread.ofVirtual().factory());

    public SseEmitter criarConexao(Long usuarioId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT_20_SEGUNDOS);

        emitter.onTimeout(() -> {
            log.debug("SSE - Ciclo normal encerrado (20s) para usuario {}.", usuarioId);
            emitter.complete();
            conexoesAtivas.remove(usuarioId);
        });

        emitter.onError((ex) -> {
            log.error("SSE ERRO- Ciclo normal encerrado (20s) para usuario {} erro: {}.", usuarioId, ex.getMessage());
            emitter.complete();
            conexoesAtivas.remove(usuarioId);
        });

        emitter.onCompletion(() -> {
            log.debug("SSE - Ciclo completo (20s) para usuario {}.", usuarioId);
            conexoesAtivas.remove(usuarioId);
        });

        conexoesAtivas.put(usuarioId, emitter);

        // Handshake inicial imediato para liberar os headers no API Gateway
        try {
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("Conectado"));
        } catch (Exception e) {
            log.error("SSE ERRO- Falha ao enviar evento INIT para usuario {} erro: {}.", usuarioId, e.getMessage());
            conexoesAtivas.remove(usuarioId);
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public Optional<SseEmitter> getConexaoUsuario(Long usuarioId) {
        return Optional.ofNullable(conexoesAtivas.get(usuarioId));
    }
}