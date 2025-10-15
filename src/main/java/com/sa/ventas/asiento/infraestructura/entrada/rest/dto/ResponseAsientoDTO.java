package com.sa.ventas.asiento.infraestructura.entrada.rest.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class ResponseAsientoDTO {

    private UUID asientoId;
    private String fila;
    private Integer columna;
    private UUID salaId;
    private Boolean disponible;
}
