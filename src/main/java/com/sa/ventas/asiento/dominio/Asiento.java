package com.sa.ventas.asiento.dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Asiento {

    private UUID asientoId;
    private String fila;
    private Integer columna;
    private UUID salaId;
    private Boolean disponible;

    public Asiento(UUID asientoId, String fila, Integer columna, UUID salaId, Boolean disponible) {
        this.asientoId = asientoId;
        this.fila = fila;
        this.columna = columna;
        this.salaId = salaId;
        this.disponible = disponible;
        validarAsiento();
    }

    private void validarAsiento() {
        if (fila == null || fila.isEmpty()) {
            throw new IllegalArgumentException("La fila es obligatoria");
        }
        if (columna <= 0) {
            throw new IllegalArgumentException("La columna debe ser mayor a cero");
        }
    }

    public void reservar() {
        if (!disponible) {
            throw new IllegalStateException("El asiento no está disponible");
        }
        this.disponible = false;
    }

    public void liberar() {
        this.disponible = true;
    }
}
