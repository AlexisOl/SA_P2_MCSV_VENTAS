-- Tabla: venta_snack
CREATE TABLE venta_snack (
                             venta_snack_id CHAR(36) NOT NULL,
                             venta_id CHAR(36) NULL,
                             snack_id CHAR(36) NOT NULL,
                             cantidad INT NOT NULL,
                             precio_unitario DOUBLE NOT NULL,
                             subtotal DOUBLE NOT NULL,
                             usuario_id CHAR(36) NULL,
                             fecha_venta DATETIME NOT NULL,
                             PRIMARY KEY (venta_snack_id),
                             CONSTRAINT fk_venta_snack_venta FOREIGN KEY (venta_id)
                                 REFERENCES venta(venta_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_venta_snack_venta ON venta_snack(venta_id);
CREATE INDEX idx_venta_snack_snack ON venta_snack(snack_id);
CREATE INDEX idx_venta_snack_usuario ON venta_snack(usuario_id);
CREATE INDEX idx_venta_snack_fecha ON venta_snack(fecha_venta);

ALTER TABLE venta_snack COMMENT = 'Tabla de ventas de snacks (asociadas a ventas o independientes)';