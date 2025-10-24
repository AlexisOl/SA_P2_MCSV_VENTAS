package com.sa.ventas.ventasnack.infraestructura.salida.adaptador;

import com.sa.ventas.ventasnack.aplicacion.dto.FiltroVentaSnackDTO;
import com.sa.ventas.ventasnack.aplicacion.puertos.salida.VentaSnackOutputPort;
import com.sa.ventas.ventasnack.dominio.VentaSnack;
import com.sa.ventas.ventasnack.infraestructura.salida.mapper.VentaSnackMapper;
import com.sa.ventas.ventasnack.infraestructura.salida.repositorio.VentaSnackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class VentaSnackPersistenciaAdaptador implements VentaSnackOutputPort {

    private final VentaSnackRepository ventaSnackRepository;
    private final VentaSnackMapper ventaSnackMapper;

    @Override
    @Transactional
    public VentaSnack crearVentaSnack(VentaSnack ventaSnack) {
        return ventaSnackMapper.toVentaSnack(
                ventaSnackRepository.save(ventaSnackMapper.toEntity(ventaSnack))
        );
    }

    @Override
    @Transactional
    public List<VentaSnack> crearVentasSnacks(List<VentaSnack> ventasSnacks) {
        return ventaSnackMapper.toVentaSnackList(
                ventaSnackRepository.saveAll(
                        ventasSnacks.stream()
                                .map(ventaSnackMapper::toEntity)
                                .collect(Collectors.toList())
                )
        );
    }

    @Override
    public List<VentaSnack> obtenerVentasSnacksPorVenta(UUID ventaId) {
        return ventaSnackMapper.toVentaSnackList(
                ventaSnackRepository.findByVentaId(ventaId)
        );
    }

    @Override
    public List<VentaSnack> listarVentasSnacks(FiltroVentaSnackDTO filtro) {
        return ventaSnackMapper.toVentaSnackList(
                ventaSnackRepository.buscarConFiltros(
                        filtro.getVentaSnackId(),
                        filtro.getVentaId(),
                        filtro.getSnackId(),
                        filtro.getUsuarioId(),
                        filtro.getFechaInicio(),
                        filtro.getFechaFin()
                )
        );
    }

    @Override
    public VentaSnack obtenerVentaSnackPorId(UUID ventaSnackId) {
        return ventaSnackRepository.findById(ventaSnackId)
                .map(ventaSnackMapper::toVentaSnack)
                .orElse(null);
    }
}
