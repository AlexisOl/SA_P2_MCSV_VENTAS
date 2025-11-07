package com.sa.ventas.boleto.infraestructura.eventos;

import com.example.comun.DTO.boletos.HorarioDTO;
import com.example.comun.DTO.boletos.PeliculaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.ventas.boleto.aplicacion.puertos.salida.eventos.ConsultarHorarioOutputPort;
import com.sa.ventas.boleto.aplicacion.puertos.salida.eventos.ConsultarPeliculaOutputPort;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class BoletoConsultasKafkaAdapter implements
        ConsultarPeliculaOutputPort, ConsultarHorarioOutputPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final ConcurrentHashMap<String, CompletableFuture<PeliculaDTO>> peliculaFutures =
            new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CompletableFuture<HorarioDTO>> horarioFutures =
            new ConcurrentHashMap<>();

    // ========== PELÍCULA ==========
    @Override
    public CompletableFuture<PeliculaDTO> obtenerPelicula(UUID peliculaId) {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<PeliculaDTO> future = new CompletableFuture<>();
        peliculaFutures.put(correlationId, future);

        try {
            var consulta = new ConsultaPeliculaDTO(peliculaId, correlationId);
            String mensaje = objectMapper.writeValueAsString(consulta);

            Message<String> message = MessageBuilder
                    .withPayload(mensaje)
                    .setHeader(KafkaHeaders.TOPIC, "consulta-pelicula")
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();

            kafkaTemplate.send(message);
            System.out.println("Consulta película enviada - CorrelationId: " + correlationId);
        } catch (Exception e) {
            peliculaFutures.remove(correlationId);
            future.completeExceptionally(e);
        }

        return future;
    }

    @KafkaListener(topics = "respuesta-consulta-pelicula", groupId = "ventas-group")
    public void handlePeliculaResponse(@Payload String mensaje,
                                       @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId) {
        try {
            if (correlationId == null) {
                System.err.println("Missing correlationId in respuesta-consulta-pelicula");
                return;
            }

            PeliculaDTO pelicula = objectMapper.readValue(mensaje, PeliculaDTO.class);
            CompletableFuture<PeliculaDTO> future = peliculaFutures.remove(correlationId);

            if (future != null) {
                future.complete(pelicula);
                System.out.println("Respuesta película recibida - CorrelationId: " + correlationId);
            }
        } catch (Exception e) {
            System.err.println("Error procesando respuesta película: " + e.getMessage());
        }
    }

    // ========== HORARIO ==========
    @Override
    public CompletableFuture<HorarioDTO> obtenerHorario(UUID funcionId) {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<HorarioDTO> future = new CompletableFuture<>();
        horarioFutures.put(correlationId, future);

        try {
            var consulta = new ConsultaHorarioDTO(funcionId, correlationId);
            String mensaje = objectMapper.writeValueAsString(consulta);

            Message<String> message = MessageBuilder
                    .withPayload(mensaje)
                    .setHeader(KafkaHeaders.TOPIC, "consulta-horario")
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();

            kafkaTemplate.send(message);
            System.out.println("Consulta horario enviada - CorrelationId: " + correlationId);
        } catch (Exception e) {
            horarioFutures.remove(correlationId);
            future.completeExceptionally(e);
        }

        return future;
    }

    @KafkaListener(topics = "respuesta-consulta-horario", groupId = "ventas-group")
    public void handleHorarioResponse(@Payload String mensaje,
                                      @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId) {
        try {
            if (correlationId == null) {
                System.err.println("Missing correlationId in respuesta-consulta-horario");
                return;
            }

            HorarioDTO horario = objectMapper.readValue(mensaje, HorarioDTO.class);
            CompletableFuture<HorarioDTO> future = horarioFutures.remove(correlationId);

            if (future != null) {
                future.complete(horario);
                System.out.println("Respuesta horario recibida - CorrelationId: " + correlationId);
            }
        } catch (Exception e) {
            System.err.println("Error procesando respuesta horario: " + e.getMessage());
        }
    }

    // DTOs internos para las consultas
    private record ConsultaPeliculaDTO(UUID peliculaId, String correlationId) {}
    private record ConsultaHorarioDTO(UUID funcionId, String correlationId) {}
}
