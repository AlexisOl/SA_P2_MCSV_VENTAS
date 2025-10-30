package com.sa.ventas.ventas.infraestructura.eventos;

import com.example.comun.DTO.FacturaAnuncio.AnuncioCreadoDTO;
import com.example.comun.DTO.FacturaBoleto.*;
import com.sa.ventas.ventas.aplicacion.puertos.salida.CambiarEstadoVenta;
import com.sa.ventas.ventas.aplicacion.puertos.salida.eventos.CrearFacturaBoleto;
import com.sa.ventas.ventas.aplicacion.puertos.salida.eventos.CrearFacturaSnacks;
import com.sa.ventas.ventas.aplicacion.puertos.salida.eventos.VerificarFuncionOutputPort;
import com.sa.ventas.ventas.aplicacion.puertos.salida.eventos.VerificarUsuarioOutputPort;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.ventas.ventas.dominio.Venta;
import com.sa.ventas.ventas.dominio.objeto_valor.EstadoVenta;
import com.sa.ventas.ventas.infraestructura.eventos.dto.VerificarDTO;
import com.sa.ventas.ventas.infraestructura.eventos.dto.VerificarRespuestaDTO;
import com.sa.ventas.ventasnack.dominio.VentaSnack;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VentasAdaptadorKafka implements VerificarUsuarioOutputPort, VerificarFuncionOutputPort, CrearFacturaBoleto,
        CrearFacturaSnacks {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> verificacionFutures = new ConcurrentHashMap<>();
    private final CambiarEstadoVenta  cambiarEstadoVenta;


    public VentasAdaptadorKafka(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper,
                                CambiarEstadoVenta  cambiarEstadoVenta) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.cambiarEstadoVenta=cambiarEstadoVenta;
    }


    // LISTENERS
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


    // para los estados de venta
    @KafkaListener(topics = "venta-actualizada", groupId = "ventas-group")
    @Transactional
    public void manejarExitoVenta(
            @Payload String mensaje,
            @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId
    )   throws Exception {

        RespuestaFacturaBoletoCreadoDTO solicitud = objectMapper.readValue(mensaje, RespuestaFacturaBoletoCreadoDTO.class);

        this.cambiarEstadoVenta.cambiarEstadoVenta(solicitud.getVentaId(),
                EstadoVenta.COMPLETADA);
    }

    @KafkaListener(topics = "venta-fallido", groupId = "ventas-group")
    @Transactional
    public void manejarFalloVenta(
            @Payload String mensaje,
            @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId
    )  throws Exception {
        RespuestaFacturaBoletoCreadoDTO solicitud = objectMapper.readValue(mensaje, RespuestaFacturaBoletoCreadoDTO.class);

        this.cambiarEstadoVenta.cambiarEstadoVenta(solicitud.getVentaId(),
                EstadoVenta.ANULADA);
    }

    // -----------------------------------------
    // METODOS
    // -----------------------------------------
    // METODOS
    // -----------------------------------------
    // METODOS
    // -----------------------------------------
    // METODOS
    // -----------------------------------------
    // METODOS

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

    @Override
    public void crearFacturaBoleto(Venta venta, UUID idCine) {
        try {
            String correlationId = UUID.randomUUID().toString();
            // Creamos un DTO solo con lo necesario para facturación
            FacturaBoletoCreadoDTO evento = new FacturaBoletoCreadoDTO();
            evento.setVentaId(venta.getVentaId());
            evento.setUsuarioId(venta.getUsuarioId());
            evento.setFuncionId(venta.getFuncionId());
            evento.setMontoTotal(venta.getMontoTotal());
            evento.setCantidadBoletos(venta.getCantidadBoletos());
            evento.setFechaVenta(LocalDate.from(venta.getFechaVenta()));
            evento.setCorrelationId(correlationId);
            evento.setIdCine(idCine);

            String mensaje = objectMapper.writeValueAsString(evento);
            Message<String> mensajeKafka = MessageBuilder
                    .withPayload(mensaje)
                    .setHeader(KafkaHeaders.TOPIC, "crear-factura-boleto")
                    .setHeader(KafkaHeaders.CORRELATION_ID, evento.getCorrelationId())
                    .build();

            kafkaTemplate.send(mensajeKafka);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando evento de creación de venta", e);
        }
    }

    @Override
    public void crearFacturaSnacks(List<VentaSnack> ventaSnack, UUID idCine) {
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
                    .setHeader(KafkaHeaders.TOPIC, "crear-factura-snacks")
                    .setHeader(KafkaHeaders.CORRELATION_ID, evento.getCorrelationId())
                    .build();

            kafkaTemplate.send(mensajeKafka);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando el evento de venta de comida");
        }
    }
}
