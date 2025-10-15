package com.sa.ventas.ventas.aplicacion.puertos.salida;

import com.sa.ventas.ventas.dominio.Venta;

import java.util.UUID;

public interface EditarVentaOutputPort {

    Venta editarVenta(Venta venta);
    Venta obtenerVentaPorId(UUID id);
}
