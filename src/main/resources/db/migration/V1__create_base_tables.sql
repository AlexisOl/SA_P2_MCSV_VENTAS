-- Migración para crear las tablas asiento, venta y boleto en MariaDB

-- Tabla: venta
CREATE TABLE venta (
                       venta_id CHAR(36) NOT NULL,
                       usuario_id CHAR(36) NOT NULL,
                       funcion_id CHAR(36) NOT NULL,
                       fecha_venta DATETIME NOT NULL,
                       monto_total DOUBLE NOT NULL,
                       estado VARCHAR(20) NOT NULL,
                       cantidad_boletos INT NOT NULL,
                       PRIMARY KEY (venta_id),
                       CONSTRAINT chk_estado_venta CHECK (estado IN ('PENDIENTE', 'COMPLETADA', 'ANULADA', 'REEMBOLSADA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para venta
CREATE INDEX idx_venta_usuario ON venta(usuario_id);
CREATE INDEX idx_venta_funcion ON venta(funcion_id);
CREATE INDEX idx_venta_fecha ON venta(fecha_venta);
CREATE INDEX idx_venta_estado ON venta(estado);

-- Tabla: asiento
CREATE TABLE asiento (
                         asiento_id CHAR(36) NOT NULL,
                         fila VARCHAR(10) NOT NULL,
                         columna INT NOT NULL,
                         sala_id CHAR(36) NOT NULL,
                         disponible BOOLEAN NOT NULL DEFAULT TRUE,
                         PRIMARY KEY (asiento_id),
                         UNIQUE KEY uk_asiento_sala_fila_columna (sala_id, fila, columna)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para asiento
CREATE INDEX idx_asiento_sala ON asiento(sala_id);
CREATE INDEX idx_asiento_disponible ON asiento(disponible);

-- Tabla: boleto
CREATE TABLE boleto (
                        boleto_id CHAR(36) NOT NULL,
                        venta_id CHAR(36) NOT NULL,
                        asiento_id CHAR(36) NOT NULL,
                        precio DOUBLE NOT NULL,
                        codigo_boleto VARCHAR(100) NOT NULL,
                        PRIMARY KEY (boleto_id),
                        UNIQUE KEY uk_codigo_boleto (codigo_boleto),
                        CONSTRAINT fk_boleto_venta FOREIGN KEY (venta_id) REFERENCES venta(venta_id) ON DELETE CASCADE,
                        CONSTRAINT fk_boleto_asiento FOREIGN KEY (asiento_id) REFERENCES asiento(asiento_id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para boleto
CREATE INDEX idx_boleto_venta ON boleto(venta_id);
CREATE INDEX idx_boleto_asiento ON boleto(asiento_id);

-- Comentarios de las tablas
ALTER TABLE venta COMMENT = 'Tabla de ventas de boletos';
ALTER TABLE asiento COMMENT = 'Tabla de asientos de las salas';
ALTER TABLE boleto COMMENT = 'Tabla de boletos vendidos';