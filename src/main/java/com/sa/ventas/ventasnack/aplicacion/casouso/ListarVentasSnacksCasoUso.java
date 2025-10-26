package com.sa.ventas.ventasnack.aplicacion.casouso;

import com.sa.ventas.ventasnack.aplicacion.dto.FiltroVentaSnackDTO;
import com.sa.ventas.ventasnack.aplicacion.puertos.entrada.ListarVentasSnacksInputPort;
import com.sa.ventas.ventasnack.aplicacion.puertos.salida.VentaSnackOutputPort;
import com.sa.ventas.ventasnack.dominio.VentaSnack;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListarVentasSnacksCasoUso implements ListarVentasSnacksInputPort {

    private final VentaSnackOutputPort ventaSnackOutputPort;

    public ListarVentasSnacksCasoUso(VentaSnackOutputPort ventaSnackOutputPort) {
        this.ventaSnackOutputPort = ventaSnackOutputPort;
    }

    @Override
    public List<VentaSnack> listarVentasSnacks(FiltroVentaSnackDTO filtro) {
        return ventaSnackOutputPort.listarVentasSnacks(filtro);
    }

    @Override
    public VentaSnack obtenerVentaSnackPorId(UUID ventaSnackId) {
        VentaSnack ventaSnack = ventaSnackOutputPort.obtenerVentaSnackPorId(ventaSnackId);
        if (ventaSnack == null) {
            throw new IllegalArgumentException("La venta de snack no existe");
        }
        return ventaSnack;
    }
}
