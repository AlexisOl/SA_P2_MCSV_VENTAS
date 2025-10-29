package com.sa.ventas.ventas.aplicacion.puertos.salida.eventos;

import com.example.comun.DTO.FacturaBoleto.FacturaBoletoCreadoDTO;
import com.sa.ventas.ventas.dominio.Venta;

import java.util.UUID;

public interface CrearFacturaBoleto {
    void crearFacturaBoleto(Venta venta, UUID idCine);

}
