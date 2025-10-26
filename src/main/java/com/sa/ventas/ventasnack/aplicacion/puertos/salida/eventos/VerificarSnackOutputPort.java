package com.sa.ventas.ventasnack.aplicacion.puertos.salida.eventos;


import com.sa.ventas.ventasnack.infraestructura.eventos.dto.SnackDTO;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
public interface VerificarSnackOutputPort {

    CompletableFuture<SnackDTO> obtenerSnack(UUID snackId);
}
