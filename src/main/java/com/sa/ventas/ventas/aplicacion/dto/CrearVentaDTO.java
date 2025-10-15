package com.sa.ventas.ventas.aplicacion.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearVentaDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private UUID idUsuario;

    @NotNull(message = "El ID de la función es obligatorio")
    private UUID idFuncion;

    @NotNull(message = "Debe seleccionar al menos un asiento")
    private List<UUID> idsAsientos;

    @Positive(message = "El monto total debe ser mayor a cero")
    private Double montoTotal;
}
