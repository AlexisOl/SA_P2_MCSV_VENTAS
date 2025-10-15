package com.sa.ventas.asiento.aplicacion.puertos.entrada;

import com.sa.ventas.asiento.aplicacion.dto.CrearAsientoDTO;
import com.sa.ventas.asiento.dominio.Asiento;

public interface CrearAsientoInputPort {

    Asiento crearAsiento(CrearAsientoDTO crearAsientoDTO);
}
