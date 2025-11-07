package com.sa.ventas.boleto.infraestructura.entrada.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBoletoDetalladoDTO {

    // Datos básicos del boleto
    private UUID boletoId;
    private UUID ventaId;
    private UUID asientoId;
    private Double precio;
    private String codigoBoleto;

    // Datos del asiento
    private String fila;
    private Integer columna;
    private UUID salaId;

    // Datos de la película (desde microservicio movies)
    private UUID peliculaId;
    private String peliculaTitulo;
    private Integer peliculaDuracion;

    // Datos de la función/horario (desde microservicio movies)
    private UUID funcionId;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String idioma;
    private String formato;
}
