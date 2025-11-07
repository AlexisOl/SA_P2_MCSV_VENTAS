package com.sa.ventas.boleto.aplicacion.puertos.entrada;

import com.sa.ventas.boleto.dominio.Boleto;
import com.sa.ventas.boleto.infraestructura.entrada.rest.dto.ResponseBoletoDetalladoDTO;

import java.util.List;
import java.util.UUID;

public interface ListarBoletosInputPort {

    List<ResponseBoletoDetalladoDTO> listarBoletosPorVenta(UUID ventaId);
    Boleto obtenerBoletoPorCodigo(String codigoBoleto);
}
