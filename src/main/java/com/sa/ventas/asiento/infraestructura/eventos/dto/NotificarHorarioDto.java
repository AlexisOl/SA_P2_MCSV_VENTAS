package com.sa.ventas.asiento.infraestructura.eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificarHorarioDto {

    private UUID funcionId;
    private UUID salaId;
    private Integer fila;
    private Integer columna;
}
