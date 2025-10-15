package com.sa.ventas.asiento.aplicacion.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearAsientoDTO {

    @NotNull(message = "El ID de la sala es obligatorio")
    private UUID salaId;

    @NotNull(message = "El numero de fila es obligatorio")
    private String fila;
    @NotNull(message = "El numero de columna es obligatorio")
    private Integer columna;

}
