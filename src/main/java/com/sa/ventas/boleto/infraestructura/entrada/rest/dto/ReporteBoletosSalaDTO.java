package com.sa.ventas.boleto.infraestructura.entrada.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteBoletosSalaDTO {
    private UUID salaId;
    private String nombreSala; // Si lo tienes disponible
    private Integer totalBoletos;
    private Double totalDinero;
    private List<UsuarioCompradorDTO> usuarios;
}
