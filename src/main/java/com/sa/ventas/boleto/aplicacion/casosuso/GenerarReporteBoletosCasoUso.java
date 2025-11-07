package com.sa.ventas.boleto.aplicacion.casosuso;

import com.sa.ventas.asiento.aplicacion.puertos.salida.AsientoOutputPort;
import com.sa.ventas.asiento.dominio.Asiento;
import com.sa.ventas.boleto.aplicacion.dto.BoletoConDetalles;
import com.sa.ventas.boleto.aplicacion.puertos.entrada.GenerarReporteBoletosInputPort;
import com.sa.ventas.boleto.dominio.Boleto;
import com.sa.ventas.boleto.infraestructura.entrada.rest.dto.ReporteBoletosSalaDTO;
import com.sa.ventas.boleto.infraestructura.entrada.rest.dto.ReporteBoletosGeneralDTO;
import com.sa.ventas.boleto.infraestructura.entrada.rest.dto.UsuarioCompradorDTO;
import com.sa.ventas.boleto.infraestructura.salida.repositorio.BoletoRepository;
import com.sa.ventas.ventas.aplicacion.puertos.salida.ListarVentasOutputPort;
import com.sa.ventas.ventas.dominio.Venta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenerarReporteBoletosCasoUso implements GenerarReporteBoletosInputPort {

    private final BoletoRepository boletoRepository;
    private final AsientoOutputPort asientoOutputPort;
    private final ListarVentasOutputPort listarVentasOutputPort;

    @Override
    public ReporteBoletosGeneralDTO generarReporte(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            UUID salaId) {

        // Obtener todos los boletos en el rango
        var boletosEntities = boletoRepository.findBoletosParaReporte(
                fechaInicio, fechaFin, salaId);

        if (boletosEntities.isEmpty()) {
            return ReporteBoletosGeneralDTO.builder()
                    .fechaInicio(fechaInicio)
                    .fechaFin(fechaFin)
                    .totalBoletosVendidos(0)
                    .totalDineroRecaudado(0.0)
                    .detallesPorSala(Collections.emptyList())
                    .build();
        }

        // Convertir a dominio y obtener asientos
        List<UUID> asientoIds = boletosEntities.stream()
                .map(b -> b.getAsientoId())
                .distinct()
                .collect(Collectors.toList());

        List<Asiento> asientos = asientoOutputPort.obtenerAsientosPorIds(asientoIds);
        Map<UUID, Asiento> asientosMap = asientos.stream()
                .collect(Collectors.toMap(Asiento::getAsientoId, a -> a));

        // Obtener ventas únicas
        List<UUID> ventaIds = boletosEntities.stream()
                .map(b -> b.getVentaId())
                .distinct()
                .collect(Collectors.toList());

        Map<UUID, Venta> ventasMap = new HashMap<>();
        for (UUID ventaId : ventaIds) {
            Venta venta = listarVentasOutputPort.obtenerVentaPorId(ventaId);
            if (venta != null) {
                ventasMap.put(ventaId, venta);
            }
        }

        // Agrupar boletos por sala
        Map<UUID, List<BoletoConDetalles>> boletosPorSala = new HashMap<>();

        for (var boletoEntity : boletosEntities) {
            Asiento asiento = asientosMap.get(boletoEntity.getAsientoId());
            Venta venta = ventasMap.get(boletoEntity.getVentaId());

            if (asiento != null && venta != null) {
                UUID salaIdActual = asiento.getSalaId();

                BoletoConDetalles boletoDetalle = new BoletoConDetalles(
                        boletoEntity.getBoletoId(),
                        boletoEntity.getVentaId(),
                        boletoEntity.getPrecio(),
                        venta.getUsuarioId(),
                        salaIdActual
                );

                boletosPorSala
                        .computeIfAbsent(salaIdActual, k -> new ArrayList<>())
                        .add(boletoDetalle);
            }
        }

        // Generar detalles por sala
        List<ReporteBoletosSalaDTO> detallesSalas = boletosPorSala.entrySet().stream()
                .map(entry -> generarDetalleSala(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // 6. Calcular totales generales
        int totalBoletos = boletosEntities.size();
        double totalDinero = boletosEntities.stream()
                .mapToDouble(b -> b.getPrecio())
                .sum();

        return ReporteBoletosGeneralDTO.builder()
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .totalBoletosVendidos(totalBoletos)
                .totalDineroRecaudado(totalDinero)
                .detallesPorSala(detallesSalas)
                .build();
    }

    private ReporteBoletosSalaDTO generarDetalleSala(
            UUID salaId,
            List<BoletoConDetalles> boletos) {

        // Agrupar por usuario
        Map<UUID, List<BoletoConDetalles>> boletosPorUsuario = boletos.stream()
                .collect(Collectors.groupingBy(BoletoConDetalles::getUsuarioId));

        // Crear DTOs de usuarios
        List<UsuarioCompradorDTO> usuarios = boletosPorUsuario.entrySet().stream()
                .map(entry -> {
                    UUID usuarioId = entry.getKey();
                    List<BoletoConDetalles> boletosUsuario = entry.getValue();

                    int cantidadBoletos = boletosUsuario.size();
                    double totalGastado = boletosUsuario.stream()
                            .mapToDouble(BoletoConDetalles::getPrecio)
                            .sum();

                    return UsuarioCompradorDTO.builder()
                            .usuarioId(usuarioId)
                            .cantidadBoletosComprados(cantidadBoletos)
                            .totalGastado(totalGastado)
                            .build();
                })
                .sorted(Comparator.comparing(UsuarioCompradorDTO::getTotalGastado).reversed())
                .collect(Collectors.toList());

        // Totales de la sala
        int totalBoletosSala = boletos.size();
        double totalDineroSala = boletos.stream()
                .mapToDouble(BoletoConDetalles::getPrecio)
                .sum();

        return ReporteBoletosSalaDTO.builder()
                .salaId(salaId)
                .totalBoletos(totalBoletosSala)
                .totalDinero(totalDineroSala)
                .usuarios(usuarios)
                .build();
    }

}
