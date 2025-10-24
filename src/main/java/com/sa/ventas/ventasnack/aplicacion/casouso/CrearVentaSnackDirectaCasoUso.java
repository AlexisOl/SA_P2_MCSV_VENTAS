package com.sa.ventas.ventasnack.aplicacion.casouso;

import com.sa.ventas.ventasnack.aplicacion.dto.CrearVentaSnackDirectaDTO;
import com.sa.ventas.ventasnack.aplicacion.puertos.entrada.CrearVentaSnackDirectaInputPort;
import com.sa.ventas.ventasnack.aplicacion.puertos.salida.VentaSnackOutputPort;
import com.sa.ventas.ventasnack.aplicacion.puertos.salida.eventos.VerificarSnackOutputPort;
import com.sa.ventas.ventasnack.dominio.VentaSnack;
import com.sa.ventas.ventasnack.infraestructura.eventos.dto.SnackDTO;
import com.sa.ventas.ventas.aplicacion.puertos.salida.eventos.VerificarUsuarioOutputPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class CrearVentaSnackDirectaCasoUso implements CrearVentaSnackDirectaInputPort {

    private final VentaSnackOutputPort ventaSnackOutputPort;
    private final VerificarSnackOutputPort verificarSnackOutputPort;
    private final VerificarUsuarioOutputPort verificarUsuarioOutputPort;

    public CrearVentaSnackDirectaCasoUso(VentaSnackOutputPort ventaSnackOutputPort,
                                         VerificarSnackOutputPort verificarSnackOutputPort,
                                         VerificarUsuarioOutputPort verificarUsuarioOutputPort) {
        this.ventaSnackOutputPort = ventaSnackOutputPort;
        this.verificarSnackOutputPort = verificarSnackOutputPort;
        this.verificarUsuarioOutputPort = verificarUsuarioOutputPort;
    }

    @Override
    @Transactional
    public List<VentaSnack> crearVentaSnackDirecta(CrearVentaSnackDirectaDTO dto) {
        // Verificar usuario
        boolean usuarioExiste = verificarUsuarioExistente(dto.getUsuarioId());
        if (!usuarioExiste) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        List<VentaSnack> ventasSnacks = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : dto.getSnacks().entrySet()) {
            UUID snackId = entry.getKey();
            Integer cantidad = entry.getValue();

            // Verificar y obtener precio del snack
            SnackDTO snack = verificarSnackExistente(snackId);
            if (snack == null) {
                throw new IllegalArgumentException("El snack con ID " + snackId + " no existe");
            }

            VentaSnack ventaSnack = new VentaSnack(
                    UUID.randomUUID(),
                    null, // Sin ventaId porque es venta directa
                    snackId,
                    cantidad,
                    snack.getPrecio(),
                    dto.getUsuarioId()
            );
            ventasSnacks.add(ventaSnack);
        }

        return ventaSnackOutputPort.crearVentasSnacks(ventasSnacks);
    }

    private boolean verificarUsuarioExistente(UUID idUsuario) {
        try {
            return verificarUsuarioOutputPort.verificarUsuario(idUsuario).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error al verificar usuario", e);
        }
    }

    private SnackDTO verificarSnackExistente(UUID snackId) {
        try {
            return verificarSnackOutputPort.obtenerSnack(snackId).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error al verificar snack", e);
        }
    }

}
