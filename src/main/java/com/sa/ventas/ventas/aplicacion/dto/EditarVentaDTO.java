package com.sa.ventas.ventas.aplicacion.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditarVentaDTO {

    private UUID idFuncion;
    private Double montoTotal;

}
