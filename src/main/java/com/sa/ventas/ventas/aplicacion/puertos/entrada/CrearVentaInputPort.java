package com.sa.ventas.ventas.aplicacion.puertos.entrada;

import com.sa.ventas.ventas.aplicacion.dto.CrearVentaDTO;
import com.sa.ventas.ventas.dominio.Venta;

public interface CrearVentaInputPort {

    Venta crearVenta(CrearVentaDTO crearVentaDTO);
}
