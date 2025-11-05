package com.sa.ventas.ventas.aplicacion.casouso;

import com.example.comun.DTO.PeticionSnackEspecifica.SnackDTO;
import com.example.comun.DTO.promocion.PromocionDTO;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private final ConsultarPromocionOutputPort consultarPromocionOutputPort;

    public CrearVentaCasoUso(CrearVentaOutputPort crearVentaOutputPort,
                             BoletoOutputPort boletoOutputPort,
                             AsientoOutputPort asientoOutputPort,
                             VerificarUsuarioOutputPort verificarUsuarioOutputPort,
                             VerificarFuncionOutputPort verificarFuncionOutputPort,
                             NotificarVentaOutputPort notificarVentaOutputPort,
                             VerificarSnackOutputPort verificarSnackOutputPort,
                             VentaSnackOutputPort ventaSnackOutputPort,
                             CrearFacturaBoleto facturaBoleto,
                             CrearFacturaSnacksDirecta crearFacturaSnacksDirecta,
                             ConsultarPromocionOutputPort consultarPromocionOutputPort) {
        this.crearVentaOutputPort = crearVentaOutputPort;
        this.boletoOutputPort = boletoOutputPort;
        this.asientoOutputPort = asientoOutputPort;
        this.verificarUsuarioOutputPort = verificarUsuarioOutputPort;
        this.verificarFuncionOutputPort = verificarFuncionOutputPort;
        this.notificarVentaOutputPort = notificarVentaOutputPort;
        this.verificarSnackOutputPort = verificarSnackOutputPort;
        this.ventaSnackOutputPort = ventaSnackOutputPort;
        this.facturaBoleto = facturaBoleto;
        this.crearFacturaSnacksDirecta = crearFacturaSnacksDirecta;
        this.consultarPromocionOutputPort = consultarPromocionOutputPort;
    }

    @Override
    @Transactional
    public Venta crearVenta(CrearVentaDTO crearVentaDTO) {
        // Verificar usuario
        boolean usuarioExiste = verificarUsuarioExistente(crearVentaDTO.getIdUsuario());
        if (!usuarioExiste) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        // Verificar función
        boolean funcionExiste = verificarFuncionExistente(crearVentaDTO.getIdFuncion());
        if (!funcionExiste) {
            throw new IllegalArgumentException("La función no existe");
        }

        // Verificar disponibilidad de asientos
        if (!asientoOutputPort.verificarDisponibilidad(crearVentaDTO.getIdsAsientos())) {
            throw new IllegalStateException("Uno o más asientos no están disponibles");
        }

        UUID ventaId = UUID.randomUUID();
        Double montoOriginal = crearVentaDTO.getMontoTotal();
        Venta nuevaVenta;

        // Consultar y aplicar promoción si está habilitado
        if (Boolean.TRUE.equals(crearVentaDTO.getAplicarPromocion())) {
            PromocionDTO promocion = consultarMejorPromocion(
                    crearVentaDTO.getIdCine(),
                    crearVentaDTO.getSalaId(),
                    crearVentaDTO.getPeliculaId(),
                    crearVentaDTO.getIdUsuario()
            );

            if (promocion != null && promocion.getExiste() && promocion.getPorcentajeDescuento() > 0) {
                System.out.println("Aplicando promoción: " + promocion.getNombre() +
                        " - Descuento: " + promocion.getPorcentajeDescuento() + "%");

                nuevaVenta = new Venta(
                        ventaId,
                        crearVentaDTO.getIdUsuario(),
                        crearVentaDTO.getIdFuncion(),
                        LocalDateTime.now(),
                        montoOriginal,
                        EstadoVenta.PENDIENTE,
                        crearVentaDTO.getIdsAsientos().size(),
                        promocion.getPromocionId(),
                        promocion.getPorcentajeDescuento()
                );
            } else {
                System.out.println("No se encontró promoción aplicable, creando venta sin descuento");
                nuevaVenta = crearVentaSinPromocion(ventaId, crearVentaDTO, montoOriginal);
            }
        } else {
            nuevaVenta = crearVentaSinPromocion(ventaId, crearVentaDTO, montoOriginal);
        }

        Venta ventaCreada = crearVentaOutputPort.crearVenta(nuevaVenta);

        // Reservar asientos
        asientoOutputPort.reservarAsientos(crearVentaDTO.getIdsAsientos());

        // Crear boletos con el precio ajustado si hay promoción
        crearBoletos(ventaCreada, crearVentaDTO.getIdsAsientos());

        // Crear factura de boletos
        this.facturaBoleto.crearFacturaBoleto(ventaCreada, crearVentaDTO.getIdCine());

        // Procesar snacks si existen
        procesarSnacks(crearVentaDTO, ventaCreada);

        // Notificar venta creada
        notificarVentaOutputPort.notificarVentaCreada(ventaCreada);

        return ventaCreada;
    }

    private Venta crearVentaSinPromocion(UUID ventaId, CrearVentaDTO dto, Double monto) {
        return new Venta(
                ventaId,
                dto.getIdUsuario(),
                dto.getIdFuncion(),
                LocalDateTime.now(),
                monto,
                EstadoVenta.PENDIENTE,
                dto.getIdsAsientos().size()
        );
    }

    private PromocionDTO consultarMejorPromocion(UUID cineId, UUID salaId, UUID peliculaId, UUID clienteId) {
        try {
            // Consultar promoción para boletos
            return consultarPromocionOutputPort.obtenerMejorPromocion(
                    cineId,
                    salaId,
                    peliculaId,
                    clienteId,
                    "BOLETOS"
            ).get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("Error al consultar promoción: " + e.getMessage());
            return null;
        }
    }

    private void crearBoletos(Venta venta, List<UUID> idsAsientos) {
        List<Boleto> boletos = new ArrayList<>();
        double precioPorBoleto = venta.getMontoTotal() / idsAsientos.size();

        for (UUID idAsiento : idsAsientos) {
            Boleto boleto = new Boleto(
                    UUID.randomUUID(),
                    venta.getVentaId(),
                    idAsiento,
                    precioPorBoleto,
                    generarCodigoBoleto()
            );
            boletos.add(boleto);
        }

        boletoOutputPort.crearBoletos(boletos);
    }

    private void procesarSnacks(CrearVentaDTO dto, Venta ventaCreada) {
        if (dto.getSnacks() != null && !dto.getSnacks().isEmpty()) {
            List<VentaSnack> ventasSnacks = new ArrayList<>();

            for (Map.Entry<UUID, Integer> entry : dto.getSnacks().entrySet()) {
                UUID snackId = entry.getKey();
                Integer cantidad = entry.getValue();

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

            List<VentaSnack> ventasComida = ventaSnackOutputPort.crearVentasSnacks(ventasSnacks);
            this.crearFacturaSnacksDirecta.crearFacturaSnacksDirecto(ventasComida, dto.getIdCine());
        }
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
