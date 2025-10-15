package com.sa.ventas.ventas.infraestructura.entrada.rest.dto;

import com.sa.ventas.ventas.dominio.objeto_valor.EstadoVenta;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class ResponseVentaDTO {
    private UUID ventaId;
    private UUID usuarioId;
    private UUID funcionId;
    private LocalDateTime fechaVenta;
    private Double montoTotal;
    private EstadoVenta estado;
    private Integer cantidadBoletos;
}
