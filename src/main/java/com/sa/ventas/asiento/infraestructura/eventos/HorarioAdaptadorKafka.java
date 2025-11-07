package com.sa.ventas.asiento.infraestructura.eventos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.ventas.asiento.aplicacion.dto.CrearAsientoDTO;
import com.sa.ventas.asiento.aplicacion.puertos.entrada.CrearAsientoInputPort;
import com.sa.ventas.asiento.dominio.Asiento;
import com.sa.ventas.asiento.infraestructura.eventos.dto.NotificarHorarioDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class HorarioAdaptadorKafka {

    private final ObjectMapper objectMapper;
    private final CrearAsientoInputPort crearAsientoInputPort;


    public HorarioAdaptadorKafka(ObjectMapper objectMapper, CrearAsientoInputPort crearAsientoInputPort) {
        this.objectMapper = objectMapper;
        this.crearAsientoInputPort = crearAsientoInputPort;
    }

    @KafkaListener(topics = "horario-creada", groupId = "promocion-group")
    public void consumirEventoHorario(String mensaje) {
        try {
            NotificarHorarioDto evento = objectMapper.readValue(mensaje, NotificarHorarioDto.class);
            System.out.println(" Evento recibido: " + evento.getSalaId());
            procesarEvento(evento);

        } catch (Exception e) {
            System.err.println(" Error al procesar evento horario: " + e.getMessage());
        }
    }

    private void procesarEvento(NotificarHorarioDto evento) {
        System.out.println("Procesando horario creado para asientos " + evento.getSalaId());
        System.out.println("FILA: "+ evento.getFila() + " COLUMNA: " + evento.getColumna());
        int totalFilas = evento.getFila();
        int totalColumnas = evento.getColumna();

        for (int i = 0; i < totalFilas; i++) {

            char letraFila = (char) ('A' + i);

            for (int j = 1; j <= totalColumnas; j++) {
                //String nombreAsiento = letraFila + String.valueOf(j);
                //System.out.println("Creando asiento: " + nombreAsiento);

                CrearAsientoDTO asiento = new CrearAsientoDTO(
                        evento.getSalaId(),
                        evento.getFuncionId(),
                        String.valueOf(letraFila),
                       j
                );
                this.crearAsientoInputPort.crearAsiento(asiento);
            }
        }
    }
}
