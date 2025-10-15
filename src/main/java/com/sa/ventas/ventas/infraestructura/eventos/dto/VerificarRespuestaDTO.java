package com.sa.ventas.ventas.infraestructura.eventos.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificarRespuestaDTO {

    private boolean existe;
    private String correlationId;
}
