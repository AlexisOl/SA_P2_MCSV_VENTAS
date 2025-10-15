package com.sa.ventas.ventas.aplicacion.puertos.salida;

import com.sa.ventas.ventas.dominio.Venta;

public interface CrearVentaOutputPort {

    Venta crearVenta(Venta venta);

}
