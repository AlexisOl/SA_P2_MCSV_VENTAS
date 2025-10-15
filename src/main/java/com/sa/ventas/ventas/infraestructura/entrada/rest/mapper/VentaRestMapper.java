package com.sa.ventas.ventas.infraestructura.entrada.rest.mapper;

import com.sa.ventas.ventas.dominio.Venta;
import com.sa.ventas.ventas.infraestructura.entrada.rest.dto.ResponseVentaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VentaRestMapper {

    ResponseVentaDTO toResponseVentaDto(Venta venta);
    List<ResponseVentaDTO> toResponseVentasDto(List<Venta> ventas);
}
