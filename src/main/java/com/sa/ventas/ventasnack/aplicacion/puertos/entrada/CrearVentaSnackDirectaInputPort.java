package com.sa.ventas.ventasnack.aplicacion.puertos.entrada;

import com.sa.ventas.ventasnack.aplicacion.dto.CrearVentaSnackDirectaDTO;
import com.sa.ventas.ventasnack.dominio.VentaSnack;

import java.util.List;

public interface CrearVentaSnackDirectaInputPort {

    List<VentaSnack> crearVentaSnackDirecta(CrearVentaSnackDirectaDTO dto);
}
