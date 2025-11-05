package com.sa.ventas.ventas.aplicacion.puertos.salida.eventos;

import com.example.comun.DTO.promocion.PromocionDTO;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ConsultarPromocionOutputPort {

    CompletableFuture<PromocionDTO> obtenerMejorPromocion(
            UUID cineId,
            UUID salaId,
            UUID peliculaId,
            UUID clienteId,
            String tipo  // BOLETOS, SNACKS, AMBOS
    );
}
