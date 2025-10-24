package com.sa.ventas.ventasnack.infraestructura.salida.mapper;

import com.sa.ventas.ventasnack.dominio.VentaSnack;
import com.sa.ventas.ventasnack.infraestructura.salida.entidades.VentaSnackEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VentaSnackMapper {

    VentaSnackEntity toEntity(VentaSnack ventaSnack);
    VentaSnack toVentaSnack(VentaSnackEntity ventaSnackEntity);
    List<VentaSnack> toVentaSnackList(List<VentaSnackEntity> ventaSnackEntities);
    List<VentaSnackEntity> toVentaSnackEntityList(List<VentaSnack> ventasSnacks);
}
