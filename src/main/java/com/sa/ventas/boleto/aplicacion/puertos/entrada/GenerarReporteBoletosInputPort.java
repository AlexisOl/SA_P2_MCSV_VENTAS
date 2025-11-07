package com.sa.ventas.boleto.aplicacion.puertos.entrada;

import com.sa.ventas.boleto.infraestructura.entrada.rest.dto.ReporteBoletosGeneralDTO;
import java.time.LocalDateTime;
import java.util.UUID;

public interface GenerarReporteBoletosInputPort {
    ReporteBoletosGeneralDTO generarReporte(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            UUID salaId
    );
}
