package com.sa.ventas.ventasnack.aplicacion.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroVentaSnackDTO {

    private UUID ventaSnackId;
    private UUID ventaId;
    private UUID snackId;
    private UUID usuarioId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
