package com.sa.ventas.ventas.aplicacion.puertos.salida.eventos;

import com.sa.ventas.ventas.dominio.Venta;
import com.sa.ventas.ventasnack.dominio.VentaSnack;

import java.util.List;
import java.util.UUID;

public interface CrearFacturaSnacks {
    void crearFacturaSnacks(List<VentaSnack> ventaSnack, UUID idCine);

}
