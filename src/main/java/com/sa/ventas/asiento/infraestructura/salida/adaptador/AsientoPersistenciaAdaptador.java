package com.sa.ventas.asiento.infraestructura.salida.adaptador;

import com.sa.ventas.asiento.aplicacion.puertos.salida.AsientoOutputPort;

import com.sa.ventas.asiento.aplicacion.puertos.salida.CrearAsientoOutputPort;
import com.sa.ventas.asiento.aplicacion.puertos.salida.ListarAsientosOutputPort;
import com.sa.ventas.asiento.dominio.Asiento;
import com.sa.ventas.asiento.infraestructura.salida.mapper.AsientoMapper;
import com.sa.ventas.asiento.infraestructura.salida.repositorio.AsientoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AsientoPersistenciaAdaptador implements
        AsientoOutputPort,
        CrearAsientoOutputPort,
        ListarAsientosOutputPort {

    private final AsientoRepository asientoRepository;
    private final AsientoMapper asientoMapper;

    @Override
    @Transactional
    public Asiento crearAsiento(Asiento asiento) {
        return asientoMapper.toAsiento(
                asientoRepository.save(asientoMapper.toEntity(asiento))
        );
    }

    @Override
    public List<Asiento> listarAsientosPorSala(UUID salaId, UUID funcionId) {
        return asientoMapper.toAsientoList(
                asientoRepository.findBySalaIdAndFuncionId(salaId, funcionId)
        );
    }

    @Override
    public Asiento obtenerAsientoPorId(UUID id) {
        return asientoRepository.findById(id)
                .map(asientoMapper::toAsiento)
                .orElse(null);
    }

    @Override
    public List<Asiento> obtenerAsientosPorIds(List<UUID> ids) {
        return asientoMapper.toAsientoList(
                asientoRepository.findAllById(ids)
        );
    }

    @Override
    @Transactional
    public void reservarAsientos(List<UUID> ids) {
        asientoRepository.reservarAsientos(ids);
    }

    @Override
    @Transactional
    public void liberarAsientos(List<UUID> ids) {
        asientoRepository.liberarAsientos(ids);
    }

    @Override
    public boolean verificarDisponibilidad(List<UUID> ids) {
        return asientoRepository.todosDisponibles(ids, ids.size());
    }
}
