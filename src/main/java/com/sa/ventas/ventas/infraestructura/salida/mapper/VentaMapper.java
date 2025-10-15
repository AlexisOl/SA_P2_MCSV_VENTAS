package com.sa.ventas.ventas.infraestructura.salida.mapper;

import com.sa.ventas.ventas.dominio.Venta;
import com.sa.ventas.ventas.infraestructura.salida.entidades.VentaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VentaMapper {

    VentaEntity toEntity(Venta venta);
    Venta toVenta(VentaEntity ventaEntity);
    List<Venta> toVentaList(List<VentaEntity> ventaEntities);
    List<VentaEntity> toVentaEntityList(List<Venta> ventas);
}
