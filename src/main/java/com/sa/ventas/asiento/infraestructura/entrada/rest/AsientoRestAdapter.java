package com.sa.ventas.asiento.infraestructura.entrada.rest;

import com.sa.ventas.asiento.aplicacion.dto.CrearAsientoDTO;
import com.sa.ventas.asiento.aplicacion.puertos.entrada.CrearAsientoInputPort;
import com.sa.ventas.asiento.aplicacion.puertos.entrada.ListarAsientosInputPort;
import com.sa.ventas.asiento.infraestructura.entrada.rest.dto.ResponseAsientoDTO;
import com.sa.ventas.asiento.infraestructura.entrada.rest.mapper.AsientoRestMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Asientos", description = "API para gestión de asientos")
@RestController
@AllArgsConstructor
@RequestMapping("/asientos")
public class AsientoRestAdapter {

    private final CrearAsientoInputPort crearAsientoInputPort;
    private final ListarAsientosInputPort listarAsientosInputPort;
    private final AsientoRestMapper asientoRestMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponseAsientoDTO> crearAsiento(@Valid @RequestBody CrearAsientoDTO crearAsientoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(asientoRestMapper.toResponseAsientoDto(
                        crearAsientoInputPort.crearAsiento(crearAsientoDTO)
                ));
    }

    @GetMapping("/sala/{salaId}")
    public ResponseEntity<List<ResponseAsientoDTO>> listarAsientosPorSala(@PathVariable("salaId") UUID salaId) {
        return ResponseEntity.ok(
                asientoRestMapper.toResponseAsientosDto(
                        listarAsientosInputPort.listarAsientosPorSala(salaId)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAsientoDTO> obtenerAsiento(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(
                asientoRestMapper.toResponseAsientoDto(
                        listarAsientosInputPort.obtenerAsientoPorId(id)
                )
        );
    }
}
