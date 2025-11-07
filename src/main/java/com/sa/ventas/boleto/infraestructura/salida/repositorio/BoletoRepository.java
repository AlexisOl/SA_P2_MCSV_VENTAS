package com.sa.ventas.boleto.infraestructura.salida.repositorio;

import com.sa.ventas.boleto.infraestructura.salida.entidades.BoletoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BoletoRepository extends JpaRepository<BoletoEntity, UUID> {

    List<BoletoEntity> findByVentaId(UUID ventaId);
    BoletoEntity findByCodigoBoleto(String codigoBoleto);

    // query para el reporte
    @Query("""
        SELECT b FROM BoletoEntity b
        JOIN VentaEntity v ON b.ventaId = v.ventaId
        JOIN AsientoEntity a ON b.asientoId = a.asientoId
        WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin
        AND v.estado = 'COMPLETADA'
        AND (:salaId IS NULL OR a.salaId = :salaId)
        ORDER BY a.salaId, v.usuarioId
    """)
    List<BoletoEntity> findBoletosParaReporte(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("salaId") UUID salaId
    );
}
