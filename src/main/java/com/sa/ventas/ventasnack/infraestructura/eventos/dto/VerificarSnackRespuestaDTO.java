package com.sa.ventas.ventasnack.infraestructura.eventos.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificarSnackRespuestaDTO {

    private boolean existe;
    private Double precio;
    private String correlationId;
}
