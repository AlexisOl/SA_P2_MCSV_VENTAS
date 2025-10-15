package com.sa.ventas.ventas.infraestructura.eventos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.ventas.ventas.aplicacion.puertos.salida.eventos.NotificarVentaOutputPort;
import com.sa.ventas.ventas.dominio.Venta;
import com.sa.ventas.ventas.infraestructura.eventos.dto.NotificacionVentaDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificacionVentaAdaptador implements NotificarVentaOutputPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public NotificacionVentaAdaptador(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void notificarVentaCreada(Venta venta) {
        try {
            NotificacionVentaDTO notificacion = new NotificacionVentaDTO(
                    venta.getVentaId(),
                    venta.getUsuarioId(),
                    "Venta creada exitosamente",
                    "Su compra de " + venta.getCantidadBoletos() + " boleto(s) ha sido procesada."
            );
            String mensaje = objectMapper.writeValueAsString(notificacion);
            kafkaTemplate.send("venta-creada", mensaje);
        } catch (Exception e) {
            System.err.println("Error al enviar notificación de venta creada: " + e.getMessage());
        }
    }

    @Override
    public void notificarVentaAnulada(Venta venta) {
        try {
            NotificacionVentaDTO notificacion = new NotificacionVentaDTO(
                    venta.getVentaId(),
                    venta.getUsuarioId(),
                    "Venta anulada",
                    "Su venta ha sido anulada exitosamente."
            );
            String mensaje = objectMapper.writeValueAsString(notificacion);
            kafkaTemplate.send("venta-anulada", mensaje);
        } catch (Exception e) {
            System.err.println("Error al enviar notificación de venta anulada: " + e.getMessage());
        }
    }
}
