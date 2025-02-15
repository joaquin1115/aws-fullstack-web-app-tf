/* CREACION DE TABLAS */

DROP TABLE IF EXISTS reporte;
DROP TABLE IF EXISTS programacion_reporte;
DROP TABLE IF EXISTS evidencia;
DROP TABLE IF EXISTS reclamo;
DROP TABLE IF EXISTS seguimiento;
DROP TABLE IF EXISTS norma;
DROP TABLE IF EXISTS procedimiento;
DROP TABLE IF EXISTS incidencia;
DROP TABLE IF EXISTS detalle_ticket_producto;
DROP TABLE IF EXISTS detalle_ticket_traslado;
DROP TABLE IF EXISTS traslado;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS detalle_mercancia_stock;
DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS paradero;
DROP TABLE IF EXISTS "local";
DROP TABLE IF EXISTS gps;
DROP TABLE IF EXISTS representante;
DROP TABLE IF EXISTS mercancia;
DROP TABLE IF EXISTS operacion;
DROP TABLE IF EXISTS transportista;
DROP TABLE IF EXISTS empleado;
DROP TABLE IF EXISTS ruta;
DROP TABLE IF EXISTS elemento_catalogo;
DROP TABLE IF EXISTS vehiculo;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS ubicacion;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS persona;
DROP TABLE IF EXISTS reporte_estado;
DROP TABLE IF EXISTS reporte_formato;
DROP TABLE IF EXISTS reporte_frecuencia;
DROP TABLE IF EXISTS reporte_tipo;
DROP TABLE IF EXISTS archivo_tipo;
DROP TABLE IF EXISTS evidencia_tipo;
DROP TABLE IF EXISTS nivel_urgencia;
DROP TABLE IF EXISTS estado_reclamo;
DROP TABLE IF EXISTS reclamo_tipo;
DROP TABLE IF EXISTS accion_tipo;
DROP TABLE IF EXISTS norma_tipo;
DROP TABLE IF EXISTS procedimiento_tipo;
DROP TABLE IF EXISTS incidencia_estado;
DROP TABLE IF EXISTS incidencia_tipo;
DROP TABLE IF EXISTS pedido_estado;
DROP TABLE IF EXISTS pedido_tipo;
DROP TABLE IF EXISTS paradero_tipo;
DROP TABLE IF EXISTS local_ubigeo;
DROP TABLE IF EXISTS local_tipo;
DROP TABLE IF EXISTS operacion_tipo;
DROP TABLE IF EXISTS licencia_tipo;
DROP TABLE IF EXISTS transportista_estado;
DROP TABLE IF EXISTS empleado_cargo;
DROP TABLE IF EXISTS ruta_tipo;
DROP TABLE IF EXISTS elemento_catalogo_unidad;
DROP TABLE IF EXISTS elemento_catalogo_tipo;
DROP TABLE IF EXISTS segmento;
DROP TABLE IF EXISTS elemento_produccion;
DROP TABLE IF EXISTS vehiculo_marca;
DROP TABLE IF EXISTS vehiculo_modelo;
DROP TABLE IF EXISTS vehiculo_tipo;
DROP TABLE IF EXISTS vehiculo_estado;
DROP TABLE IF EXISTS cliente_tipo;
DROP TABLE IF EXISTS cliente_estado;
DROP TABLE IF EXISTS genero;
DROP TABLE IF EXISTS nacionalidad;
DROP TABLE IF EXISTS estado_civil;

/* CREAR LOOKUP TABLES */

CREATE TABLE IF NOT EXISTS estado_civil (
 cod_estado_civil INT NOT NULL,
 descripcion VARCHAR(16),
 PRIMARY KEY (cod_estado_civil)
);

CREATE TABLE IF NOT EXISTS nacionalidad (
 cod_nacionalidad INT NOT NULL,
 descripcion VARCHAR(16),
 PRIMARY KEY (cod_nacionalidad)
);

CREATE TABLE IF NOT EXISTS genero (
 cod_genero INT NOT NULL,
 descripcion VARCHAR(16),
 PRIMARY KEY (cod_genero)
);

CREATE TABLE IF NOT EXISTS cliente_estado (
 cod_cliente_estado CHAR(1) NOT NULL,
 estado_cliente VARCHAR(20) NULL,
 PRIMARY KEY (cod_cliente_estado)

);

CREATE TABLE IF NOT EXISTS cliente_tipo (
 cod_cliente_tipo CHAR(1) NOT NULL,
 tipo_cliente VARCHAR(20) NULL,
 PRIMARY KEY (cod_cliente_tipo)
);

CREATE TABLE IF NOT EXISTS vehiculo_estado (
 cod_vehiculo_estado CHAR(1),
 descripcion VARCHAR(20),
 PRIMARY KEY (cod_vehiculo_estado)
);

CREATE TABLE IF NOT EXISTS vehiculo_marca (
 cod_vehiculo_marca INT,
 descripcion VARCHAR(20),
 PRIMARY KEY (cod_vehiculo_marca)
);

CREATE TABLE IF NOT EXISTS vehiculo_modelo (
 cod_vehiculo_modelo CHAR(1) NOT NULL,
 descripcion VARCHAR(20),
 PRIMARY KEY (cod_vehiculo_modelo)
);

CREATE TABLE IF NOT EXISTS vehiculo_tipo (
 cod_vehiculo_tipo CHAR(1),
 descripcion VARCHAR(20),
 PRIMARY KEY (cod_vehiculo_tipo)
);

