package com.sa.ventas.boleto.infraestructura.entrada.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCompradorDTO {
    private UUID usuarioId;
    private String nombreUsuario; // Si lo tienes disponible
    private String emailUsuario;  // Si lo tienes disponible
    private Integer cantidadBoletosComprados;
    private Double totalGastado;
}
