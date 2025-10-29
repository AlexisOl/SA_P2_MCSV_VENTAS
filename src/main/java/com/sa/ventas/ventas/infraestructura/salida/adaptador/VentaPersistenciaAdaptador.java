package com.sa.ventas.ventas.infraestructura.salida.adaptador;

import com.sa.ventas.ventas.aplicacion.dto.FiltroVentaDTO;
import com.sa.ventas.ventas.aplicacion.puertos.salida.*;
import com.sa.ventas.ventas.dominio.Venta;
import com.sa.ventas.ventas.dominio.objeto_valor.EstadoVenta;
import com.sa.ventas.ventas.infraestructura.salida.entidades.VentaEntity;
import com.sa.ventas.ventas.infraestructura.salida.mapper.VentaMapper;
import com.sa.ventas.ventas.infraestructura.salida.repositorio.VentaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class VentaPersistenciaAdaptador implements
        CrearVentaOutputPort,
        EditarVentaOutputPort,
        AnularVentaOutputPort,
        ListarVentasOutputPort,
        CambiarEstadoVenta {

    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;

    @Override
    @Transactional
    public Venta crearVenta(Venta venta) {
        return ventaMapper.toVenta(
                ventaRepository.save(ventaMapper.toEntity(venta))
        );
    }

    @Override
    @Transactional
    public Venta editarVenta(Venta venta) {
        return ventaMapper.toVenta(
                ventaRepository.save(ventaMapper.toEntity(venta))
        );
    }

    @Override
    public Venta obtenerVentaPorId(UUID id) {
        return ventaRepository.findById(id)
                .map(ventaMapper::toVenta)
                .orElse(null);
    }

    @Override
    @Transactional
    public void anularVenta(UUID id) {
        ventaRepository.findById(id).ifPresent(venta -> {
            venta.setEstado(EstadoVenta.ANULADA);
            ventaRepository.save(venta);
        });
    }

    @Override
    public List<Venta> listarVentas(FiltroVentaDTO filtro) {
        return ventaMapper.toVentaList(
                ventaRepository.buscarConFiltros(
                        filtro.getUsuarioId(),
                        filtro.getFuncionId(),
                        filtro.getEstado(),
                        filtro.getFechaInicio(),
                        filtro.getFechaFin()
                )
        );
    }

    @Override
    public void cambiarEstadoVenta(UUID id, EstadoVenta estadoVenta) {
        VentaEntity entidad = this.ventaRepository.findById(id).orElse(null);
        if (entidad == null) return;
        entidad.setEstado(estadoVenta);
        this.ventaRepository.save(entidad);
    }
}
