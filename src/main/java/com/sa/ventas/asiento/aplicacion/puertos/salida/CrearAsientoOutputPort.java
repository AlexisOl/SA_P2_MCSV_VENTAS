package com.sa.ventas.asiento.aplicacion.puertos.salida;

import com.sa.ventas.asiento.dominio.Asiento;

public interface CrearAsientoOutputPort {

    Asiento crearAsiento(Asiento asiento);
}
