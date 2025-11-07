package com.sa.ventas.boleto.aplicacion.puertos.salida.eventos;

import com.example.comun.DTO.boletos.HorarioDTO;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ConsultarHorarioOutputPort {

    CompletableFuture<HorarioDTO> obtenerHorario(UUID funcionId);
}
