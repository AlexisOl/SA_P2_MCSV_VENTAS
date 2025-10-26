package com.sa.ventas.ventasnack.infraestructura.salida.entidades;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "venta_snack")
public class VentaSnackEntity {

    @Id
    @Column(name = "venta_snack_id", updatable = false, nullable = false)
    private UUID ventaSnackId;

    @Column(name = "venta_id", nullable = true) // CAMBIAR a nullable
    private UUID ventaId;

    @Column(name = "snack_id", nullable = false)
    private UUID snackId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double subtotal;

    // AGREGAR estos campos
    @Column(name = "usuario_id", nullable = true)
    private UUID usuarioId;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;
}
