package com.sa.ventas.boleto.infraestructura.salida.repositorio;

import com.sa.ventas.boleto.infraestructura.salida.entidades.BoletoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoletoRepository extends JpaRepository<BoletoEntity, UUID> {

    List<BoletoEntity> findByVentaId(UUID ventaId);
    BoletoEntity findByCodigoBoleto(String codigoBoleto);
}
