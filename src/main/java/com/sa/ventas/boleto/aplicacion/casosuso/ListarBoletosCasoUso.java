package com.sa.ventas.boleto.aplicacion.casosuso;

import com.sa.ventas.boleto.aplicacion.puertos.entrada.ListarBoletosInputPort;

import com.sa.ventas.boleto.aplicacion.puertos.salida.ListarBoletosOutputPort;
import com.sa.ventas.boleto.dominio.Boleto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListarBoletosCasoUso implements ListarBoletosInputPort {

    private final ListarBoletosOutputPort listarBoletosOutputPort;

    public ListarBoletosCasoUso(ListarBoletosOutputPort listarBoletosOutputPort) {
        this.listarBoletosOutputPort = listarBoletosOutputPort;
    }

    @Override
    public List<Boleto> listarBoletosPorVenta(UUID ventaId) {
        return listarBoletosOutputPort.listarBoletosPorVenta(ventaId);
    }

    @Override
    public Boleto obtenerBoletoPorCodigo(String codigoBoleto) {
        Boleto boleto = listarBoletosOutputPort.obtenerBoletoPorCodigo(codigoBoleto);
        if (boleto == null) {
            throw new IllegalArgumentException("El boleto no existe");
        }
        return boleto;
    }
}
