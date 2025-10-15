package com.sa.ventas.boleto.aplicacion.puertos.entrada;

import com.sa.ventas.boleto.dominio.Boleto;

import java.util.List;
import java.util.UUID;

public interface ListarBoletosInputPort {

    List<Boleto> listarBoletosPorVenta(UUID ventaId);
    Boleto obtenerBoletoPorCodigo(String codigoBoleto);
}
