package com.sa.ventas.ventas.dominio;

import com.sa.ventas.ventas.dominio.objeto_valor.EstadoVenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    private UUID ventaId;
    private UUID usuarioId;
    private UUID funcionId;
    private LocalDateTime fechaVenta;
    private Double montoTotal;
    private EstadoVenta estado;
    private Integer cantidadBoletos;
    private UUID promocionAplicadaId;
    private Double montoDescuento;
    private Double porcentajeDescuento;
    private Double montoOriginal;

    public Venta(UUID ventaId, UUID usuarioId, UUID funcionId, LocalDateTime fechaVenta,
                 Double montoTotal, EstadoVenta estado, Integer cantidadBoletos) {
        this.ventaId = ventaId;
        this.usuarioId = usuarioId;
        this.funcionId = funcionId;
        this.fechaVenta = fechaVenta;
        this.montoTotal = montoTotal;
        this.estado = estado;
        this.cantidadBoletos = cantidadBoletos;
        this.montoOriginal = montoTotal;
        this.montoDescuento = 0.0;
        this.porcentajeDescuento = 0.0;
        validarVenta();
    }

    // Constructor con promoción
    public Venta(UUID ventaId, UUID usuarioId, UUID funcionId, LocalDateTime fechaVenta,
                 Double montoTotal, EstadoVenta estado, Integer cantidadBoletos,
                 UUID promocionAplicadaId, Double porcentajeDescuento) {
        this.ventaId = ventaId;
        this.usuarioId = usuarioId;
        this.funcionId = funcionId;
        this.fechaVenta = fechaVenta;
        this.montoOriginal = montoTotal;
        this.estado = estado;
        this.cantidadBoletos = cantidadBoletos;
        this.promocionAplicadaId = promocionAplicadaId;
        this.porcentajeDescuento = porcentajeDescuento;
        aplicarDescuento();
        validarVenta();
    }

    private void validarVenta() {
        if (montoTotal < 0) {
            throw new IllegalArgumentException("El monto total no puede ser negativo");
        }
        if (cantidadBoletos <= 0) {
            throw new IllegalArgumentException("Debe haber al menos un boleto");
        }
        if (porcentajeDescuento != null && (porcentajeDescuento < 0 || porcentajeDescuento > 100)) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100");
        }
    }

    private void aplicarDescuento() {
        if (porcentajeDescuento != null && porcentajeDescuento > 0) {
            this.montoDescuento = this.montoOriginal * (porcentajeDescuento / 100);
            this.montoTotal = this.montoOriginal - this.montoDescuento;
        } else {
            this.montoDescuento = 0.0;
            this.montoTotal = this.montoOriginal;
        }
    }

    public void aplicarPromocion(UUID promocionId, Double porcentaje) {
        if (this.promocionAplicadaId != null) {
            throw new IllegalStateException("Ya existe una promoción aplicada a esta venta");
        }
        this.promocionAplicadaId = promocionId;
        this.porcentajeDescuento = porcentaje;
        aplicarDescuento();
    }

    public void anular() {
        if (this.estado == EstadoVenta.ANULADA) {
            throw new IllegalStateException("La venta ya está anulada");
        }
        this.estado = EstadoVenta.ANULADA;
    }
}
