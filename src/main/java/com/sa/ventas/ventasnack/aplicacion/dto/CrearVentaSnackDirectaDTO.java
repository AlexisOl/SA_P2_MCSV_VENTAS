package com.sa.ventas.ventasnack.aplicacion.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearVentaSnackDirectaDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private UUID usuarioId;

    @NotNull(message = "Debe seleccionar al menos un snack")
    private Map<UUID, Integer> snacks; // Map<snackId, cantidad>
}
