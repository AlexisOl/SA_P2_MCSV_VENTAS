package com.sa.ventas.boleto.aplicacion.puertos.salida.eventos;

import com.example.comun.DTO.boletos.PeliculaDTO;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ConsultarPeliculaOutputPort {

    CompletableFuture<PeliculaDTO> obtenerPelicula(UUID peliculaId);
}
