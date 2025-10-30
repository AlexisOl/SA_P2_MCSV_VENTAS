package com.sa.ventas.ventasnack.aplicacion.puertos.salida.eventos;

import com.sa.ventas.ventasnack.dominio.VentaSnack;

import java.util.List;
import java.util.UUID;

public interface CrearFacturaSnacksDirecta {
    void crearFacturaSnacksDirecto(List<VentaSnack> ventaSnack, UUID idCine);

}
