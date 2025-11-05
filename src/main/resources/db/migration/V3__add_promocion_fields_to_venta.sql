ALTER TABLE venta
    ADD COLUMN promocion_aplicada_id CHAR(36) NULL,
ADD COLUMN monto_descuento DOUBLE NULL DEFAULT 0,
ADD COLUMN porcentaje_descuento DOUBLE NULL DEFAULT 0;

CREATE INDEX idx_venta_promocion ON venta(promocion_aplicada_id);