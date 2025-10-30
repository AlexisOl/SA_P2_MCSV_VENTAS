package com.sa.ventas.ventasnack.aplicacion.puertos.salida.eventos;



import com.example.comun.DTO.PeticionSnackEspecifica.SnackDTO;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
public interface VerificarSnackOutputPort {

    CompletableFuture<SnackDTO> obtenerSnack(UUID snackId);
}
