package com.sa.ventas.ventasnack.infraestructura.eventos;

import com.example.comun.DTO.FacturaBoleto.DetalleSnackDTO;
import com.example.comun.DTO.FacturaBoleto.FacturaSnackCreadaDTO;
import com.example.comun.DTO.PeticionSnackEspecifica.SnackDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.ventas.ventasnack.aplicacion.puertos.salida.eventos.CrearFacturaSnacksDirecta;
import com.sa.ventas.ventasnack.aplicacion.puertos.salida.eventos.VerificarSnackOutputPort;
import com.sa.ventas.ventasnack.dominio.VentaSnack;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VentaSnackAdaptadorKafka implements VerificarSnackOutputPort, CrearFacturaSnacksDirecta {

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

        SnackDTO respuesta = objectMapper.readValue(mensaje, SnackDTO.class);
        CompletableFuture<SnackDTO> future = snackVerificationFutures.remove(correlationId);

        if (future != null) {
            if (respuesta.isExiste()) {
                SnackDTO snackDTO = new SnackDTO();
                snackDTO.setPrecio(respuesta.getPrecio());
                System.out.println("llego");
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

    @Override
    public void crearFacturaSnacksDirecto(List<VentaSnack> ventaSnack, UUID idCine) {
        try {
            String correlationId = UUID.randomUUID().toString();
            // Creamos un DTO solo con lo necesario para facturación
            FacturaSnackCreadaDTO evento = new FacturaSnackCreadaDTO();

            evento.setVentaId(ventaSnack.get(0).getVentaId());
            evento.setUsuarioId(ventaSnack.get(0).getUsuarioId());
            evento.setIdCine(idCine);
            evento.setCorrelationId(correlationId);
            evento.setVentaSnackId(ventaSnack.get(0).getVentaSnackId());

            List<DetalleSnackDTO> detalles = ventaSnack.stream()
                    .map(vs -> {
                        DetalleSnackDTO dto = new DetalleSnackDTO();
                        dto.setSnackId(vs.getSnackId());
                        dto.setCantidad(vs.getCantidad());
                        dto.setSubtotal(vs.getSubtotal() );
                        dto.setFechaVenta(LocalDate.from(vs.getFechaVenta()));
                        return dto;
                    })
                    .toList();
            evento.setDetalleSnacks(detalles);

            String mensaje = objectMapper.writeValueAsString(evento);
            Message<String> mensajeKafka = MessageBuilder
                    .withPayload(mensaje)
                    .setHeader(KafkaHeaders.TOPIC, "crear-factura-snacks-directa")
                    .setHeader(KafkaHeaders.CORRELATION_ID, evento.getCorrelationId())
                    .build();

            kafkaTemplate.send(mensajeKafka);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando el evento de venta de comida");
        }
    }
}
