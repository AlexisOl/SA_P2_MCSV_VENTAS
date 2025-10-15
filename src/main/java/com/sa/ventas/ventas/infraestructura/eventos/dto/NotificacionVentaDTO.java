package com.sa.ventas.ventas.infraestructura.eventos.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionVentaDTO {

    private UUID ventaId;
    private UUID usuarioId;
    private String asunto;
    private String mensaje;
}
