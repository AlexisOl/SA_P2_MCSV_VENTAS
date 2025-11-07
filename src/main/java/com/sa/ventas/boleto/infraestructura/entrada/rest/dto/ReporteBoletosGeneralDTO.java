package com.sa.ventas.boleto.infraestructura.entrada.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteBoletosGeneralDTO {
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer totalBoletosVendidos;
    private Double totalDineroRecaudado;
    private List<ReporteBoletosSalaDTO> detallesPorSala;
}
