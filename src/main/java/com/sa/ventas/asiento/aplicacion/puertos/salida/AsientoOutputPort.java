package com.sa.ventas.asiento.aplicacion.puertos.salida;

import com.sa.ventas.asiento.dominio.Asiento;

import java.util.List;
import java.util.UUID;

public interface AsientoOutputPort {

    Asiento obtenerAsientoPorId(UUID id);
    List<Asiento> obtenerAsientosPorIds(List<UUID> ids);
    void reservarAsientos(List<UUID> ids);
    void liberarAsientos(List<UUID> ids);
    boolean verificarDisponibilidad(List<UUID> ids);
}
