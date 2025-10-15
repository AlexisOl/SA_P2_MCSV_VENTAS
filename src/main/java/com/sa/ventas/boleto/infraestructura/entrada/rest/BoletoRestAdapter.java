package com.sa.ventas.boleto.infraestructura.entrada.rest;

import com.sa.ventas.boleto.aplicacion.puertos.entrada.ListarBoletosInputPort;
import com.sa.ventas.boleto.infraestructura.entrada.rest.dto.ResponseBoletoDTO;
import com.sa.ventas.boleto.infraestructura.entrada.rest.mapper.BoletoRestMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Boletos", description = "API para consulta de boletos")
@RestController
@AllArgsConstructor
@RequestMapping("/boletos")
public class BoletoRestAdapter {

    private final ListarBoletosInputPort listarBoletosInputPort;
    private final BoletoRestMapper boletoRestMapper;

    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<List<ResponseBoletoDTO>> listarBoletosPorVenta(@PathVariable("idVenta") UUID idVenta) {
        return ResponseEntity.ok(
                boletoRestMapper.toResponseBoletosDto(
                        listarBoletosInputPort.listarBoletosPorVenta(idVenta)
                )
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
}
