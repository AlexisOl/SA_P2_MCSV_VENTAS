package com.sa.ventas.boleto.infraestructura.entrada.rest;

import com.sa.ventas.boleto.aplicacion.puertos.entrada.GenerarReporteBoletosInputPort;
import com.sa.ventas.boleto.aplicacion.puertos.entrada.ListarBoletosInputPort;
import com.sa.ventas.boleto.infraestructura.entrada.rest.dto.ReporteBoletosGeneralDTO;
import com.sa.ventas.boleto.infraestructura.entrada.rest.dto.ResponseBoletoDTO;
import com.sa.ventas.boleto.infraestructura.entrada.rest.dto.ResponseBoletoDetalladoDTO;
import com.sa.ventas.boleto.infraestructura.entrada.rest.mapper.BoletoRestMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "Boletos", description = "API para consulta de boletos")
@RestController
@AllArgsConstructor
@RequestMapping("/boletos")
public class BoletoRestAdapter {

    private final ListarBoletosInputPort listarBoletosInputPort;
    private final BoletoRestMapper boletoRestMapper;
    private final GenerarReporteBoletosInputPort generarReporteBoletosInputPort;

    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<List<ResponseBoletoDetalladoDTO>> listarBoletosPorVenta(@PathVariable("idVenta") UUID idVenta) {
        return ResponseEntity.ok(
                listarBoletosInputPort.listarBoletosPorVenta(idVenta)
        );
    }

    @GetMapping("/codigo/{codigoBoleto}")
    public ResponseEntity<ResponseBoletoDTO> obtenerBoletoPorCodigo(@PathVariable("codigoBoleto") String codigoBoleto) {
        return ResponseEntity.ok(
                boletoRestMapper.toResponseBoletoDto(
                        listarBoletosInputPort.obtenerBoletoPorCodigo(codigoBoleto)
                )
        );
    }

    @GetMapping("/reporte")
    public ResponseEntity<ReporteBoletosGeneralDTO> generarReporte(
            @RequestParam(name = "fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(name = "fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) UUID salaId) {

        return ResponseEntity.ok(
                generarReporteBoletosInputPort.generarReporte(fechaInicio, fechaFin, salaId)
        );
    }
}
