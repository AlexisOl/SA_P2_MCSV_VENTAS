package com.sa.ventas.ventasnack.infraestructura.eventos.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificarSnackDTO {

    private UUID snackId;
    private String correlationId;
}
