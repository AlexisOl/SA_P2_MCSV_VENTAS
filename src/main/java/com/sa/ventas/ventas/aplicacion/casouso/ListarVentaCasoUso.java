package com.sa.ventas.ventas.aplicacion.casouso;

import com.sa.ventas.ventas.aplicacion.dto.FiltroVentaDTO;
import com.sa.ventas.ventas.aplicacion.puertos.entrada.ListarVentasInputPort;

import com.sa.ventas.ventas.aplicacion.puertos.salida.ListarVentasOutputPort;
import com.sa.ventas.ventas.dominio.Venta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListarVentaCasoUso implements ListarVentasInputPort {

    private final ListarVentasOutputPort listarVentasOutputPort;


    public ListarVentaCasoUso(ListarVentasOutputPort listarVentasOutputPort) {
        this.listarVentasOutputPort = listarVentasOutputPort;
    }

    @Override
    public List<Venta> listarVentas(FiltroVentaDTO filtro) {
        return listarVentasOutputPort.listarVentas(filtro);
    }

    @Override
    public Venta obtenerVentaPorId(UUID id) {
        Venta venta = listarVentasOutputPort.obtenerVentaPorId(id);
        if (venta == null) {
            throw new IllegalArgumentException("La venta no existe");
        }
        return venta;
    }
}
