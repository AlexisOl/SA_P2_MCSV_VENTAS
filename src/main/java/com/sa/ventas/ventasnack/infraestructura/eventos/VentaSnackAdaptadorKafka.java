package com.sa.ventas.ventasnack.infraestructura.eventos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.ventas.ventasnack.aplicacion.puertos.salida.eventos.VerificarSnackOutputPort;
import com.sa.ventas.ventasnack.infraestructura.eventos.dto.SnackDTO;
import com.sa.ventas.ventasnack.infraestructura.eventos.dto.VerificarSnackDTO;
import com.sa.ventas.ventasnack.infraestructura.eventos.dto.VerificarSnackRespuestaDTO;
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
public class VentaSnackAdaptadorKafka implements VerificarSnackOutputPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, CompletableFuture<SnackDTO>> snackVerificationFutures = new ConcurrentHashMap<>();

    public VentaSnackAdaptadorKafka(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "respuesta-verificar-snack", groupId = "ventas-group")
    public void handleSnackVerificationResponse(@Payload String mensaje,
                                                @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId) throws Exception {
        if (correlationId == null) {
            System.err.println("Missing correlationId in Kafka message");
            return;
        }

        VerificarSnackRespuestaDTO respuesta = objectMapper.readValue(mensaje, VerificarSnackRespuestaDTO.class);
        CompletableFuture<SnackDTO> future = snackVerificationFutures.remove(correlationId);

        if (future != null) {
            if (respuesta.isExiste()) {
                SnackDTO snackDTO = new SnackDTO();
                snackDTO.setPrecio(respuesta.getPrecio());
                future.complete(snackDTO);
            } else {
                future.complete(null);
            }
        }
    }

    @Override
    public CompletableFuture<SnackDTO> obtenerSnack(UUID snackId) {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<SnackDTO> future = new CompletableFuture<>();
        snackVerificationFutures.put(correlationId, future);

        try {
            String mensaje = objectMapper.writeValueAsString(new VerificarSnackDTO(snackId, correlationId));
            Message<String> kafkaMessage = MessageBuilder
                    .withPayload(mensaje)
                    .setHeader(KafkaHeaders.TOPIC, "verificar-snack")
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();
            kafkaTemplate.send(kafkaMessage);
        } catch (Exception e) {
            snackVerificationFutures.remove(correlationId);
            future.completeExceptionally(e);
        }

        return future;
    }
}
