package com.sa.ventas.ventas.aplicacion.casouso;

import com.sa.ventas.ventas.aplicacion.dto.EditarVentaDTO;
import com.sa.ventas.ventas.aplicacion.puertos.entrada.EditarVentaInputPort;

import com.sa.ventas.ventas.aplicacion.puertos.salida.EditarVentaOutputPort;
import com.sa.ventas.ventas.dominio.Venta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EditarVentaCasoUso implements EditarVentaInputPort {

    private final EditarVentaOutputPort editarVentaOutputPort;

    public EditarVentaCasoUso(EditarVentaOutputPort editarVentaOutputPort) {
        this.editarVentaOutputPort = editarVentaOutputPort;
    }


    @Override
    @Transactional
    public Venta editarVenta(UUID id, EditarVentaDTO editarVentaDTO) {
        Venta ventaExistente = editarVentaOutputPort.obtenerVentaPorId(id);

        if (ventaExistente == null) {
            throw new IllegalArgumentException("La venta no existe");
        }

        if (editarVentaDTO.getIdFuncion() != null) {
            ventaExistente.setFuncionId(editarVentaDTO.getIdFuncion());
        }

        if (editarVentaDTO.getMontoTotal() != null) {
            ventaExistente.setMontoTotal(editarVentaDTO.getMontoTotal());
        }

        return editarVentaOutputPort.editarVenta(ventaExistente);
    }
}
