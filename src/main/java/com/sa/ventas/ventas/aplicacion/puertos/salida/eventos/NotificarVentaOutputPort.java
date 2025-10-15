package com.sa.ventas.ventas.aplicacion.puertos.salida.eventos;

import com.sa.ventas.ventas.dominio.Venta;

public interface NotificarVentaOutputPort {

    void notificarVentaCreada(Venta venta);
    void notificarVentaAnulada(Venta venta);
}
