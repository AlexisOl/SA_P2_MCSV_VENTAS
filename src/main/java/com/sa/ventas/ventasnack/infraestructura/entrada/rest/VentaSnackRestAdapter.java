package com.sa.ventas.ventasnack.infraestructura.entrada.rest;

import com.sa.ventas.ventasnack.aplicacion.dto.CrearVentaSnackDirectaDTO;
import com.sa.ventas.ventasnack.aplicacion.dto.FiltroVentaSnackDTO;
import com.sa.ventas.ventasnack.aplicacion.puertos.entrada.CrearVentaSnackDirectaInputPort;
import com.sa.ventas.ventasnack.aplicacion.puertos.entrada.ListarVentasSnacksInputPort;
import com.sa.ventas.ventasnack.infraestructura.entrada.rest.dto.ResponseVentaSnackDTO;
import com.sa.ventas.ventasnack.infraestructura.entrada.rest.mapper.VentaSnackRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "Venta Snacks", description = "API para venta directa de snacks")
@RestController
@AllArgsConstructor
@RequestMapping("/venta-snacks")
public class VentaSnackRestAdapter {

    private final CrearVentaSnackDirectaInputPort crearVentaSnackDirectaInputPort;
    private final VentaSnackRestMapper ventaSnackRestMapper;
    private final ListarVentasSnacksInputPort listarVentasSnacksInputPort;

    @Operation(summary = "Comprar snacks sin boletos",
            description = "Permite comprar snacks directamente sin necesidad de comprar boletos")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<List<ResponseVentaSnackDTO>> comprarSnacksDirecto(
            @Valid @RequestBody CrearVentaSnackDirectaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ventaSnackRestMapper.toResponseVentaSnacksDto(
                        crearVentaSnackDirectaInputPort.crearVentaSnackDirecta(dto)
                ));
    }

    @Operation(summary = "Listar ventas de snacks con filtros",
            description = "Lista todas las ventas de snacks. Todos los filtros son opcionales.")
    @GetMapping
    public ResponseEntity<List<ResponseVentaSnackDTO>> listarVentasSnacks(
            @Parameter(description = "ID de la venta de snack específica")
            @RequestParam(required = false) UUID ventaSnackId,

            @Parameter(description = "ID de la venta de boletos asociada")
            @RequestParam(required = false) UUID ventaId,

            @Parameter(description = "ID del snack")
            @RequestParam(required = false) UUID snackId,

            @Parameter(description = "ID del usuario que realizó la compra")
            @RequestParam(required = false) UUID usuarioId,

            @Parameter(description = "Fecha de inicio del rango de búsqueda (formato: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,

            @Parameter(description = "Fecha fin del rango de búsqueda (formato: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        FiltroVentaSnackDTO filtro = new FiltroVentaSnackDTO();
        filtro.setVentaSnackId(ventaSnackId);
        filtro.setVentaId(ventaId);
        filtro.setSnackId(snackId);
        filtro.setUsuarioId(usuarioId);
        filtro.setFechaInicio(fechaInicio);
        filtro.setFechaFin(fechaFin);

        return ResponseEntity.ok(
                ventaSnackRestMapper.toResponseVentaSnacksDto(
                        listarVentasSnacksInputPort.listarVentasSnacks(filtro)
                )
        );
    }

    @Operation(summary = "Obtener venta de snack por ID",
            description = "Obtiene los detalles de una venta de snack específica")
    @GetMapping("/{ventaSnackId}")
    public ResponseEntity<ResponseVentaSnackDTO> obtenerVentaSnack(
            @PathVariable("ventaSnackId") UUID ventaSnackId) {
        return ResponseEntity.ok(
                ventaSnackRestMapper.toResponseVentaSnackDto(
                        listarVentasSnacksInputPort.obtenerVentaSnackPorId(ventaSnackId)
                )
        );
    }

    @Operation(summary = "Listar ventas de snacks por venta de boletos",
            description = "Lista todos los snacks asociados a una venta de boletos específica")
    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<List<ResponseVentaSnackDTO>> listarVentasSnacksPorVenta(
            @PathVariable("ventaId") UUID ventaId) {

        FiltroVentaSnackDTO filtro = new FiltroVentaSnackDTO();
        filtro.setVentaId(ventaId);

        return ResponseEntity.ok(
                ventaSnackRestMapper.toResponseVentaSnacksDto(
                        listarVentasSnacksInputPort.listarVentasSnacks(filtro)
                )
        );
    }

    @Operation(summary = "Listar ventas de snacks por usuario",
            description = "Lista todas las ventas de snacks realizadas por un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ResponseVentaSnackDTO>> listarVentasSnacksPorUsuario(
            @PathVariable("usuarioId") UUID usuarioId) {

        FiltroVentaSnackDTO filtro = new FiltroVentaSnackDTO();
        filtro.setUsuarioId(usuarioId);

        return ResponseEntity.ok(
                ventaSnackRestMapper.toResponseVentaSnacksDto(
                        listarVentasSnacksInputPort.listarVentasSnacks(filtro)
                )
        );
    }

    @Operation(summary = "Listar ventas de un snack específico",
            description = "Lista todas las ventas de un snack en particular")
    @GetMapping("/snack/{snackId}")
    public ResponseEntity<List<ResponseVentaSnackDTO>> listarVentasPorSnack(
            @PathVariable("snackId") UUID snackId) {

        FiltroVentaSnackDTO filtro = new FiltroVentaSnackDTO();
        filtro.setSnackId(snackId);

        return ResponseEntity.ok(
                ventaSnackRestMapper.toResponseVentaSnacksDto(
                        listarVentasSnacksInputPort.listarVentasSnacks(filtro)
                )
        );
    }
}
