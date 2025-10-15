package com.sa.ventas.asiento.aplicacion.casouso;

import com.sa.ventas.asiento.aplicacion.puertos.entrada.ListarAsientosInputPort;

import com.sa.ventas.asiento.aplicacion.puertos.salida.ListarAsientosOutputPort;
import com.sa.ventas.asiento.dominio.Asiento;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListarAsientosUseCase implements ListarAsientosInputPort {

    private final ListarAsientosOutputPort listarAsientosOutputPort;

    public ListarAsientosUseCase(ListarAsientosOutputPort listarAsientosOutputPort) {
        this.listarAsientosOutputPort = listarAsientosOutputPort;
    }

    @Override
    public List<Asiento> listarAsientosPorSala(UUID idSala) {
        return listarAsientosOutputPort.listarAsientosPorSala(idSala);
    }

    @Override
    public Asiento obtenerAsientoPorId(UUID id) {
        Asiento asiento = listarAsientosOutputPort.obtenerAsientoPorId(id);
        if (asiento == null) {
            throw new IllegalArgumentException("El asiento no existe");
        }
        return asiento;
    }
}
