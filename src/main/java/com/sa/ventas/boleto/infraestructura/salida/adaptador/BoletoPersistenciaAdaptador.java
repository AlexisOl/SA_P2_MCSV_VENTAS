package com.sa.ventas.boleto.infraestructura.salida.adaptador;

import com.sa.ventas.boleto.aplicacion.puertos.salida.BoletoOutputPort;
import com.sa.ventas.boleto.aplicacion.puertos.salida.ListarBoletosOutputPort;
import com.sa.ventas.boleto.dominio.Boleto;
import com.sa.ventas.boleto.infraestructura.salida.mapper.BoletoMapper;
import com.sa.ventas.boleto.infraestructura.salida.repositorio.BoletoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BoletoPersistenciaAdaptador implements BoletoOutputPort, ListarBoletosOutputPort {

    private final BoletoRepository boletoRepository;
    private final BoletoMapper boletoMapper;

    @Override
    @Transactional
    public Boleto crearBoleto(Boleto boleto) {
        return boletoMapper.toBoleto(
                boletoRepository.save(boletoMapper.toEntity(boleto))
        );
    }

    @Override
    @Transactional
    public List<Boleto> crearBoletos(List<Boleto> boletos) {
        return boletoMapper.toBoletoList(
                boletoRepository.saveAll(
                        boletos.stream()
                                .map(boletoMapper::toEntity)
                                .collect(Collectors.toList())
                )
        );
    }

    @Override
    public List<Boleto> obtenerBoletosPorVenta(UUID idVenta) {
        return boletoMapper.toBoletoList(
                boletoRepository.findByVentaId(idVenta)
        );
    }

    @Override
    public List<Boleto> listarBoletosPorVenta(UUID idVenta) {
        return boletoMapper.toBoletoList(
                boletoRepository.findByVentaId(idVenta)
        );
    }

    @Override
    public Boleto obtenerBoletoPorCodigo(String codigoBoleto) {
        return boletoMapper.toBoleto(
                boletoRepository.findByCodigoBoleto(codigoBoleto)
        );
    }
}
