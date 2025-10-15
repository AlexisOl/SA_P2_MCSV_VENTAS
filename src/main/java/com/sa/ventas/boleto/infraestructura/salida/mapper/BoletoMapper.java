package com.sa.ventas.boleto.infraestructura.salida.mapper;

import com.sa.ventas.boleto.dominio.Boleto;
import com.sa.ventas.boleto.infraestructura.salida.entidades.BoletoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoletoMapper {

    BoletoEntity toEntity(Boleto boleto);
    Boleto toBoleto(BoletoEntity boletoEntity);
    List<Boleto> toBoletoList(List<BoletoEntity> boletoEntities);
    List<BoletoEntity> toBoletoEntityList(List<Boleto> boletos);
}
