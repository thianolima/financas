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
    private static final Long TIMEOUT_UMA_HORA = 3600000L;
    private final ScheduledExecutorService heartbeatExecutor =
            Executors.newScheduledThreadPool(0, Thread.ofVirtual().factory());

    public SseEmitter criarConexao(Long usuarioId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT_UMA_HORA);

        emitter.onTimeout(() -> {
            log.info("SSE - Conexão expirou pelo timeout para usuario {}.", usuarioId);
            emitter.complete();
            conexoesAtivas.remove(usuarioId);
        });

        emitter.onError((ex) -> {
            log.info("SSE - Conexão interrompida pelo cliente (Aba fechada ou F5): {} Usuario: {}", ex.getMessage(), usuarioId);
            emitter.complete();
            conexoesAtivas.remove(usuarioId);
        });

        var task = heartbeatExecutor.scheduleAtFixedRate(() -> {
            try {
                // Envia o comentário vazio de "ping" para enganar o timeout de 60s do ALB da AWS
                emitter.send(SseEmitter.event()
                        .name("PING")
                        .data("")
                );
            } catch (Exception e) {
                // Se cair aqui, significa que o SseEmitter foi concluído/fechado pelo timeout ou erro.
                // O próprio Spring limpa o emitter, então apenas deixamos o log de debug.
                log.error("SSE - Canal fechado. Ping interrompido.");
            }
        }, 20, 20, TimeUnit.SECONDS);

        emitter.onCompletion(() -> {
            log.info("SSE - Ciclo de vida finalizado para usuario {}.", usuarioId);
            conexoesAtivas.remove(usuarioId);
            task.cancel(true);
        });

        conexoesAtivas.put(usuarioId, emitter);

        try {
            emitter.send(SseEmitter.event()
                    .name("CONECTADO")
                    .data("Conexão SSE estabelecida com sucesso para o usuário: " + usuarioId));
        } catch (Exception e) {
            conexoesAtivas.remove(usuarioId);
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public Optional<SseEmitter> getConexaoUsuario(Long usuarioId) {
        return Optional.of(conexoesAtivas.get(usuarioId));
    }
}