package br.com.thianolima.infrastructure.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConexaoSseService {
    private final Map<Long, SseEmitter> conexoesAtivas = new ConcurrentHashMap<>();
    private static final Long TIMEOUT_UMA_HORA = 3600000L;

    public SseEmitter criarConexao(Long usuarioId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT_UMA_HORA);
        emitter.onCompletion(() -> conexoesAtivas.remove(usuarioId));
        emitter.onTimeout(() -> conexoesAtivas.remove(usuarioId));
        emitter.onError((ex) -> conexoesAtivas.remove(usuarioId));

        conexoesAtivas.put(usuarioId, emitter);

        try {
            emitter.send(SseEmitter.event()
                    .name("CONECTADO")
                    .data("Conexão SSE estabelecida com sucesso para o usuário: " + usuarioId));
        } catch (Exception e) {
            conexoesAtivas.remove(usuarioId);
        }
        return emitter;
    }

    public SseEmitter getConexao(String usuarioId) {
        return conexoesAtivas.get(usuarioId);
    }
}