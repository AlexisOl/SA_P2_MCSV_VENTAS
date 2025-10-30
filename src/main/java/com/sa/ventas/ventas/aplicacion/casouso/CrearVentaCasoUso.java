package com.sa.ventas.ventas.aplicacion.casouso;

import com.example.comun.DTO.PeticionSnackEspecifica.SnackDTO;
import com.sa.ventas.asiento.aplicacion.puertos.salida.AsientoOutputPort;
import com.sa.ventas.boleto.aplicacion.puertos.salida.BoletoOutputPort;
import com.sa.ventas.boleto.dominio.Boleto;
import com.sa.ventas.ventas.aplicacion.dto.CrearVentaDTO;
import com.sa.ventas.ventas.aplicacion.puertos.entrada.CrearVentaInputPort;
import com.sa.ventas.ventas.aplicacion.puertos.salida.CrearVentaOutputPort;
import com.sa.ventas.ventas.aplicacion.puertos.salida.eventos.*;
import com.sa.ventas.ventas.dominio.Venta;
import com.sa.ventas.ventas.dominio.objeto_valor.EstadoVenta;
import com.sa.ventas.ventasnack.aplicacion.puertos.salida.VentaSnackOutputPort;
import com.sa.ventas.ventasnack.aplicacion.puertos.salida.eventos.CrearFacturaSnacksDirecta;
import com.sa.ventas.ventasnack.aplicacion.puertos.salida.eventos.VerificarSnackOutputPort;
import com.sa.ventas.ventasnack.dominio.VentaSnack;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class CrearVentaCasoUso implements CrearVentaInputPort {

    private final CrearVentaOutputPort crearVentaOutputPort;
    private final BoletoOutputPort boletoOutputPort;
    private final AsientoOutputPort asientoOutputPort;
    private final VerificarUsuarioOutputPort verificarUsuarioOutputPort;
    private final VerificarFuncionOutputPort verificarFuncionOutputPort;
    private final NotificarVentaOutputPort notificarVentaOutputPort;
    private final VerificarSnackOutputPort verificarSnackOutputPort;
    private final VentaSnackOutputPort ventaSnackOutputPort;
    private final CrearFacturaBoleto facturaBoleto;
    private final CrearFacturaSnacksDirecta crearFacturaSnacksDirecta;

    public CrearVentaCasoUso(CrearVentaOutputPort crearVentaOutputPort,
                             BoletoOutputPort boletoOutputPort,
                             AsientoOutputPort asientoOutputPort,
                             VerificarUsuarioOutputPort verificarUsuarioOutputPort,
                             VerificarFuncionOutputPort verificarFuncionOutputPort,
                             NotificarVentaOutputPort notificarVentaOutputPort,
                             VerificarSnackOutputPort verificarSnackOutputPort,
                             VentaSnackOutputPort ventaSnackOutputPort,
                             CrearFacturaBoleto facturaBoleto,
                             CrearFacturaSnacksDirecta crearFacturaSnacksDirecta) {
        this.crearVentaOutputPort = crearVentaOutputPort;
        this.boletoOutputPort = boletoOutputPort;
        this.asientoOutputPort = asientoOutputPort;
        this.verificarUsuarioOutputPort = verificarUsuarioOutputPort;
        this.verificarFuncionOutputPort = verificarFuncionOutputPort;
        this.notificarVentaOutputPort = notificarVentaOutputPort;
        this.verificarSnackOutputPort = verificarSnackOutputPort;
        this.ventaSnackOutputPort = ventaSnackOutputPort;
        this.facturaBoleto = facturaBoleto;
        this.crearFacturaSnacksDirecta=crearFacturaSnacksDirecta;
    }


    @Override
    @Transactional
    public Venta crearVenta(CrearVentaDTO crearVentaDTO) {
        // Verificar usuario
//        boolean usuarioExiste = verificarUsuarioExistente(crearVentaDTO.getIdUsuario());
//        if (!usuarioExiste) {
//            throw new IllegalArgumentException("El usuario no existe");
//        }
//
//        // Verificar función
//        boolean funcionExiste = verificarFuncionExistente(crearVentaDTO.getIdFuncion());
//        if (!funcionExiste) {
//            throw new IllegalArgumentException("La función no existe");
//        }
//
        // Verificar disponibilidad de asientos
        if (!asientoOutputPort.verificarDisponibilidad(crearVentaDTO.getIdsAsientos())) {
            throw new IllegalStateException("Uno o más asientos no están disponibles");
        }


        UUID id = UUID.randomUUID();
        // Crear venta
        Venta nuevaVenta = new Venta(
                id,
                crearVentaDTO.getIdUsuario(),
                crearVentaDTO.getIdFuncion(),
                LocalDateTime.now(),
                crearVentaDTO.getMontoTotal(),
                EstadoVenta.PENDIENTE,
                crearVentaDTO.getIdsAsientos().size()
        );

        Venta ventaCreada = crearVentaOutputPort.crearVenta(nuevaVenta);

        // Reservar asientos
        asientoOutputPort.reservarAsientos(crearVentaDTO.getIdsAsientos());

        // Crear boletos
        List<Boleto> boletos = new ArrayList<>();
        double precioPorBoleto = crearVentaDTO.getMontoTotal() / crearVentaDTO.getIdsAsientos().size();

        for (UUID idAsiento : crearVentaDTO.getIdsAsientos()) {
            Boleto boleto = new Boleto(
                    UUID.randomUUID(),
                    ventaCreada.getVentaId(),
                    idAsiento,
                    precioPorBoleto,
                    generarCodigoBoleto()
            );
            boletos.add(boleto);
        }

        boletoOutputPort.crearBoletos(boletos);
        //aca deberia de enviar al evento
        //crear-factura-boleto
        this.facturaBoleto.crearFacturaBoleto(ventaCreada, crearVentaDTO.getIdCine());



        // Crear ventas de snacks si existen
        if (crearVentaDTO.getSnacks() != null && !crearVentaDTO.getSnacks().isEmpty()) {
            List<VentaSnack> ventasSnacks = new ArrayList<>();

            for (Map.Entry<UUID, Integer> entry : crearVentaDTO.getSnacks().entrySet()) {
                UUID snackId = entry.getKey();
                Integer cantidad = entry.getValue();

                // Verificar y obtener precio del snack desde el microservicio externo
                SnackDTO snack = verificarSnackExistente(snackId);
                if (snack == null) {
                    throw new IllegalArgumentException("El snack con ID " + snackId + " no existe");
                }

                VentaSnack ventaSnack = new VentaSnack(
                        UUID.randomUUID(),
                        ventaCreada.getVentaId(),
                        snackId,
                        cantidad,
                        snack.getPrecio(),
                        ventaCreada.getUsuarioId()
                );
                ventasSnacks.add(ventaSnack);
            }


            List<VentaSnack> ventasComida= ventaSnackOutputPort.crearVentasSnacks(ventasSnacks);

            // aca enviar al evento de facturacion de snacks
            //crea 1 por 1, entonces las que alcancen sino se da como estado cancelada

            // aca enviar al evento de facturacion de snacks
            //crea 1 por 1, entonces las que alcancen sino se da como estado cancelada
            this.crearFacturaSnacksDirecta.crearFacturaSnacksDirecto(ventasComida, crearVentaDTO.getIdCine() );

        }

        // Notificar venta creada
        notificarVentaOutputPort.notificarVentaCreada(ventaCreada);

        return ventaCreada;
    }

    private SnackDTO verificarSnackExistente(UUID snackId) {
        try {
            return verificarSnackOutputPort.obtenerSnack(snackId).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error al verificar snack", e);
        }
    }

    private boolean verificarUsuarioExistente(UUID idUsuario) {
        try {
            return verificarUsuarioOutputPort.verificarUsuario(idUsuario).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error al verificar usuario", e);
        }
    }

    private boolean verificarFuncionExistente(UUID idFuncion) {
        try {
            return verificarFuncionOutputPort.verificarFuncion(idFuncion).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error al verificar función", e);
        }
    }

    private String generarCodigoBoleto() {
        return "BOL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
