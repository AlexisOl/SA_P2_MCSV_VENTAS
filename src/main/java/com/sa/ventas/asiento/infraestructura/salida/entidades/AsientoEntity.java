package com.sa.ventas.asiento.infraestructura.salida.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "asiento")
public class AsientoEntity {

    @Id
    @Column(name = "asiento_id", updatable = false, nullable = false)
    private UUID asientoId;

    @Column(nullable = false)
    private String fila;

    @Column(nullable = false)
    private Integer columna;

    @Column(name = "sala_id", nullable = false)
    private UUID salaId;

    @Column(nullable = false)
    private Boolean disponible;

    @Column(name = "funcion_id", nullable = false)
    private UUID funcionId;
}
