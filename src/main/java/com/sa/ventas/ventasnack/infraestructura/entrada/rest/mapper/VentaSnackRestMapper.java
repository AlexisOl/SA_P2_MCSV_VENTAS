package com.sa.ventas.ventasnack.infraestructura.entrada.rest.mapper;

import com.sa.ventas.ventasnack.dominio.VentaSnack;
import com.sa.ventas.ventasnack.infraestructura.entrada.rest.dto.ResponseVentaSnackDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VentaSnackRestMapper {

    ResponseVentaSnackDTO toResponseVentaSnackDto(VentaSnack ventaSnack);
    List<ResponseVentaSnackDTO> toResponseVentaSnacksDto(List<VentaSnack> ventasSnacks);
}