CREATE TABLE IF NOT EXISTS elemento_produccion (
 id_elemento_produccion INT NOT NULL PRIMARY KEY,
 descripcion VARCHAR(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS segmento (
 id_segmento INT NOT NULL PRIMARY KEY,
 descripcion VARCHAR(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS elemento_catalogo_tipo (
 id_elemento_catalogo_tipo INT NOT NULL PRIMARY KEY,
 id_elemento_produccion INT NOT NULL,
 id_segmento INT NOT NULL,
 descripcion VARCHAR(30) NOT NULL,
 FOREIGN KEY (id_elemento_produccion) REFERENCES elemento_produccion (id_elemento_produccion),
 FOREIGN KEY (id_segmento) REFERENCES segmento (id_segmento)
);

CREATE TABLE IF NOT EXISTS elemento_catalogo_unidad (
 cod_unidad INT NOT NULL,
 descripcion VARCHAR(30),
 PRIMARY KEY (cod_unidad)
);

CREATE TABLE IF NOT EXISTS ruta_tipo (
 cod_ruta_tipo INT NOT NULL,
 descripcion VARCHAR(32),
 PRIMARY KEY (cod_ruta_tipo)
);

CREATE TABLE IF NOT EXISTS empleado_cargo (
 cod_empleado_cargo INT NOT NULL,
 descripcion VARCHAR(32),
 PRIMARY KEY (cod_empleado_cargo)
);

CREATE TABLE IF NOT EXISTS transportista_estado (
 cod_estado_transportista CHAR(1) NOT NULL,
 descripcion VARCHAR(20) NULL DEFAULT NULL,
 PRIMARY KEY (cod_estado_transportista)
);

CREATE TABLE IF NOT EXISTS licencia_tipo (
 cod_tipo_licencia CHAR(1) NOT NULL,
 descripcion VARCHAR(20) NULL DEFAULT NULL,
 PRIMARY KEY (cod_tipo_licencia)
);

CREATE TABLE IF NOT EXISTS operacion_tipo (
 cod_tipo_operacion INT NOT NULL,
 descripcion VARCHAR(10),
 PRIMARY KEY (cod_tipo_operacion)
);

CREATE TABLE IF NOT EXISTS local_tipo (
 cod_local_tipo INT NOT NULL,
 descripcion VARCHAR(32),
 PRIMARY KEY (cod_local_tipo)
);

CREATE TABLE IF NOT EXISTS local_ubigeo (
    cod_local_ubigeo INT NOT NULL,
    departamento VARCHAR(50),
    provincia VARCHAR(50),
    distrito VARCHAR(50),
    capital VARCHAR(50),
    cod_region_natural INT NOT NULL,
    region_natural VARCHAR(50),
    PRIMARY KEY (cod_local_ubigeo)
);

CREATE TABLE IF NOT EXISTS paradero_tipo (
 cod_paradero_tipo INT NOT NULL,
 descripcion VARCHAR(32),
 PRIMARY KEY (cod_paradero_tipo)
);

CREATE TABLE IF NOT EXISTS pedido_tipo (
 cod_pedido_tipo CHAR(1) NOT NULL,
 tipo_pedido VARCHAR(20) NULL,
 PRIMARY KEY (cod_pedido_tipo)
);

CREATE TABLE IF NOT EXISTS pedido_estado (
 cod_pedido_estado CHAR(1) NOT NULL,
 estado_pedido VARCHAR(20) NULL,
 PRIMARY KEY (cod_pedido_estado)
);

CREATE TABLE IF NOT EXISTS incidencia_tipo (
 cod_tipo_incidencia CHAR(1) NOT NULL,
 descripcion VARCHAR(60) NULL DEFAULT NULL,
 PRIMARY KEY (cod_tipo_incidencia)
);

CREATE TABLE IF NOT EXISTS incidencia_estado (
    cod_estado_incidencia CHAR(1) PRIMARY KEY,
    descripcion VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS procedimiento_tipo (
 cod_tipo_procedimiento CHAR(1) NOT NULL,
 descripcion VARCHAR(60) NULL DEFAULT NULL,
 PRIMARY KEY (cod_tipo_procedimiento)
);

CREATE TABLE IF NOT EXISTS norma_tipo (
 cod_norma_tipo CHAR(1) NOT NULL,
 descripcion VARCHAR(60) NULL DEFAULT NULL,
 PRIMARY KEY (cod_norma_tipo)
);

CREATE TABLE IF NOT EXISTS accion_tipo (
 cod_tipo_accion CHAR(1) NOT NULL,
 descripcion VARCHAR(60) NULL DEFAULT NULL,
 PRIMARY KEY (cod_tipo_accion)
);

CREATE TABLE IF NOT EXISTS reclamo_tipo (
 cod_tipo_reclamo CHAR(1) NOT NULL,
 descripcion VARCHAR(100) NULL DEFAULT NULL,
 PRIMARY KEY (cod_tipo_reclamo)
);

CREATE TABLE IF NOT EXISTS estado_reclamo (
 cod_estado_reclamo CHAR(1) NOT NULL,
 descripcion VARCHAR(15) NULL DEFAULT NULL,
 PRIMARY KEY (cod_estado_reclamo)
);

CREATE TABLE IF NOT EXISTS nivel_urgencia (
 cod_nivel_urgencia CHAR(1) NOT NULL,
 descripcion VARCHAR(15) NULL DEFAULT NULL,
 PRIMARY KEY (cod_nivel_urgencia)
);

CREATE TABLE IF NOT EXISTS evidencia_tipo (
 cod_tipo_evidencia CHAR(1) NOT NULL,
 descripcion VARCHAR(30) NULL DEFAULT NULL,
 PRIMARY KEY (cod_tipo_evidencia)
);

CREATE TABLE IF NOT EXISTS archivo_tipo (
 cod_tipo_archivo INT NOT NULL,
 descripcion VARCHAR(5) NULL DEFAULT NULL,
 PRIMARY KEY (cod_tipo_archivo)
);

CREATE TABLE IF NOT EXISTS reporte_frecuencia (
    cod_reporte_frecuencia INT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL,
    cantidad_tiempo INT NOT NULL,
    unidad_tiempo VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS reporte_formato (
 cod_reporte_formato INT NOT NULL,
 descripcion VARCHAR(32),
 PRIMARY KEY (cod_reporte_formato)
);

CREATE TABLE IF NOT EXISTS reporte_tipo (
    cod_reporte_tipo INT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS reporte_estado (
 cod_reporte_estado INT NOT NULL,
 descripcion VARCHAR(16),
 PRIMARY KEY (cod_reporte_estado)
);

/* CREAR TABLAS */

CREATE TABLE IF NOT EXISTS persona (
 cod_persona SERIAL,
 cod_estado_civil INT NOT NULL,
 cod_nacionalidad INT NOT NULL,
 cod_genero INT NOT NULL,
 dni CHAR(8) NOT NULL,
 primer_apellido VARCHAR(32) NULL DEFAULT NULL,
 segundo_apellido VARCHAR(32) NULL DEFAULT NULL,
 prenombre VARCHAR(32) NULL DEFAULT NULL,
 direccion VARCHAR(128) NULL DEFAULT NULL,
 PRIMARY KEY (cod_persona),
 CONSTRAINT cod_estado_civil
 FOREIGN KEY (cod_estado_civil)
 REFERENCES estado_civil (cod_estado_civil),
 CONSTRAINT cod_nacionalidad
 FOREIGN KEY (cod_nacionalidad)
 REFERENCES nacionalidad (cod_nacionalidad),
 CONSTRAINT cod_genero
 FOREIGN KEY (cod_genero)
 REFERENCES genero (cod_genero)
);

CREATE TABLE IF NOT EXISTS cliente (
 cod_cliente SERIAL,
 cod_cliente_tipo CHAR(1) NOT NULL,
 cod_cliente_estado CHAR(1) NOT NULL,
 nombre VARCHAR(32) NOT NULL,
 ruc CHAR(11) NOT NULL,
 razon_social VARCHAR(120) NOT NULL,
 fecha_registro DATE NOT NULL,
 PRIMARY KEY (cod_cliente),
 CONSTRAINT cod_cliente_tipo
 FOREIGN KEY (cod_cliente_tipo)
 REFERENCES cliente_tipo (cod_cliente_tipo),
 CONSTRAINT cod_cliente_estado
 FOREIGN KEY (cod_cliente_estado)
 REFERENCES cliente_estado (cod_cliente_estado)
);

CREATE TABLE IF NOT EXISTS ubicacion (
 cod_ubicacion SERIAL NOT NULL,
 longitud FLOAT NOT NULL,
 latitud FLOAT NOT NULL,
 PRIMARY KEY (cod_ubicacion)
);

CREATE TABLE IF NOT EXISTS ticket (
 cod_ticket SERIAL NOT NULL,
 fecha_entrega DATE NOT NULL,
 PRIMARY KEY (cod_ticket)
);

CREATE TABLE IF NOT EXISTS vehiculo (
 cod_vehiculo SERIAL NOT NULL,
 cod_vehiculo_estado CHAR(1) NOT NULL,
 anio_fabricacion INT NOT NULL,
 fecha_ultimo_mantenimiento DATE NOT NULL,
 fecha_ultimo_viaje DATE,
 capacidad_carga FLOAT NOT NULL CHECK (capacidad_carga > 0),
 cod_vehiculo_marca INT NOT NULL,
 cod_vehiculo_tipo CHAR(1) NOT NULL,
 placa CHAR(7) NOT NULL,
 cod_vehiculo_modelo CHAR(1) NOT NULL,
 PRIMARY KEY (cod_vehiculo),
 CONSTRAINT cod_vehiculo_estado
 FOREIGN KEY (cod_vehiculo_estado)
 REFERENCES vehiculo_estado (cod_vehiculo_estado),
 CONSTRAINT cod_vehiculo_marca
 FOREIGN KEY (cod_vehiculo_marca)
 REFERENCES vehiculo_marca (cod_vehiculo_marca),
 CONSTRAINT cod_vehiculo_modelo
 FOREIGN KEY (cod_vehiculo_modelo)
 REFERENCES vehiculo_modelo (cod_vehiculo_modelo),
 CONSTRAINT cod_vehiculo_tipo
 FOREIGN KEY (cod_vehiculo_tipo)
 REFERENCES vehiculo_tipo (cod_vehiculo_tipo)
);

CREATE TABLE IF NOT EXISTS elemento_catalogo (
 id_elemento_catalogo SERIAL NOT NULL,
 nombre VARCHAR(128) NOT NULL,
 id_elemento_catalogo_tipo INT NOT NULL,
 descripcion VARCHAR(256) NOT NULL,
 cod_unidad INT NOT NULL,
 temperatura_minima FLOAT NULL DEFAULT NULL,
 temperatura_maxima FLOAT NULL DEFAULT NULL,
 vida_util FLOAT NULL DEFAULT NULL CHECK (vida_util > 0),
 peso_unitario FLOAT NOT NULL CHECK (peso_unitario > 0),
 PRIMARY KEY (id_elemento_catalogo),
 CONSTRAINT id_elemento_catalogo_tipo
 FOREIGN KEY (id_elemento_catalogo_tipo)
 REFERENCES elemento_catalogo_tipo (id_elemento_catalogo_tipo),
 CONSTRAINT cod_unidad
 FOREIGN KEY (cod_unidad)
 REFERENCES elemento_catalogo_unidad (cod_unidad)
);

CREATE TABLE IF NOT EXISTS ruta (
 cod_ruta SERIAL NOT NULL,
 distancia_total FLOAT NOT NULL CHECK (distancia_total > 0),
 cod_ruta_tipo INT NOT NULL,
 duracion FLOAT NOT NULL CHECK (duracion > 0),
 PRIMARY KEY (cod_ruta),
 CONSTRAINT cod_ruta_tipo
 FOREIGN KEY (cod_ruta_tipo)
 REFERENCES ruta_tipo (cod_ruta_tipo)
);

CREATE TABLE IF NOT EXISTS empleado (
 cod_empleado SERIAL,
 cod_cliente INT NOT NULL,
 cod_persona INT NOT NULL,
 cod_empleado_cargo INT NOT NULL,
 fecha_contrato DATE NOT NULL,
 PRIMARY KEY (cod_empleado),
 CONSTRAINT cod_cliente
 FOREIGN KEY (cod_cliente)
 REFERENCES cliente (cod_cliente),
 CONSTRAINT cod_persona
 FOREIGN KEY (cod_persona)
 REFERENCES persona (cod_persona),
 CONSTRAINT cod_empleado_cargo
 FOREIGN KEY (cod_empleado_cargo)
 REFERENCES empleado_cargo (cod_empleado_cargo)
);

CREATE TABLE IF NOT EXISTS transportista (
cod_transportista SERIAL NOT NULL,
cod_empleado INT NOT NULL,
cod_estado_transportista CHAR(1) DEFAULT NULL,
cod_tipo_licencia CHAR(1) NULL DEFAULT NULL,
num_licencia CHAR(9) NULL DEFAULT NULL,
fecha_vencimiento_licencia DATE NOT NULL,
fecha_ultimo_traslado DATE DEFAULT NULL,
PRIMARY KEY (cod_transportista),
CONSTRAINT cod_empleado
 FOREIGN KEY (cod_empleado)
 REFERENCES empleado (cod_empleado),
CONSTRAINT cod_estado_transportista
 FOREIGN KEY (cod_estado_transportista)
 REFERENCES transportista_estado (cod_estado_transportista),
CONSTRAINT cod_tipo_licencia
 FOREIGN KEY (cod_tipo_licencia)
 REFERENCES licencia_tipo (cod_tipo_licencia)
);

CREATE TABLE IF NOT EXISTS operacion (
 id_operacion SERIAL,
 id_operacion_picking INT NULL DEFAULT NULL,
 cod_empleado_ejecutor INT NOT NULL,
 cod_empleado_supervisor INT NOT NULL,
 fecha DATE NOT NULL,
 hora_inicio TIME NOT NULL,
 hora_fin TIME NOT NULL,
 cod_tipo_operacion INT NOT NULL,
 PRIMARY KEY (id_operacion),
 CONSTRAINT cod_empleado_ejecutor
 FOREIGN KEY (cod_empleado_ejecutor)
 REFERENCES empleado (cod_empleado),
 CONSTRAINT cod_empleado_supervisor
 FOREIGN KEY (cod_empleado_supervisor)
 REFERENCES empleado (cod_empleado),
 CONSTRAINT id_operacion_picking
 FOREIGN KEY (id_operacion_picking)
 REFERENCES operacion (id_operacion),
 CONSTRAINT cod_tipo_operacion
 FOREIGN KEY (cod_tipo_operacion)
 REFERENCES operacion_tipo (cod_tipo_operacion)
);

CREATE TABLE IF NOT EXISTS mercancia (
 id_mercancia SERIAL,
 id_operacion_picking INT NOT NULL,
 nro_precinto CHAR(20) NULL DEFAULT NULL,
 PRIMARY KEY (id_mercancia),
 CONSTRAINT id_operacion_picking
 FOREIGN KEY (id_operacion_picking)
 REFERENCES operacion (id_operacion),
 CONSTRAINT uk_nro_precinto UNIQUE (nro_precinto)
);

CREATE TABLE IF NOT EXISTS representante (
 cod_representante SERIAL,
 cod_cliente INT NOT NULL,
 cod_persona INT NOT NULL,
 num_telefono VARCHAR(20) NULL DEFAULT NULL,
 correo_empresarial VARCHAR(50) NULL DEFAULT NULL,
 cargo VARCHAR(40) NULL DEFAULT NULL,
 PRIMARY KEY (cod_representante),
 CONSTRAINT cod_cliente
 FOREIGN KEY (cod_cliente)
 REFERENCES cliente (cod_cliente),
 CONSTRAINT cod_persona
 FOREIGN KEY (cod_persona)
 REFERENCES persona (cod_persona)
);

CREATE TABLE IF NOT EXISTS gps (
 cod_gps SERIAL,
 cod_ubicacion INT NOT NULL,
 cod_vehiculo INT NOT NULL,
 fecha_ubicacion DATE NOT NULL,
 PRIMARY KEY (cod_gps),
 CONSTRAINT cod_ubicacion
 FOREIGN KEY (cod_ubicacion)
 REFERENCES ubicacion (cod_ubicacion),
 CONSTRAINT cod_vehiculo
 FOREIGN KEY (cod_vehiculo)
 REFERENCES vehiculo (cod_vehiculo)
);

CREATE TABLE IF NOT EXISTS "local" (
 cod_local SERIAL NOT NULL,
 cod_cliente INT NOT NULL,
 cod_ubicacion INT NOT NULL,
 cod_local_tipo INT NOT NULL,
 cod_local_ubigeo INT NOT NULL,
 calle VARCHAR(64) NULL,
 numero INT NULL,
 denominacion VARCHAR(64) NULL DEFAULT NULL,
 pais VARCHAR(8) NOT NULL,
 PRIMARY KEY (cod_local),
 CONSTRAINT cod_cliente
 FOREIGN KEY (cod_cliente)
 REFERENCES cliente (cod_cliente),
 CONSTRAINT cod_ubicacion
 FOREIGN KEY (cod_ubicacion)
 REFERENCES ubicacion (cod_ubicacion),
 CONSTRAINT cod_local_tipo
 FOREIGN KEY (cod_local_tipo)
 REFERENCES local_tipo (cod_local_tipo),
 CONSTRAINT cod_local_ubigeo
 FOREIGN KEY (cod_local_ubigeo)
 REFERENCES local_ubigeo (cod_local_ubigeo)
);

CREATE TABLE IF NOT EXISTS paradero (
 cod_paradero SERIAL NOT NULL,
 cod_ruta INT NOT NULL,
 cod_local INT NOT NULL,
 cod_paradero_tipo INT NOT NULL,
 orden INT NOT NULL CHECK (orden > 0),
 PRIMARY KEY (cod_paradero),
 CONSTRAINT cod_ruta
 FOREIGN KEY (cod_ruta)
 REFERENCES ruta (cod_ruta),
 CONSTRAINT cod_local
 FOREIGN KEY (cod_local)
 REFERENCES local (cod_local),
 CONSTRAINT cod_paradero_tipo
 FOREIGN KEY (cod_paradero_tipo)
 REFERENCES paradero_tipo (cod_paradero_tipo)
);

CREATE TABLE IF NOT EXISTS stock (
 id_stock SERIAL NOT NULL,
 cod_stock CHAR(17) NOT NULL,
 id_elemento_catalogo INT NOT NULL,
 nro_lote INT NULL DEFAULT NULL,
 fecha_caducidad DATE NOT NULL,
 cantidad_disponible INT NOT NULL CHECK (cantidad_disponible > 0),
 PRIMARY KEY (id_stock),
 CONSTRAINT id_elemento_catalogo
 FOREIGN KEY (id_elemento_catalogo)
 REFERENCES elemento_catalogo (id_elemento_catalogo)
);

CREATE TABLE IF NOT EXISTS detalle_mercancia_stock(
 id_mercancia INT NOT NULL,
 id_stock INT NOT NULL,
 cantidad INT NOT NULL,
 FOREIGN KEY (id_mercancia) REFERENCES mercancia (id_mercancia),
 FOREIGN KEY (id_stock) REFERENCES stock (id_stock),
 PRIMARY KEY (id_mercancia, id_stock)
);

CREATE TABLE IF NOT EXISTS pedido (
 cod_pedido SERIAL,
 cod_representante INT NOT NULL,
 cod_empleado INT NOT NULL,
 cod_pedido_tipo CHAR(1) NULL DEFAULT NULL,
 cod_pedido_estado CHAR(1) NOT NULL,
 fecha_registro DATE NOT NULL,
 cod_ticket INT NOT NULL,
 PRIMARY KEY (cod_pedido),
 CONSTRAINT cod_representante
 FOREIGN KEY (cod_representante)
 REFERENCES representante (cod_representante),
 CONSTRAINT cod_empleado
 FOREIGN KEY (cod_empleado)
 REFERENCES empleado (cod_empleado),
 CONSTRAINT cod_pedido_tipo
 FOREIGN KEY (cod_pedido_tipo)
 REFERENCES pedido_tipo (cod_pedido_tipo),
 CONSTRAINT cod_pedido_estado
 FOREIGN KEY (cod_pedido_estado)
 REFERENCES pedido_estado (cod_pedido_estado),
 CONSTRAINT cod_ticket
 FOREIGN KEY (cod_ticket)
 REFERENCES ticket (cod_ticket)
);

CREATE TABLE IF NOT EXISTS traslado (
 id_traslado SERIAL,
 cod_vehiculo INT NOT NULL,
 cod_ruta INT NOT NULL,
 cod_transportista INT NOT NULL,
 id_operacion_inicia INT NOT NULL,
 id_operacion_termina INT NULL DEFAULT NULL,
 cod_guia_remision CHAR(21) NULL,
 PRIMARY KEY (id_traslado),
 CONSTRAINT id_operacion_inicia
 FOREIGN KEY (id_operacion_inicia)
 REFERENCES operacion (id_operacion),
 CONSTRAINT id_operacion_termina
 FOREIGN KEY (id_operacion_termina)
 REFERENCES operacion (id_operacion),
 CONSTRAINT cod_ruta
 FOREIGN KEY (cod_ruta)
 REFERENCES ruta (cod_ruta),
 CONSTRAINT cod_transportista
 FOREIGN KEY (cod_transportista)
 REFERENCES transportista (cod_transportista),
 CONSTRAINT cod_vehiculo
 FOREIGN KEY (cod_vehiculo)
 REFERENCES vehiculo (cod_vehiculo)
);

CREATE TABLE IF NOT EXISTS detalle_ticket_traslado (
 id_traslado INT NOT NULL,
 cod_ticket INT NOT NULL,
 PRIMARY KEY (id_traslado, cod_ticket),
 CONSTRAINT id_traslado
 FOREIGN KEY (id_traslado)
 REFERENCES traslado (id_traslado),
 CONSTRAINT cod_ticket
 FOREIGN KEY (cod_ticket)
 REFERENCES ticket (cod_ticket)
);


CREATE TABLE IF NOT EXISTS detalle_ticket_producto (
 cod_ticket INT NOT NULL,
 id_elemento_catalogo INT NOT NULL,
 cantidad INT NOT NULL CHECK (cantidad > 0),
 PRIMARY KEY (cod_ticket, id_elemento_catalogo),
 CONSTRAINT cod_ticket
 FOREIGN KEY (cod_ticket)
 REFERENCES ticket (cod_ticket),
 CONSTRAINT id_elemento_catalogo
 FOREIGN KEY (id_elemento_catalogo)
 REFERENCES elemento_catalogo (id_elemento_catalogo)
);

CREATE TABLE IF NOT EXISTS incidencia (
 cod_incidencia SERIAL NOT NULL,
 id_traslado INT NOT NULL,
 cod_tipo_incidencia CHAR(1) NOT NULL,
 descripcion VARCHAR(128) NULL DEFAULT NULL,
 fecha_ocurrencia DATE NOT NULL,
 hora_ocurrencia TIME NOT NULL,
 cod_estado_incidencia CHAR(1) NOT NULL,
 PRIMARY KEY (cod_incidencia),
 CONSTRAINT id_traslado
 FOREIGN KEY (id_traslado)
 REFERENCES traslado (id_traslado),
 CONSTRAINT cod_tipo_incidencia
 FOREIGN KEY (cod_tipo_incidencia)
 REFERENCES incidencia_tipo (cod_tipo_incidencia),
 CONSTRAINT cod_estado_incidencia
 FOREIGN KEY (cod_estado_incidencia)
 REFERENCES incidencia_estado (cod_estado_incidencia)
);

CREATE TABLE IF NOT EXISTS procedimiento (
cod_procedimiento SERIAL NOT NULL,
cod_incidencia INT NOT NULL,
cod_tipo_procedimiento CHAR(1) NOT NULL,
nombre VARCHAR(64) NULL DEFAULT NULL,
tiempo_estimado INT NOT NULL CHECK (tiempo_estimado > 0),
PRIMARY KEY (cod_procedimiento),
CONSTRAINT cod_tipo_procedimiento
 FOREIGN KEY (cod_tipo_procedimiento)
 REFERENCES procedimiento_tipo (cod_tipo_procedimiento),
CONSTRAINT cod_incidencia
 FOREIGN KEY (cod_incidencia)
 REFERENCES incidencia (cod_incidencia)
);

CREATE TABLE IF NOT EXISTS norma (
cod_norma SERIAL NOT NULL,
cod_incidencia INT NOT NULL,
cod_norma_tipo CHAR(1) NOT NULL,
fecha_emision DATE NOT NULL,
fecha_vigencia DATE NOT NULL,
PRIMARY KEY (cod_norma),
CONSTRAINT cod_incidencia
 FOREIGN KEY (cod_incidencia)
 REFERENCES incidencia (cod_incidencia),
CONSTRAINT cod_norma_tipo
 FOREIGN KEY (cod_norma_tipo)
 REFERENCES norma_tipo (cod_norma_tipo)
);

CREATE TABLE IF NOT EXISTS seguimiento (
 cod_seguimiento SERIAL NOT NULL,
 cod_cliente_interno INT NOT NULL,
 cod_tipo_accion CHAR(1) NOT NULL,
 comentario VARCHAR(500) NULL DEFAULT NULL,
 fecha_resolucion DATE NOT NULL,
 numero_caso INT NOT NULL,
 PRIMARY KEY (cod_seguimiento),
 CONSTRAINT cod_cliente_interno
 FOREIGN KEY (cod_cliente_interno)
 REFERENCES cliente (cod_cliente),
 CONSTRAINT cod_tipo_accion
 FOREIGN KEY (cod_tipo_accion)
 REFERENCES accion_tipo (cod_tipo_accion)
);

CREATE TABLE IF NOT EXISTS reclamo (
 cod_reclamo SERIAL NOT NULL,
 cod_representante INT NOT NULL,
 cod_pedido INT NOT NULL,
 cod_seguimiento INT NOT NULL,
 cod_tipo_reclamo CHAR(1) NOT NULL,
 cod_nivel_urgencia CHAR(1) NOT NULL,
 cod_estado_reclamo CHAR(1) NOT NULL,
 comentario VARCHAR(200) NULL DEFAULT NULL,
 fecha_suceso DATE NOT NULL,
 fecha_reclamo DATE NOT NULL,
 PRIMARY KEY (cod_reclamo),
 CONSTRAINT cod_representante
 FOREIGN KEY (cod_representante)
 REFERENCES representante (cod_representante),
 CONSTRAINT cod_pedido
 FOREIGN KEY (cod_pedido)
 REFERENCES pedido (cod_pedido),
 CONSTRAINT cod_seguimiento
 FOREIGN KEY (cod_seguimiento)
 REFERENCES seguimiento (cod_seguimiento),
 CONSTRAINT cod_tipo_reclamo
 FOREIGN KEY (cod_tipo_reclamo)
 REFERENCES reclamo_tipo (cod_tipo_reclamo),
 CONSTRAINT cod_nivel_urgencia
 FOREIGN KEY (cod_nivel_urgencia)
 REFERENCES nivel_urgencia (cod_nivel_urgencia),
 CONSTRAINT cod_estado_reclamo
 FOREIGN KEY (cod_estado_reclamo)
 REFERENCES estado_reclamo (cod_estado_reclamo)
);

CREATE TABLE IF NOT EXISTS evidencia (
 cod_evidencia SERIAL NOT NULL,
 cod_reclamo INT NOT NULL,
 cod_tipo_evidencia CHAR(1) NOT NULL,
 cod_tipo_archivo INT NOT NULL,
 nombre_evidencia VARCHAR(60) NULL DEFAULT NULL,
 PRIMARY KEY (cod_evidencia),
 CONSTRAINT cod_reclamo
 FOREIGN KEY (cod_reclamo)
 REFERENCES reclamo (cod_reclamo),
 CONSTRAINT cod_tipo_evidencia
 FOREIGN KEY (cod_tipo_evidencia)
 REFERENCES evidencia_tipo (cod_tipo_evidencia),
 CONSTRAINT cod_tipo_archivo
 FOREIGN KEY (cod_tipo_archivo)
 REFERENCES archivo_tipo (cod_tipo_archivo)
);

CREATE TABLE IF NOT EXISTS programacion_reporte (
 cod_programacion_reporte SERIAL NOT NULL,
 cod_representante INT NOT NULL,
 cod_reporte_formato INT NOT NULL,
 cod_reporte_estado INT NOT NULL,
 cod_reporte_tipo INT NOT NULL,
 cod_reporte_frecuencia INT NOT NULL,
 fecha_inicio DATE NOT NULL,
 fecha_fin TIME NOT NULL,
 PRIMARY KEY (cod_programacion_reporte),
 CONSTRAINT cod_empleado
 FOREIGN KEY (cod_representante)
 REFERENCES representante (cod_representante),
 CONSTRAINT cod_reporte_formato
 FOREIGN KEY (cod_reporte_formato)
 REFERENCES reporte_formato (cod_reporte_formato),
 CONSTRAINT cod_reporte_estado
 FOREIGN KEY (cod_reporte_estado)
 REFERENCES reporte_estado (cod_reporte_estado),
 CONSTRAINT cod_reporte_tipo
 FOREIGN KEY (cod_reporte_tipo)
 REFERENCES reporte_tipo (cod_reporte_tipo),
 CONSTRAINT cod_reporte_frecuencia
 FOREIGN KEY (cod_reporte_frecuencia)
 REFERENCES reporte_frecuencia (cod_reporte_frecuencia)
);

CREATE TABLE IF NOT EXISTS reporte (
 cod_reporte SERIAL NOT NULL,
 cod_representante INT NOT NULL,
 cod_reporte_formato INT NOT NULL,
 cod_reporte_tipo INT NOT NULL,
 fecha_generacion DATE NOT NULL,
 hora_generacion TIME NOT NULL,
 PRIMARY KEY (cod_reporte),
 CONSTRAINT cod_reporte_representante
 FOREIGN KEY (cod_representante)
 REFERENCES representante (cod_representante),
 CONSTRAINT cod_reporte_formato_fk
 FOREIGN KEY (cod_reporte_formato)
 REFERENCES reporte_formato (cod_reporte_formato),
 CONSTRAINT cod_reporte_tipo_fk
 FOREIGN KEY (cod_reporte_tipo)
 REFERENCES reporte_tipo (cod_reporte_tipo)
);

/* CARGA DE DATOS */

DO $$
DECLARE 
    base_path TEXT;
BEGIN
    base_path := '/var/lib/postgresql/data/';

    EXECUTE 'COPY estado_civil FROM ' || quote_literal(base_path || 'Estado_civil.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY nacionalidad FROM ' || quote_literal(base_path || 'Nacionalidad.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY genero FROM ' || quote_literal(base_path || 'Genero.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY cliente_estado FROM ' || quote_literal(base_path || 'Cliente_estado.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY cliente_tipo FROM ' || quote_literal(base_path || 'Cliente_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY vehiculo_estado FROM ' || quote_literal(base_path || 'Vehiculo_estado.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY vehiculo_tipo FROM ' || quote_literal(base_path || 'Vehiculo_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY vehiculo_marca FROM ' || quote_literal(base_path || 'Vehiculo_marca.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY vehiculo_modelo FROM ' || quote_literal(base_path || 'Vehiculo_modelo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY elemento_produccion FROM ' || quote_literal(base_path || 'Elemento_produccion.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY segmento FROM ' || quote_literal(base_path || 'Segmento.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY elemento_catalogo_tipo FROM ' || quote_literal(base_path || 'Elemento_catalogo_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY elemento_catalogo_unidad FROM ' || quote_literal(base_path || 'Elemento_catalogo_unidad.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY ruta_tipo FROM ' || quote_literal(base_path || 'Ruta_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY empleado_cargo FROM ' || quote_literal(base_path || 'Empleado_cargo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY transportista_estado FROM ' || quote_literal(base_path || 'Transportista_estado.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY licencia_tipo FROM ' || quote_literal(base_path || 'Licencia_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY operacion_tipo FROM ' || quote_literal(base_path || 'Operacion_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY local_tipo FROM ' || quote_literal(base_path || 'Local_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY local_ubigeo FROM ' || quote_literal(base_path || 'Ubigeos.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY paradero_tipo FROM ' || quote_literal(base_path || 'Paradero_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY pedido_tipo FROM ' || quote_literal(base_path || 'Pedido_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY pedido_estado FROM ' || quote_literal(base_path || 'Pedido_estado.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY incidencia_tipo FROM ' || quote_literal(base_path || 'Incidencia_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY incidencia_estado FROM ' || quote_literal(base_path || 'Incidencia_estado.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY procedimiento_tipo FROM ' || quote_literal(base_path || 'Procedimiento_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY norma_tipo FROM ' || quote_literal(base_path || 'Norma_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY accion_tipo FROM ' || quote_literal(base_path || 'Accion_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY reclamo_tipo FROM ' || quote_literal(base_path || 'Reclamo_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY estado_reclamo FROM ' || quote_literal(base_path || 'Estado_reclamo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY nivel_urgencia FROM ' || quote_literal(base_path || 'Nivel_urgencia.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY evidencia_tipo FROM ' || quote_literal(base_path || 'Evidencia_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY archivo_tipo FROM ' || quote_literal(base_path || 'Archivo_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY reporte_frecuencia FROM ' || quote_literal(base_path || 'Reporte_frecuencia.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY reporte_formato FROM ' || quote_literal(base_path || 'Reporte_formato.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY reporte_tipo FROM ' || quote_literal(base_path || 'Reporte_tipo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY reporte_estado FROM ' || quote_literal(base_path || 'Reporte_estado.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY persona FROM ' || quote_literal(base_path || 'Persona.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY cliente FROM ' || quote_literal(base_path || 'Cliente.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY ubicacion FROM ' || quote_literal(base_path || 'Ubicacion.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY ticket FROM ' || quote_literal(base_path || 'Ticket.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY vehiculo FROM ' || quote_literal(base_path || 'Vehiculo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY elemento_catalogo FROM ' || quote_literal(base_path || 'Elemento_catalogo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY ruta FROM ' || quote_literal(base_path || 'Ruta.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY empleado FROM ' || quote_literal(base_path || 'Empleado.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY transportista FROM ' || quote_literal(base_path || 'Transportista.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY operacion FROM ' || quote_literal(base_path || 'Operacion.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY mercancia FROM ' || quote_literal(base_path || 'Mercancia.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY representante FROM ' || quote_literal(base_path || 'Representante.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY gps FROM ' || quote_literal(base_path || 'Gps.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY "local" FROM ' || quote_literal(base_path || 'Local.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY paradero FROM ' || quote_literal(base_path || 'Paradero.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY stock FROM ' || quote_literal(base_path || 'Stock.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY detalle_mercancia_stock FROM ' || quote_literal(base_path || 'Detalle_mercancia_stock.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY pedido FROM ' || quote_literal(base_path || 'Pedido.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY traslado FROM ' || quote_literal(base_path || 'Traslado.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY detalle_ticket_traslado FROM ' || quote_literal(base_path || 'Detalle_ticket_traslado.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY detalle_ticket_producto FROM ' || quote_literal(base_path || 'Detalle_ticket_producto.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY incidencia FROM ' || quote_literal(base_path || 'Incidencia.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY procedimiento FROM ' || quote_literal(base_path || 'Procedimiento.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY norma FROM ' || quote_literal(base_path || 'Norma.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY seguimiento FROM ' || quote_literal(base_path || 'Seguimiento.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY reclamo FROM ' || quote_literal(base_path || 'Reclamo.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY evidencia FROM ' || quote_literal(base_path || 'Evidencia.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY programacion_reporte FROM ' || quote_literal(base_path || 'Programacion_reporte.csv') || ' DELIMITER '','' CSV HEADER';
    EXECUTE 'COPY reporte FROM ' || quote_literal(base_path || 'Reporte.csv') || ' DELIMITER '','' CSV HEADER';
END $$;

/* ACTUALIZAMOS VALORES DE LAS SECUENCIAS */

-- Actualizar la secuencia para la tabla "persona"
SELECT setval('persona_cod_persona_seq', (SELECT MAX(cod_persona) FROM persona));

-- Actualizar la secuencia para la tabla "cliente"
SELECT setval('cliente_cod_cliente_seq', (SELECT MAX(cod_cliente) FROM cliente));

-- Actualizar la secuencia para la tabla "ubicacion"
SELECT setval('ubicacion_cod_ubicacion_seq', (SELECT MAX(cod_ubicacion) FROM ubicacion));

-- Actualizar la secuencia para la tabla "ticket"
SELECT setval('ticket_cod_ticket_seq', (SELECT MAX(cod_ticket) FROM ticket));

-- Actualizar la secuencia para la tabla "vehiculo"
SELECT setval('vehiculo_cod_vehiculo_seq', (SELECT MAX(cod_vehiculo) FROM vehiculo));

-- Actualizar la secuencia para la tabla "elemento_catalogo"
SELECT setval('elemento_catalogo_id_elemento_catalogo_seq', (SELECT MAX(id_elemento_catalogo) FROM elemento_catalogo));

-- Actualizar la secuencia para la tabla "ruta"
SELECT setval('ruta_cod_ruta_seq', (SELECT MAX(cod_ruta) FROM ruta));

-- Actualizar la secuencia para la tabla "empleado"
SELECT setval('empleado_cod_empleado_seq', (SELECT MAX(cod_empleado) FROM empleado));

-- Actualizar la secuencia para la tabla "transportista"
SELECT setval('transportista_cod_transportista_seq', (SELECT MAX(cod_transportista) FROM transportista));

-- Actualizar la secuencia para la tabla "operacion"
SELECT setval('operacion_id_operacion_seq', (SELECT MAX(id_operacion) FROM operacion));

-- Actualizar la secuencia para la tabla "mercancia"
SELECT setval('mercancia_id_mercancia_seq', (SELECT MAX(id_mercancia) FROM mercancia));

-- Actualizar la secuencia para la tabla "representante"
SELECT setval('representante_cod_representante_seq', (SELECT MAX(cod_representante) FROM representante));

-- Actualizar la secuencia para la tabla "gps"
SELECT setval('gps_cod_gps_seq', (SELECT MAX(cod_gps) FROM gps));

-- Actualizar la secuencia para la tabla "local"
SELECT setval('"local_cod_local_seq"', (SELECT MAX(cod_local) FROM "local"));

-- Actualizar la secuencia para la tabla "paradero"
SELECT setval('paradero_cod_paradero_seq', (SELECT MAX(cod_paradero) FROM paradero));

-- Actualizar la secuencia para la tabla "stock"
SELECT setval('stock_id_stock_seq', (SELECT MAX(id_stock) FROM stock));

-- Actualizar la secuencia para la tabla "pedido"
SELECT setval('pedido_cod_pedido_seq', (SELECT MAX(cod_pedido) FROM pedido));

-- Actualizar la secuencia para la tabla "traslado"
SELECT setval('traslado_id_traslado_seq', (SELECT MAX(id_traslado) FROM traslado));

-- Actualizar la secuencia para la tabla "incidencia"
SELECT setval('incidencia_cod_incidencia_seq', (SELECT MAX(cod_incidencia) FROM incidencia));

-- Actualizar la secuencia para la tabla "procedimiento"
SELECT setval('procedimiento_cod_procedimiento_seq', (SELECT MAX(cod_procedimiento) FROM procedimiento));

-- Actualizar la secuencia para la tabla "norma"
SELECT setval('norma_cod_norma_seq', (SELECT MAX(cod_norma) FROM norma));

-- Actualizar la secuencia para la tabla "seguimiento"
SELECT setval('seguimiento_cod_seguimiento_seq', (SELECT MAX(cod_seguimiento) FROM seguimiento));

-- Actualizar la secuencia para la tabla "reclamo"
SELECT setval('reclamo_cod_reclamo_seq', (SELECT MAX(cod_reclamo) FROM reclamo));

-- Actualizar la secuencia para la tabla "evidencia"
SELECT setval('evidencia_cod_evidencia_seq', (SELECT MAX(cod_evidencia) FROM evidencia));

-- Actualizar la secuencia para la tabla "programacion_reporte"
SELECT setval('programacion_reporte_cod_programacion_reporte_seq', (SELECT MAX(cod_programacion_reporte) FROM programacion_reporte));

-- Actualizar la secuencia para la tabla "reporte"
SELECT setval('reporte_cod_reporte_seq', (SELECT MAX(cod_reporte) FROM reporte));

/* CREACION DE INDICES */

DROP INDEX IF EXISTS fecha_mantenimiento;
CREATE INDEX fecha_mantenimiento
ON vehiculo(fecha_ultimo_mantenimiento, cod_vehiculo_estado);

/* CREACION DE TRIGGERS */

-- Trigger para actualizar la fecha de último traslado de un transportista y fecha de último viaje de un vehículo

CREATE OR REPLACE FUNCTION actualizar_fechas_ultimo_traslado_y_viaje()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE transportista
    SET fecha_ultimo_traslado = NEW.fecha
    WHERE cod_transportista = (
        SELECT t.cod_transportista
        FROM traslado t
        JOIN operacion o ON t.id_operacion_inicia = o.id_operacion
        WHERE o.id_operacion_picking = NEW.id_operacion_picking
        LIMIT 1
    );
    
    UPDATE vehiculo
    SET fecha_ultimo_viaje = NEW.fecha
    WHERE cod_vehiculo = (
        SELECT t.cod_vehiculo
        FROM traslado t
        JOIN operacion o ON t.id_operacion_inicia = o.id_operacion
        WHERE o.id_operacion_picking = NEW.id_operacion_picking
        LIMIT 1
    );
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_actualizar_fechas_ultimo_traslado_y_viaje
AFTER INSERT ON operacion
FOR EACH ROW
WHEN (NEW.cod_tipo_operacion = 6)
EXECUTE FUNCTION actualizar_fechas_ultimo_traslado_y_viaje();

-- Trigger para la generación de un registro cuando se actualiza el estado de un vehículo

DROP TABLE IF EXISTS vehiculo_logs;
CREATE TABLE IF NOT EXISTS vehiculo_logs (
	cod_vehiculo_log SERIAL NOT NULL,
	cod_vehiculo INT NOT NULL,
	cod_estado_anterior CHAR NOT NULL,
	cod_estado_actual CHAR NOT NULL,
	fecha_log TIMESTAMP NOT NULL,
	PRIMARY KEY (cod_vehiculo_log),
	CONSTRAINT cod_estado_anterior
	FOREIGN KEY (cod_estado_anterior)
	REFERENCES vehiculo_estado (cod_vehiculo_estado),
	CONSTRAINT cod_estado_actual
	FOREIGN KEY (cod_estado_actual)
	REFERENCES vehiculo_estado (cod_vehiculo_estado)	
);

CREATE OR REPLACE FUNCTION validar_transportista()
RETURNS TRIGGER
LANGUAGE plpgsql AS $$
BEGIN
	INSERT INTO vehiculo_logs (cod_vehiculo, cod_estado_anterior, cod_estado_actual, fecha_log)
    VALUES (NEW.cod_vehiculo, OLD.cod_vehiculo_estado, NEW.cod_vehiculo_estado, CURRENT_TIMESTAMP);
    RETURN NEW;
END $$;

CREATE TRIGGER cambio_estado_vehiculo
AFTER UPDATE OF cod_vehiculo_estado ON vehiculo
FOR EACH ROW
WHEN (OLD.cod_vehiculo_estado IS DISTINCT FROM NEW.cod_vehiculo_estado)
EXECUTE FUNCTION validar_transportista();