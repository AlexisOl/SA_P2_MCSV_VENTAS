package com.sa.ventas.boleto.aplicacion.puertos.salida;

import com.sa.ventas.boleto.dominio.Boleto;

import java.util.List;
import java.util.UUID;

public interface ListarBoletosOutputPort {

    List<Boleto> listarBoletosPorVenta(UUID ventaId);
    Boleto obtenerBoletoPorCodigo(String codigoBoleto);
}
