package com.sa.ventas.ventas.aplicacion.puertos.entrada;

import com.sa.ventas.ventas.aplicacion.dto.EditarVentaDTO;
import com.sa.ventas.ventas.dominio.Venta;

import java.util.UUID;

public interface EditarVentaInputPort {

    Venta editarVenta(UUID id, EditarVentaDTO editarVentaDTO);
}
