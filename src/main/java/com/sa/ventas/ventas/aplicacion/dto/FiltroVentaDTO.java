package com.sa.ventas.ventas.aplicacion.dto;

import com.sa.ventas.ventas.dominio.objeto_valor.EstadoVenta;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FiltroVentaDTO {

    private UUID usuarioId;
    private UUID funcionId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private EstadoVenta estado;
}
