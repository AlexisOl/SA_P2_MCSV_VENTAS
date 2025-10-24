package com.sa.ventas.ventasnack.infraestructura.salida.repositorio;

import com.sa.ventas.ventasnack.infraestructura.salida.entidades.VentaSnackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface VentaSnackRepository extends JpaRepository<VentaSnackEntity, UUID> {

    List<VentaSnackEntity> findByVentaId(UUID ventaId);
    List<VentaSnackEntity> findByUsuarioId(UUID usuarioId);
    List<VentaSnackEntity> findBySnackId(UUID snackId);

    @Query("SELECT vs FROM VentaSnackEntity vs WHERE " +
            "(:ventaSnackId IS NULL OR vs.ventaSnackId = :ventaSnackId) AND " +
            "(:ventaId IS NULL OR vs.ventaId = :ventaId) AND " +
            "(:snackId IS NULL OR vs.snackId = :snackId) AND " +
            "(:usuarioId IS NULL OR vs.usuarioId = :usuarioId) AND " +
            "(:fechaInicio IS NULL OR vs.fechaVenta >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR vs.fechaVenta <= :fechaFin)")
    List<VentaSnackEntity> buscarConFiltros(
            @Param("ventaSnackId") UUID ventaSnackId,
            @Param("ventaId") UUID ventaId,
            @Param("snackId") UUID snackId,
            @Param("usuarioId") UUID usuarioId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}
