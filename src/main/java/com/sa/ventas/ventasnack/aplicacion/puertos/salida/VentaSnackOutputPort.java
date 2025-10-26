package com.sa.ventas.ventasnack.aplicacion.puertos.salida;

import com.sa.ventas.ventasnack.aplicacion.dto.FiltroVentaSnackDTO;
import com.sa.ventas.ventasnack.dominio.VentaSnack;

import java.util.List;
import java.util.UUID;

public interface VentaSnackOutputPort {

    VentaSnack crearVentaSnack(VentaSnack ventaSnack);
    List<VentaSnack> crearVentasSnacks(List<VentaSnack> ventasSnacks);
    List<VentaSnack> obtenerVentasSnacksPorVenta(UUID ventaId);
    List<VentaSnack> listarVentasSnacks(FiltroVentaSnackDTO filtro);
    VentaSnack obtenerVentaSnackPorId(UUID ventaSnackId);
}
