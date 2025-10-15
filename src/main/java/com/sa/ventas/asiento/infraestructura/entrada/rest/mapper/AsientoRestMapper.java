package com.sa.ventas.asiento.infraestructura.entrada.rest.mapper;

import com.sa.ventas.asiento.dominio.Asiento;
import com.sa.ventas.asiento.infraestructura.entrada.rest.dto.ResponseAsientoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AsientoRestMapper {
    ResponseAsientoDTO toResponseAsientoDto(Asiento asiento);
    List<ResponseAsientoDTO> toResponseAsientosDto(List<Asiento> asientos);
}
