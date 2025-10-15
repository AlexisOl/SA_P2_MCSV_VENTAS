package com.sa.ventas.boleto.infraestructura.entrada.rest.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class ResponseBoletoDTO {

    private UUID boletoId;
    private UUID ventaId;
    private UUID asientoId;
    private Double precio;
    private String codigoBoleto;
}
