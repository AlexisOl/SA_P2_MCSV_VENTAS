package com.sa.ventas.ventas.infraestructura.salida.repositorio;

import com.sa.ventas.ventas.dominio.objeto_valor.EstadoVenta;
import com.sa.ventas.ventas.infraestructura.salida.entidades.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, UUID> {

    List<VentaEntity> findByUsuarioId(UUID usuarioId);
    List<VentaEntity> findByFuncionId(UUID funcionId);
    List<VentaEntity> findByEstado(EstadoVenta estado);

    @Query("SELECT v FROM VentaEntity v WHERE " +
            "(:idUsuario IS NULL OR v.usuarioId = :usuarioId) AND " +
            "(:idFuncion IS NULL OR v.funcionId = :funcionId) AND " +
            "(:estado IS NULL OR v.estado = :estado) AND " +
            "(:fechaInicio IS NULL OR v.fechaVenta >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR v.fechaVenta <= :fechaFin)")
    List<VentaEntity> buscarConFiltros(
            @Param("usuarioId") UUID usuarioId,
            @Param("funcionId") UUID funcionId,
            @Param("estado") EstadoVenta estado,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}
