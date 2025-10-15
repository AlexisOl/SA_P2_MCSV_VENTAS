package com.sa.ventas.asiento.aplicacion.casouso;

import com.sa.ventas.asiento.aplicacion.dto.CrearAsientoDTO;
import com.sa.ventas.asiento.aplicacion.puertos.entrada.CrearAsientoInputPort;

import com.sa.ventas.asiento.aplicacion.puertos.salida.CrearAsientoOutputPort;
import com.sa.ventas.asiento.dominio.Asiento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CrearAsientoCasoUso implements CrearAsientoInputPort {

    private final CrearAsientoOutputPort crearAsientoOutputPort;

    public CrearAsientoCasoUso(CrearAsientoOutputPort crearAsientoOutputPort) {
        this.crearAsientoOutputPort = crearAsientoOutputPort;
    }

    @Override
    @Transactional
    public Asiento crearAsiento(CrearAsientoDTO crearAsientoDTO) {
        Asiento nuevoAsiento = new Asiento(
                UUID.randomUUID(),
                crearAsientoDTO.getFila(),
                crearAsientoDTO.getColumna(),
                crearAsientoDTO.getSalaId(),
                true // Disponible por defecto
        );

        return crearAsientoOutputPort.crearAsiento(nuevoAsiento);
    }
}
