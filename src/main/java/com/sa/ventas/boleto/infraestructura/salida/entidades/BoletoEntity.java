package com.sa.ventas.boleto.infraestructura.salida.entidades;

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
@Table(name = "boleto")
public class BoletoEntity {

    @Id
    @Column(name = "boleto_id", updatable = false, nullable = false)
    private UUID boletoId;

    @Column(name = "venta_id", nullable = false)
    private UUID ventaId;

    @Column(name = "asiento_id", nullable = false)
    private UUID asientoId;

    @Column(nullable = false)
    private Double precio;

    @Column(name = "codigo_boleto", nullable = false, unique = true)
    private String codigoBoleto;
}
