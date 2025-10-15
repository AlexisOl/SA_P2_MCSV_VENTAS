package com.sa.ventas.ventas.aplicacion.puertos.salida.eventos;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface VerificarUsuarioOutputPort {
    CompletableFuture<Boolean> verificarUsuario(UUID idUsuario);
}
