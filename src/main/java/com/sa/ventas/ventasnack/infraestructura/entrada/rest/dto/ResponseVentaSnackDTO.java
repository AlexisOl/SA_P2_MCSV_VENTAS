package com.sa.ventas.ventasnack.infraestructura.entrada.rest.dto;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class ResponseVentaSnackDTO {

    private UUID ventaSnackId;
    private UUID ventaId;
    private UUID snackId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private UUID usuarioId;
    private LocalDateTime fechaVenta;
}
