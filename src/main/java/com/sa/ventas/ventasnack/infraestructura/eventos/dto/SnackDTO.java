package com.sa.ventas.ventasnack.infraestructura.eventos.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnackDTO {

    private UUID snackId;
    private String nombre;
    private Double precio;
    private UUID cineId;
}
