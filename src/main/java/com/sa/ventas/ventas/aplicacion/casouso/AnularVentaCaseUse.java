package com.sa.ventas.ventas.aplicacion.casouso;

import com.sa.ventas.asiento.aplicacion.puertos.salida.AsientoOutputPort;
import com.sa.ventas.boleto.aplicacion.puertos.salida.BoletoOutputPort;
import com.sa.ventas.boleto.dominio.Boleto;
import com.sa.ventas.ventas.aplicacion.puertos.entrada.AnularVentaInputPort;

import com.sa.ventas.ventas.aplicacion.puertos.salida.AnularVentaOutputPort;
import com.sa.ventas.ventas.aplicacion.puertos.salida.EditarVentaOutputPort;
import com.sa.ventas.ventas.aplicacion.puertos.salida.eventos.NotificarVentaOutputPort;
import com.sa.ventas.ventas.dominio.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnularVentaCaseUse implements AnularVentaInputPort {

    private final AnularVentaOutputPort anularVentaOutputPort;
    private final EditarVentaOutputPort editarVentaOutputPort;
    private final BoletoOutputPort boletoOutputPort;
    private final AsientoOutputPort asientoOutputPort;
    private final NotificarVentaOutputPort notificarVentaOutputPort;

    @Autowired
    public AnularVentaCaseUse(AnularVentaOutputPort anularVentaOutputPort, EditarVentaOutputPort editarVentaOutputPort, BoletoOutputPort boletoOutputPort, AsientoOutputPort asientoOutputPort, NotificarVentaOutputPort notificarVentaOutputPort) {
        this.anularVentaOutputPort = anularVentaOutputPort;
        this.editarVentaOutputPort = editarVentaOutputPort;
        this.boletoOutputPort = boletoOutputPort;
        this.asientoOutputPort = asientoOutputPort;
        this.notificarVentaOutputPort = notificarVentaOutputPort;
    }

    @Override
    @Transactional
    public void anularVenta(UUID id) {
        Venta venta = editarVentaOutputPort.obtenerVentaPorId(id);

        if (venta == null) {
            throw new IllegalArgumentException("La venta no existe");
        }

        venta.anular();
        editarVentaOutputPort.editarVenta(venta);

        // Liberar asientos
        List<Boleto> boletos = boletoOutputPort.obtenerBoletosPorVenta(id);
        List<UUID> idsAsientos = boletos.stream()
                .map(Boleto::getAsientoId)
                .collect(Collectors.toList());

        asientoOutputPort.liberarAsientos(idsAsientos);

        anularVentaOutputPort.anularVenta(venta.getVentaId());

        // Notificar anulación
        notificarVentaOutputPort.notificarVentaAnulada(venta);
    }
}
