package com.sa.ventas.ventas.aplicacion.puertos.salida.eventos;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface VerificarFuncionOutputPort {

    CompletableFuture<Boolean> verificarFuncion(UUID idFuncion);
}
