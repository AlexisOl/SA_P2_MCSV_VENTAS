package com.sa.ventas.boleto.dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Boleto {

    private UUID boletoId;
    private UUID ventaId;
    private UUID asientoId;
    private Double precio;
    private String codigoBoleto;

    public Boleto(UUID boletoId, UUID ventaId, UUID asientoId, Double precio, String codigoBoleto) {
        this.boletoId = boletoId;
        this.ventaId = ventaId;
        this.asientoId = asientoId;
        this.precio = precio;
        this.codigoBoleto = codigoBoleto;
        validarBoleto();
    }

    private void validarBoleto() {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio del boleto debe ser mayor a cero");
        }
        if (codigoBoleto == null || codigoBoleto.isEmpty()) {
            throw new IllegalArgumentException("El código del boleto es obligatorio");
        }
    }
}
