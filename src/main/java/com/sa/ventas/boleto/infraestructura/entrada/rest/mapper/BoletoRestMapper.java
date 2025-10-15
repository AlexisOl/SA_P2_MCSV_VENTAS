package com.sa.ventas.boleto.infraestructura.entrada.rest.mapper;

import com.sa.ventas.boleto.dominio.Boleto;
import com.sa.ventas.boleto.infraestructura.entrada.rest.dto.ResponseBoletoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoletoRestMapper {

    ResponseBoletoDTO toResponseBoletoDto(Boleto boleto);
    List<ResponseBoletoDTO> toResponseBoletosDto(List<Boleto> boletos);
}
