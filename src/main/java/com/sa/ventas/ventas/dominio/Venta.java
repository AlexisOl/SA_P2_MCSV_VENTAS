package com.sa.ventas.ventas.dominio;

import com.sa.ventas.ventas.dominio.objeto_valor.EstadoVenta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Venta {

    private UUID ventaId;
    private UUID usuarioId;
    private UUID funcionId;
    private LocalDateTime fechaVenta;
    private Double montoTotal;
    private EstadoVenta estado;
    private Integer cantidadBoletos;

    public Venta(UUID ventaId, UUID usuarioId, UUID funcionId, LocalDateTime fechaVenta,
                 Double montoTotal, EstadoVenta estado, Integer cantidadBoletos) {
        this.ventaId = ventaId;
        this.usuarioId = usuarioId;
        this.funcionId = funcionId;
        this.fechaVenta = fechaVenta;
        this.montoTotal = montoTotal;
        this.estado = estado;
        this.cantidadBoletos = cantidadBoletos;
        validarVenta();
    }

    private void validarVenta() {
        if (montoTotal < 0) {
            throw new IllegalArgumentException("El monto total no puede ser negativo");
        }
        if (cantidadBoletos <= 0) {
            throw new IllegalArgumentException("Debe haber al menos un boleto");
        }
    }

    public void anular() {
        if (this.estado == EstadoVenta.ANULADA) {
            throw new IllegalStateException("La venta ya está anulada");
        }
        this.estado = EstadoVenta.ANULADA;
    }
}
