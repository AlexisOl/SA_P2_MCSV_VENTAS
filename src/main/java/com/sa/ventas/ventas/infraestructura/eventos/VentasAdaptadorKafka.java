package com.sa.ventas.ventas.infraestructura.eventos;

import com.sa.ventas.ventas.aplicacion.puertos.salida.eventos.VerificarFuncionOutputPort;
import com.sa.ventas.ventas.aplicacion.puertos.salida.eventos.VerificarUsuarioOutputPort;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.ventas.ventas.infraestructura.eventos.dto.VerificarDTO;
import com.sa.ventas.ventas.infraestructura.eventos.dto.VerificarRespuestaDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VentasAdaptadorKafka implements VerificarUsuarioOutputPort, VerificarFuncionOutputPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> verificacionFutures = new ConcurrentHashMap<>();

    public VentasAdaptadorKafka(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "respuesta-verificar-usuario", groupId = "ventas-group")
    public void handleUsuarioVerificationResponse(@Payload String mensaje,
                                                  @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId) throws Exception {
        if (correlationId == null) {
            System.err.println("Missing correlationId in Kafka message");
            return;
        }

        VerificarRespuestaDTO respuesta = objectMapper.readValue(mensaje, VerificarRespuestaDTO.class);
        CompletableFuture<Boolean> future = verificacionFutures.remove(correlationId);

        if (future != null) {
            future.complete(respuesta.isExiste());
        }
    }

    @KafkaListener(topics = "respuesta-verificar-funcion", groupId = "ventas-group")
    public void handleFuncionVerificationResponse(@Payload String mensaje,
                                                  @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId) throws Exception {
        if (correlationId == null) {
            System.err.println("Missing correlationId in Kafka message");
            return;
        }

        VerificarRespuestaDTO respuesta = objectMapper.readValue(mensaje, VerificarRespuestaDTO.class);
        CompletableFuture<Boolean> future = verificacionFutures.remove(correlationId);

        if (future != null) {
            future.complete(respuesta.isExiste());
        }
    }

    @Override
    public CompletableFuture<Boolean> verificarUsuario(UUID idUsuario) {
        return enviarVerificacion(idUsuario, "verificar-usuario");
    }

    @Override
    public CompletableFuture<Boolean> verificarFuncion(UUID idFuncion) {
        return enviarVerificacion(idFuncion, "verificar-funcion");
    }

    private CompletableFuture<Boolean> enviarVerificacion(UUID id, String topic) {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        verificacionFutures.put(correlationId, future);

        try {
            String mensaje = objectMapper.writeValueAsString(new VerificarDTO(id, correlationId));
            Message<String> kafkaMessage = MessageBuilder
                    .withPayload(mensaje)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();
            kafkaTemplate.send(kafkaMessage);
        } catch (Exception e) {
            verificacionFutures.remove(correlationId);
            future.completeExceptionally(e);
        }

        return future;
    }
}
