package com.sa.ventas.ventas.aplicacion.puertos.salida;

import com.sa.ventas.ventas.dominio.objeto_valor.EstadoVenta;

import java.util.UUID;

public interface CambiarEstadoVenta {
    void cambiarEstadoVenta(UUID id, EstadoVenta estadoVenta);
}
