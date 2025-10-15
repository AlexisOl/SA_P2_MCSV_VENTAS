package com.sa.ventas.ventas.aplicacion.puertos.salida;

import com.sa.ventas.ventas.aplicacion.dto.FiltroVentaDTO;
import com.sa.ventas.ventas.dominio.Venta;

import java.util.List;
import java.util.UUID;

public interface ListarVentasOutputPort {

    List<Venta> listarVentas(FiltroVentaDTO filtro);
    Venta obtenerVentaPorId(UUID id);
}
