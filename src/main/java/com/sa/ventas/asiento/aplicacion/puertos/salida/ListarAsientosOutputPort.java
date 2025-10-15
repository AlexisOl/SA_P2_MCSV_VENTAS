package com.sa.ventas.asiento.aplicacion.puertos.salida;

import com.sa.ventas.asiento.dominio.Asiento;

import java.util.List;
import java.util.UUID;

public interface ListarAsientosOutputPort {

    List<Asiento> listarAsientosPorSala(UUID salaId);
    Asiento obtenerAsientoPorId(UUID asientoId);
}
