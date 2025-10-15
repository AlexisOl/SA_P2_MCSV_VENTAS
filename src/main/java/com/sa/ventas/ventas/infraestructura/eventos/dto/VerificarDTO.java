package com.sa.ventas.ventas.infraestructura.eventos.dto;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificarDTO {

    private UUID id;
    private String correlationId;
}
