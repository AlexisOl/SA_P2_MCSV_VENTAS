package com.sa.ventas.asiento.infraestructura.salida.mapper;

import com.sa.ventas.asiento.dominio.Asiento;
import com.sa.ventas.asiento.infraestructura.salida.entidades.AsientoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AsientoMapper {

    AsientoEntity toEntity(Asiento asiento);
    Asiento toAsiento(AsientoEntity asientoEntity);
    List<Asiento> toAsientoList(List<AsientoEntity> asientoEntities);
    List<AsientoEntity> toAsientoEntityList(List<Asiento> asientos);
}
