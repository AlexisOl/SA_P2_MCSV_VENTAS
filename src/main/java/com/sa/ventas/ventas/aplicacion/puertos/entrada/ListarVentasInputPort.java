package com.sa.ventas.ventas.aplicacion.puertos.entrada;

import com.sa.ventas.ventas.aplicacion.dto.FiltroVentaDTO;
import com.sa.ventas.ventas.dominio.Venta;

import java.util.List;
import java.util.UUID;

public interface ListarVentasInputPort {

    List<Venta> listarVentas(FiltroVentaDTO filtro);

    Venta obtenerVentaPorId(UUID id);
}
