package com.sa.ventas.ventasnack.aplicacion.puertos.entrada;

import com.sa.ventas.ventasnack.aplicacion.dto.FiltroVentaSnackDTO;
import com.sa.ventas.ventasnack.dominio.VentaSnack;

import java.util.List;
import java.util.UUID;

public interface ListarVentasSnacksInputPort {

    List<VentaSnack> listarVentasSnacks(FiltroVentaSnackDTO filtro);
    VentaSnack obtenerVentaSnackPorId(UUID ventaSnackId);
}
