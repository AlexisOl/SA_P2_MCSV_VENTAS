package com.sa.ventas.boleto.aplicacion.dto;

import java.util.UUID;

@lombok.Value
public class BoletoConDetalles {
    UUID boletoId;
    UUID ventaId;
    Double precio;
    UUID usuarioId;
    UUID salaId;
}
