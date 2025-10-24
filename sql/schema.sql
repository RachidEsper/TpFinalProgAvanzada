
DROP DATABASE IF EXISTS ventas_cosmeticos;
CREATE DATABASE ventas_cosmeticos
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
USE ventas_cosmeticos;

-- ============================================================================
-- CONVENIO DE AUDITORÍA
-- created_at: momento de creación del registro
-- updated_at: última modificación del registro
-- En Pedidos/DetallePedido se usan DATETIME para precisión de checkout y flujos de pago.
-- ============================================================================

-- 1) TipoUsuario
CREATE TABLE tipo_usuario (
  id_tipo_usuario INT NOT NULL AUTO_INCREMENT,
  descripcion     VARCHAR(100) NOT NULL,
  created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT pk_tipo_usuario PRIMARY KEY (id_tipo_usuario),
  CONSTRAINT uq_tipo_usuario_descripcion UNIQUE (descripcion)
) ENGINE=InnoDB;

-- 2) Usuario
CREATE TABLE usuario (
  id_usuario       INT NOT NULL AUTO_INCREMENT,
  nombre           VARCHAR(100) NOT NULL,
  apellido         VARCHAR(100) NOT NULL,
  email            VARCHAR(255) NOT NULL,
  contrasenia      VARCHAR(255) NOT NULL,
  telefono         VARCHAR(20),
  id_tipo_usuario  INT NOT NULL,
  created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
  CONSTRAINT uq_usuario_email UNIQUE (email),
  CONSTRAINT fk_usuario_tipo_usuario
    FOREIGN KEY (id_tipo_usuario)
    REFERENCES tipo_usuario (id_tipo_usuario)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX ix_usuario_tipo ON usuario (id_tipo_usuario);

-- 3) Categoria
CREATE TABLE categoria (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  nombre       VARCHAR(120) NOT NULL,
  descripcion  VARCHAR(500),
  created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT pk_categoria PRIMARY KEY (id_categoria),
  CONSTRAINT uq_categoria_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

-- 4) Producto (id VARCHAR por requerimiento)
CREATE TABLE producto (
  id_producto   VARCHAR(50) NOT NULL,
  nombre        VARCHAR(150) NOT NULL,
  descripcion   TEXT,
  precio        DECIMAL(12,2) NOT NULL,
  descuento     FLOAT NOT NULL DEFAULT 0,
  imagen_url    VARCHAR(255),
  id_categoria  INT NOT NULL,
  created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT pk_producto PRIMARY KEY (id_producto),
  CONSTRAINT fk_producto_categoria
    FOREIGN KEY (id_categoria)
    REFERENCES categoria (id_categoria)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT,
  CONSTRAINT ck_producto_descuento CHECK (descuento >= 0 AND descuento <= 100)
) ENGINE=InnoDB;

CREATE INDEX ix_producto_categoria ON producto (id_categoria);

-- 5) Pedido (DATETIME para fecha/created/updated)
CREATE TABLE pedido (
  id_pedido          INT NOT NULL AUTO_INCREMENT,
  fecha              DATETIME NOT NULL,                             -- precisión de checkout
  id_usuario         INT NOT NULL,
  estado             BOOLEAN NOT NULL DEFAULT 0,                    -- BOOLEAN=TINYINT(1)
  direccion_entrega  VARCHAR(255),
  total              DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  created_at         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,   -- auditoría con hora exacta
  updated_at         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                  ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT pk_pedido PRIMARY KEY (id_pedido),
  CONSTRAINT fk_pedido_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuario (id_usuario)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT,
  CONSTRAINT ck_pedido_total_nonneg CHECK (total >= 0)
) ENGINE=InnoDB;

CREATE INDEX ix_pedido_usuario ON pedido (id_usuario);
CREATE INDEX ix_pedido_fecha   ON pedido (fecha);
CREATE INDEX ix_pedido_created ON pedido (created_at);

-- 6) DetallePedido (DATETIME para auditoría)
CREATE TABLE detalle_pedido (
  id_detalle      INT NOT NULL AUTO_INCREMENT,
  id_pedido       INT NOT NULL,
  id_producto     VARCHAR(50) NOT NULL,
  cantidad        INT NOT NULL,
  precio_unitario DECIMAL(12,2) NOT NULL,
  created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                  ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT pk_detalle_pedido PRIMARY KEY (id_detalle),
  CONSTRAINT fk_detalle_pedido_pedido
    FOREIGN KEY (id_pedido)
    REFERENCES pedido (id_pedido)
    ON UPDATE RESTRICT
    ON DELETE CASCADE,                 -- borrar pedido -> borra sus renglones
  CONSTRAINT fk_detalle_pedido_producto
    FOREIGN KEY (id_producto)
    REFERENCES producto (id_producto)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT,
  CONSTRAINT ck_detalle_cantidad_pos CHECK (cantidad > 0),
  CONSTRAINT ck_detalle_precio_nonneg CHECK (precio_unitario >= 0),
  CONSTRAINT uq_detalle_pedido_producto UNIQUE (id_pedido, id_producto)
) ENGINE=InnoDB;

CREATE INDEX ix_detalle_pedido   ON detalle_pedido (id_pedido);
CREATE INDEX ix_detalle_producto ON detalle_pedido (id_producto);
CREATE INDEX ix_detalle_created  ON detalle_pedido (created_at);
