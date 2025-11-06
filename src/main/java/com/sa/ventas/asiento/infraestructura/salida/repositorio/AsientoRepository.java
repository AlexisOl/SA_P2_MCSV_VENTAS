package com.sa.ventas.asiento.infraestructura.salida.repositorio;

import com.sa.ventas.asiento.infraestructura.salida.entidades.AsientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AsientoRepository extends JpaRepository<AsientoEntity, UUID> {

    List<AsientoEntity> findBySalaId(UUID salaId);

    @Query("SELECT COUNT(a) = :cantidad FROM AsientoEntity a WHERE a.asientoId IN :ids AND a.disponible = true")
    boolean todosDisponibles(@Param("ids") List<UUID> ids, @Param("cantidad") long cantidad);

    @Modifying
    @Query("UPDATE AsientoEntity a SET a.disponible = false WHERE a.asientoId IN :ids")
    void reservarAsientos(@Param("ids") List<UUID> ids);

    @Modifying
    @Query("UPDATE AsientoEntity a SET a.disponible = true WHERE a.asientoId IN :ids")
    void liberarAsientos(@Param("ids") List<UUID> ids);
}
