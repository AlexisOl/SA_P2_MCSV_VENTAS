package com.sa.ventas.boleto.aplicacion.casosuso;

import com.example.comun.DTO.boletos.HorarioDTO;
import com.example.comun.DTO.boletos.PeliculaDTO;
import com.sa.ventas.asiento.aplicacion.puertos.salida.AsientoOutputPort;
import com.sa.ventas.asiento.dominio.Asiento;
import com.sa.ventas.boleto.aplicacion.puertos.entrada.ListarBoletosInputPort;

import com.sa.ventas.boleto.aplicacion.puertos.salida.ListarBoletosOutputPort;
import com.sa.ventas.boleto.aplicacion.puertos.salida.eventos.ConsultarHorarioOutputPort;
import com.sa.ventas.boleto.aplicacion.puertos.salida.eventos.ConsultarPeliculaOutputPort;
import com.sa.ventas.boleto.dominio.Boleto;
import com.sa.ventas.boleto.infraestructura.entrada.rest.dto.ResponseBoletoDetalladoDTO;
import com.sa.ventas.ventas.aplicacion.puertos.salida.ListarVentasOutputPort;
import com.sa.ventas.ventas.dominio.Venta;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ListarBoletosCasoUso implements ListarBoletosInputPort {

    private final ListarBoletosOutputPort listarBoletosOutputPort;
    private final AsientoOutputPort asientoOutputPort;
    private final ListarVentasOutputPort listarVentasOutputPort;
    private final ConsultarPeliculaOutputPort consultarPeliculaOutputPort;
    private final ConsultarHorarioOutputPort consultarHorarioOutputPort;

    public ListarBoletosCasoUso(ListarBoletosOutputPort listarBoletosOutputPort, AsientoOutputPort asientoOutputPort,
                                ListarVentasOutputPort listarVentasOutputPort, ConsultarPeliculaOutputPort consultarPeliculaOutputPort, ConsultarHorarioOutputPort consultarHorarioOutputPort) {
        this.listarBoletosOutputPort = listarBoletosOutputPort;
        this.asientoOutputPort = asientoOutputPort;
        this.listarVentasOutputPort = listarVentasOutputPort;
        this.consultarPeliculaOutputPort = consultarPeliculaOutputPort;
        this.consultarHorarioOutputPort = consultarHorarioOutputPort;
    }


    @Override
    public List<ResponseBoletoDetalladoDTO> listarBoletosPorVenta(UUID ventaId) {
        List<Boleto> boletos = listarBoletosOutputPort.listarBoletosPorVenta(ventaId);
        if (boletos.isEmpty()) {
            return Collections.emptyList();
        }

        // Obtener la venta
        Venta venta = listarVentasOutputPort.obtenerVentaPorId(ventaId);
        if (venta == null) {
            throw new IllegalArgumentException("Venta no encontrada");
        }

        // Obtener asientos (batch)
        List<UUID> asientoIds = boletos.stream()
                .map(Boleto::getAsientoId)
                .collect(Collectors.toList());
        List<Asiento> asientos = asientoOutputPort.obtenerAsientosPorIds(asientoIds);
        Map<UUID, Asiento> asientosMap = asientos.stream()
                .collect(Collectors.toMap(Asiento::getAsientoId, a -> a));

        //  Consultar horario (una sola vez por venta)
        HorarioDTO horario;
        try {
            horario = consultarHorarioOutputPort.obtenerHorario(venta.getFuncionId())
                    .get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Error consultando horario", e);
        }

        //  Consultar película (una sola vez)
        PeliculaDTO pelicula;
        try {
            pelicula = consultarPeliculaOutputPort.obtenerPelicula(horario.getPeliculaId())
                    .get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Error consultando película", e);
        }

        // 6. Mapear todos los boletos
        return boletos.stream()
                .map(boleto -> {
                    Asiento asiento = asientosMap.get(boleto.getAsientoId());
                    return ResponseBoletoDetalladoDTO.builder()
                            .boletoId(boleto.getBoletoId())
                            .ventaId(boleto.getVentaId())
                            .asientoId(boleto.getAsientoId())
                            .precio(boleto.getPrecio())
                            .codigoBoleto(boleto.getCodigoBoleto())
                            .fila(asiento != null ? asiento.getFila() : null)
                            .columna(asiento != null ? asiento.getColumna() : null)
                            .salaId(asiento != null ? asiento.getSalaId() : null)
                            .peliculaId(pelicula.getPeliculaId())
                            .peliculaTitulo(pelicula.getTitulo())
                            .peliculaDuracion(pelicula.getDuracion())
                            .funcionId(horario.getFuncionId())
                            .fechaHoraInicio(horario.getFechaInicio())
                            .fechaHoraFin(horario.getFechaFin())
                            .idioma(horario.getIdioma())
                            .formato(horario.getFormato())
                            .build();
                })
                .collect(Collectors.toList());
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
