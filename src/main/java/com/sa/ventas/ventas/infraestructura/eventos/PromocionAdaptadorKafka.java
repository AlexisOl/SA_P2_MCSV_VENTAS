package com.sa.ventas.ventas.infraestructura.eventos;

import com.example.comun.DTO.promocion.ConsultaPromocionDTO;
import com.example.comun.DTO.promocion.PromocionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.ventas.ventas.aplicacion.puertos.salida.eventos.ConsultarPromocionOutputPort;
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
public class PromocionAdaptadorKafka implements ConsultarPromocionOutputPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, CompletableFuture<PromocionDTO>> futures =
            new ConcurrentHashMap<>();

    public PromocionAdaptadorKafka(KafkaTemplate<String, String> kafkaTemplate,
                                   ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // Listener para recibir respuestas de promociones
    @KafkaListener(topics = "respuesta-consulta-promocion", groupId = "ventas-group")
    public void handlePromocionResponse(@Payload String mensaje,
                                        @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId) {
        try {
            if (correlationId == null) {
                System.err.println("Missing correlationId in respuesta-consulta-promocion");
                return;
            }

            PromocionDTO promocion = objectMapper.readValue(mensaje, PromocionDTO.class);
            CompletableFuture<PromocionDTO> future = futures.remove(correlationId);

            if (future != null) {
                future.complete(promocion);
                System.out.println("Respuesta de promoción recibida - CorrelationId: " + correlationId);
            }
        } catch (Exception e) {
            System.err.println("Error procesando respuesta de promoción: " + e.getMessage());
        }
    }

    @Override
    public CompletableFuture<PromocionDTO> obtenerMejorPromocion(
            UUID cineId, UUID salaId, UUID peliculaId,
            UUID clienteId, String tipo) {

        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<PromocionDTO> future = new CompletableFuture<>();
        futures.put(correlationId, future);

        try {
            ConsultaPromocionDTO consulta = new ConsultaPromocionDTO(
                    cineId, salaId, peliculaId, clienteId, null, correlationId
            );

            String mensaje = objectMapper.writeValueAsString(consulta);
            Message<String> message = MessageBuilder
                    .withPayload(mensaje)
                    .setHeader(KafkaHeaders.TOPIC, "consulta-promocion")
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();

            kafkaTemplate.send(message);
            System.out.println("Consulta de promoción enviada - CorrelationId: " + correlationId);

        } catch (Exception e) {
            futures.remove(correlationId);
            future.completeExceptionally(e);
        }

        return future;
    }
}
