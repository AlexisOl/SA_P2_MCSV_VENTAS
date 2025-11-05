package com.sa.ventas.ventas.infraestructura.entrada.rest;

import com.sa.ventas.ventas.aplicacion.dto.CrearVentaDTO;
import com.sa.ventas.ventas.aplicacion.dto.EditarVentaDTO;
import com.sa.ventas.ventas.aplicacion.dto.FiltroVentaDTO;
import com.sa.ventas.ventas.aplicacion.puertos.entrada.AnularVentaInputPort;
import com.sa.ventas.ventas.aplicacion.puertos.entrada.CrearVentaInputPort;
import com.sa.ventas.ventas.aplicacion.puertos.entrada.EditarVentaInputPort;
import com.sa.ventas.ventas.aplicacion.puertos.entrada.ListarVentasInputPort;
import com.sa.ventas.ventas.infraestructura.entrada.rest.dto.ResponseVentaDTO;
import com.sa.ventas.ventas.infraestructura.entrada.rest.mapper.VentaRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Ventas", description = "API para gestión de ventas de boletos")
@RestController
@AllArgsConstructor
@RequestMapping("/ventas")
public class VentaRestAdapter {

    private final CrearVentaInputPort crearVentaInputPort;
    private final EditarVentaInputPort editarVentaInputPort;
    private final AnularVentaInputPort anularVentaInputPort;
    private final ListarVentasInputPort listarVentasInputPort;
    private final VentaRestMapper ventaRestMapper;

    @Operation(summary = "Crear una nueva venta",
            description = """
                   Crea una nueva venta de boletos con opción de aplicar promociones automáticamente.
                   
                   Para aplicar promociones:
                   - Establecer 'aplicarPromocion' en true
                   - Proporcionar 'salaId' y/o 'peliculaId' para promociones específicas
                   
                   El sistema buscará la mejor promoción disponible y la aplicará automáticamente.
                   """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta creada exitosamente (con o sin promoción)"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Asientos no disponibles")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponseVentaDTO> crearVenta(@Valid @RequestBody CrearVentaDTO crearVentaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ventaRestMapper.toResponseVentaDto(
                        crearVentaInputPort.crearVenta(crearVentaDTO)
                ));
    }

    @Operation(summary = "Editar una venta", description = "Actualiza los datos de una venta existente")
    @PutMapping("/{ventaId}")
    @Transactional
    public ResponseEntity<ResponseVentaDTO> editarVenta(
            @PathVariable("ventaId") UUID ventaId,
            @Valid @RequestBody EditarVentaDTO editarVentaDTO) {
        return ResponseEntity.ok(
                ventaRestMapper.toResponseVentaDto(
                        editarVentaInputPort.editarVenta(ventaId, editarVentaDTO)
                )
        );
    }

    @Operation(summary = "Anular una venta", description = "Anula una venta y libera los asientos")
    @DeleteMapping("/{ventaId}")
    @Transactional
    public ResponseEntity<Void> anularVenta(@PathVariable("ventaId") UUID ventaId) {
        anularVentaInputPort.anularVenta(ventaId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar ventas", description = "Lista todas las ventas con filtros opcionales")
    @GetMapping
    public ResponseEntity<List<ResponseVentaDTO>> listarVentas(
            @RequestParam(required = false) UUID usuarioId,
            @RequestParam(required = false) UUID funcionId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {

        FiltroVentaDTO filtro = new FiltroVentaDTO();
        filtro.setUsuarioId(usuarioId);
        filtro.setFuncionId(funcionId);

        return ResponseEntity.ok(
                ventaRestMapper.toResponseVentasDto(
                        listarVentasInputPort.listarVentas(filtro)
                )
        );
    }

    @Operation(summary = "Obtener venta por ID",
            description = "Obtiene los detalles de una venta específica, incluyendo información de promoción si fue aplicada")
    @GetMapping("/{ventaId}")
    public ResponseEntity<ResponseVentaDTO> obtenerVenta(@PathVariable("ventaId") UUID ventaId) {
        return ResponseEntity.ok(
                ventaRestMapper.toResponseVentaDto(
                        listarVentasInputPort.obtenerVentaPorId(ventaId)
                )
        );
    }
}
