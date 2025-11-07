package com.sa.ventas.asiento.aplicacion.puertos.entrada;

import com.sa.ventas.asiento.dominio.Asiento;

import java.util.List;
import java.util.UUID;

public interface ListarAsientosInputPort {

    List<Asiento> listarAsientosPorSala(UUID salaId, UUID funcionId);
    Asiento obtenerAsientoPorId(UUID asientoId);
}
