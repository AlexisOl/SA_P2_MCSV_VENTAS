package com.sa.ventas.boleto.aplicacion.puertos.salida;

import com.sa.ventas.boleto.dominio.Boleto;

import java.util.List;
import java.util.UUID;

public interface BoletoOutputPort {

    Boleto crearBoleto(Boleto boleto);
    List<Boleto> crearBoletos(List<Boleto> boletos);
    List<Boleto> obtenerBoletosPorVenta(UUID idVenta);

}
