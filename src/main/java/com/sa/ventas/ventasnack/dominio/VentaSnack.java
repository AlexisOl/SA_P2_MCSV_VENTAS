package com.sa.ventas.ventasnack.dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class VentaSnack {

    private UUID ventaSnackId;
    private UUID ventaId; // Puede ser null si es venta directa
    private UUID snackId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private UUID usuarioId; // AGREGAR para ventas directas
    private java.time.LocalDateTime fechaVenta; // AGREGAR

    public VentaSnack(UUID ventaSnackId, UUID ventaId, UUID snackId,
                      Integer cantidad, Double precioUnitario, UUID usuarioId) {
        this.ventaSnackId = ventaSnackId;
        this.ventaId = ventaId;
        this.snackId = snackId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
        this.usuarioId = usuarioId;
        this.fechaVenta = java.time.LocalDateTime.now();
        validarVentaSnack();
    }

    private void validarVentaSnack() {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (precioUnitario <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }
        // Si no hay ventaId, debe haber usuarioId
        if (ventaId == null && usuarioId == null) {
            throw new IllegalArgumentException("Debe especificar ventaId o usuarioId");
        }
    }
}
