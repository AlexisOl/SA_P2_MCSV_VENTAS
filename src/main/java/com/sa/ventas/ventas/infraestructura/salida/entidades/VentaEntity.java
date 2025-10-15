package com.sa.ventas.ventas.infraestructura.salida.entidades;

import com.sa.ventas.ventas.dominio.objeto_valor.EstadoVenta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "venta")
public class VentaEntity {

    @Id
    @Column(name = "venta_id", updatable = false, nullable = false)
    private UUID ventaId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "funcion_id", nullable = false)
    private UUID funcionId;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVenta estado;

    @Column(name = "cantidad_boletos", nullable = false)
    private Integer cantidadBoletos;

}
