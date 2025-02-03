package com.sanfernando.sanfernando.dao.daoImpl;

import com.sanfernando.sanfernando.dao.AlmacenDao;
import com.sanfernando.sanfernando.dtos.requests.almacen.DetalleMercanciaDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.MercanciaDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.MercanciasRequestDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.OperacionDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.StockDTO;
import com.sanfernando.sanfernando.dtos.requests.almacen.TrasladoDTO;
import com.sanfernando.sanfernando.dtos.responses.almacen.BusquedaAlmacenResponse;
import com.sanfernando.sanfernando.models.DetalleMercancia;
import com.sanfernando.sanfernando.models.Mercancia;
import com.sanfernando.sanfernando.models.Operacion;
import com.sanfernando.sanfernando.models.Proceso;
import com.sanfernando.sanfernando.models.Ruta;
import com.sanfernando.sanfernando.models.Transportista;
import com.sanfernando.sanfernando.models.Traslado;
import com.sanfernando.sanfernando.models.Vehiculo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AlmacenDaoImpl implements AlmacenDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BusquedaAlmacenResponse buscarPorNroPrecinto(String nroPrecinto) {
        BusquedaAlmacenResponse respuesta = new BusquedaAlmacenResponse();
        List<Operacion> operaciones = obtenerOperaciones(nroPrecinto,true);
        int cantidadOperaciones = operaciones.size();
        
        System.out.println("\nOperaciones: " + operaciones.size());

        respuesta.setProceso(new Proceso(obtenerEstadoProceso(cantidadOperaciones),operaciones));
        
        System.out.println("\nProceso: " + respuesta.getProceso().getEstado());

        respuesta.setMercancias(obtenerMercancias(nroPrecinto,true));
        if (cantidadOperaciones > 4){
            respuesta.setTraslado(obtenerTraslado(nroPrecinto,true));
            respuesta.getTraslado().setEstado(obtenerEstadoTraslado(cantidadOperaciones));
        }
        return respuesta;
    }

    @Override
    public BusquedaAlmacenResponse buscarPorCodGuiaRemision(String codGuiaRemision) {
        BusquedaAlmacenResponse respuesta = new BusquedaAlmacenResponse();
        List<Operacion> operaciones = obtenerOperaciones(codGuiaRemision,false);
        int cantidadOperaciones = operaciones.size();
        respuesta.setProceso(new Proceso(obtenerEstadoProceso(cantidadOperaciones),operaciones));
        respuesta.setMercancias(obtenerMercancias(codGuiaRemision,false));
        respuesta.setTraslado(obtenerTraslado(codGuiaRemision,false));
        respuesta.getTraslado().setEstado(obtenerEstadoTraslado(cantidadOperaciones));
        return respuesta;
    }

    @Override
    public int registrarOperacion(OperacionDTO operacionDTO) {
        String sql = "INSERT INTO operacion (id_operacion_picking, cod_empleado_ejecutor, cod_empleado_supervisor, cod_tipo_operacion, fecha, hora_inicio, hora_fin) " +
                "VALUES ( " +
                "    ?, " +
                "    (SELECT e.cod_empleado FROM empleado e JOIN persona p ON e.cod_persona = p.cod_persona WHERE p.dni = ?),  " +
                "    (SELECT e.cod_empleado FROM empleado e JOIN persona p ON e.cod_persona = p.cod_persona WHERE p.dni = ?),  " +
                "    ?, " +
                "    ?, " +
                "    ?, " +
                "    ? " +
                ") " +
                "RETURNING id_operacion;";
        Integer idOperacion = jdbcTemplate.queryForObject(sql, new Object[]{
                operacionDTO.getCodTipoOperacion() == 1 ? null : operacionDTO.getIdPicking(),
                operacionDTO.getDniEjecutor(),
                operacionDTO.getDniSupervisor(),
                operacionDTO.getCodTipoOperacion(),
                java.sql.Date.valueOf(operacionDTO.getFecha()),
                java.sql.Time.valueOf(operacionDTO.getHoraInicio()),
                java.sql.Time.valueOf(operacionDTO.getHoraFin())
        }, Integer.class);

        if (operacionDTO.getCodTipoOperacion() == 6) {
            String updateTrasladoSql = "UPDATE traslado " +
                    "SET id_operacion_termina = ? " +
                    "FROM traslado t " +
                    "INNER JOIN operacion o ON t.id_operacion_inicia = o.id_operacion " +
                    "WHERE o.id_operacion_picking = ?;";
            jdbcTemplate.update(updateTrasladoSql, idOperacion, operacionDTO.getIdPicking());
        }
        return idOperacion;
    }

    @Override
    public List<String> registrarMercancias(MercanciasRequestDTO mercanciasRequestDTO) {
        int idOperacionPicking = mercanciasRequestDTO.getIdOperacionPicking();
        List<String> nroPrecintos = new ArrayList<>();

        for (MercanciaDTO mercanciaDTO : mercanciasRequestDTO.getMercancias()) {
            String insertSql = "INSERT INTO mercancia (id_operacion_picking) VALUES (?) RETURNING id_mercancia";
            Integer idMercancia = jdbcTemplate.queryForObject(insertSql, new Object[]{idOperacionPicking}, Integer.class);

            String nroPrecinto = generarNroPrecinto(idOperacionPicking, idMercancia);

            String updateSql = "UPDATE mercancia SET nro_precinto = ? WHERE id_mercancia = ?";
            jdbcTemplate.update(updateSql, nroPrecinto, idMercancia);

            for (DetalleMercanciaDTO detalle : mercanciaDTO.getDetalles()) {
                String detalleSql = "INSERT INTO detalle_mercancia_stock (id_mercancia, id_stock, cantidad) VALUES (?, ?, ?)";
                jdbcTemplate.update(detalleSql, idMercancia, detalle.getIdStock(), detalle.getCantidad());

                String updateStockSql = "UPDATE stock SET cantidad_disponible = cantidad_disponible - ? WHERE id_stock = ?";
                jdbcTemplate.update(updateStockSql, detalle.getCantidad(), detalle.getIdStock());
            }

            nroPrecintos.add(nroPrecinto);
        }

        return nroPrecintos;
    }

    @Override
    public String registrarTraslado(TrasladoDTO trasladoDTO) {
        String sql = "INSERT INTO traslado (cod_vehiculo, cod_ruta, cod_transportista, id_operacion_inicia)" +
                " VALUES" +
                " ((SELECT cod_vehiculo FROM vehiculo WHERE placa = ?)," +
                " ?," +
                " (SELECT t.cod_transportista " +
                "  FROM transportista t " +
                "  INNER JOIN empleado e on t.cod_empleado = e.cod_empleado " +
                "  INNER JOIN persona p on p.cod_persona = e.cod_persona " +
                "  WHERE p.dni = ?)," +
                " ?)" +
                " RETURNING id_traslado;";
        Integer idTraslado = jdbcTemplate.queryForObject(sql, new Object[]{
                trasladoDTO.getPlacaVehiculo(),
                trasladoDTO.getCodRuta(),
                trasladoDTO.getDniTransportista(),
                trasladoDTO.getIdOperacionInicia()
        }, Integer.class);

        String codGuiaRemisionSql = "SELECT LPAD(CAST(? AS TEXT), 5, '0') || " +
                "TO_CHAR(o.fecha, 'YYYYMMDD') || TO_CHAR(o.hora_inicio, 'HH24MI') || TO_CHAR(o.hora_fin, 'HH24MI') " +
                "FROM operacion o WHERE o.id_operacion = ?";
        String codGuiaRemision = jdbcTemplate.queryForObject(codGuiaRemisionSql, new Object[]{idTraslado, trasladoDTO.getIdOperacionInicia()}, String.class);

        String updateGuiaRemisionSql = "UPDATE traslado SET cod_guia_remision = ? WHERE id_traslado = ?";
        jdbcTemplate.update(updateGuiaRemisionSql, codGuiaRemision, idTraslado);

        for (Integer codPedido : trasladoDTO.getCodPedidos()) {
            String sqlDetalle = "INSERT INTO detalle_ticket_traslado(id_traslado, cod_ticket) " +
                    "VALUES ( " +
                    "    ?, " +
                    "    (SELECT t.cod_ticket " +
                    "     FROM ticket t " +
                    "     INNER JOIN pedido p ON t.cod_ticket = p.cod_ticket " +
                    "     WHERE p.cod_pedido = ?) " +
                    ");";
            jdbcTemplate.update(sqlDetalle, idTraslado, codPedido);
        }

        return codGuiaRemision;
    }

    @Override
    public StockDTO obtenerStock(String codStock){
        String sql = "SELECT st.id_stock as id, ec.nombre as nombre, ect.descripcion as categoria, " +
                "ep.descripcion as tipo, se.descripcion as segmento, ecu.descripcion as unidad " +
                "FROM stock st " +
                "INNER JOIN elemento_catalogo ec ON st.id_elemento_catalogo = ec.id_elemento_catalogo " +
                "INNER JOIN elemento_catalogo_tipo ect ON ec.id_elemento_catalogo_tipo = ect.id_elemento_catalogo_tipo " +
                "INNER JOIN elemento_produccion ep ON ect.id_elemento_produccion = ep.id_elemento_produccion " +
                "INNER JOIN elemento_catalogo_unidad ecu ON ec.cod_unidad = ecu.cod_unidad " +
                "INNER JOIN segmento se ON ect.id_segmento = se.id_segmento " +
                "WHERE st.cod_stock = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(StockDTO.class), codStock);
    }

    @Override
    public boolean validarNroPrecinto(String nroPrecinto) {
        String sql = "SELECT EXISTS (SELECT * FROM mercancia WHERE nro_precinto = ?)";
        return jdbcTemplate.query(sql, rs -> { rs.next(); return rs.getBoolean(1); }, nroPrecinto);
    }

    @Override
    public boolean validarCodGuiaRemision(String codGuiaRemision) {
        String sql = "SELECT EXISTS (SELECT * FROM traslado WHERE cod_guia_remision = ?)";
        return jdbcTemplate.query(sql, rs -> { rs.next(); return rs.getBoolean(1); }, codGuiaRemision);
    }

    @Override
    public boolean validarRuta(int codRuta) {
        String sql = "SELECT EXISTS (SELECT * FROM ruta WHERE cod_ruta = ?)";
        return jdbcTemplate.query(sql, rs -> { rs.next(); return rs.getBoolean(1); }, codRuta);
    }

    @Override
    public boolean validarVehiculo(String placa) {
        String sql = "SELECT EXISTS (SELECT * FROM vehiculo WHERE placa = ?)";
        return jdbcTemplate.query(sql, rs -> { rs.next(); return rs.getBoolean(1); }, placa);
    }

    @Override
    public boolean validarTransportista(String dni) {
        String sql = "SELECT EXISTS (SELECT * " +
                "FROM transportista t " +
                "INNER JOIN empleado e ON t.cod_empleado = e.cod_empleado " +
                "INNER JOIN persona p ON e.cod_persona = p.cod_persona " +
                "WHERE p.dni = ?)";
        return jdbcTemplate.query(sql, rs -> { rs.next(); return rs.getBoolean(1); }, dni);
    }

    @Override
    public boolean validarPedido(int codPedido) {
        String sql = "SELECT EXISTS (SELECT * FROM pedido WHERE cod_pedido = ?)";
        return jdbcTemplate.query(sql, rs -> { rs.next(); return rs.getBoolean(1); }, codPedido);
    }

    @Override
    public boolean validarEmpleado(String dni) {
        String sql = "SELECT EXISTS (SELECT * " +
                "FROM empleado e " +
                "INNER JOIN persona p on p.cod_persona = e.cod_persona " +
                "WHERE p.dni = ?); ";
        return jdbcTemplate.query(sql, rs -> {rs.next(); return rs.getBoolean(1);}, dni);
    }

    private List<Operacion> obtenerOperaciones(String entradaUsuario, boolean esPrecinto) {
        String obtenerPicking = esPrecinto ?
                "WITH op_picking_cte AS (" +
                        "    SELECT o.id_operacion AS id_op_picking" +
                        "    FROM mercancia m" +
                        "    INNER JOIN operacion o ON m.id_operacion_picking = o.id_operacion" +
                        "    WHERE m.nro_precinto = ?" +
                        ")" :
                "WITH op_picking_cte AS(" +
                        "    SELECT o.id_operacion_picking AS id_op_picking" +
                        "    FROM traslado t" +
                        "    INNER JOIN operacion o on t.id_operacion_inicia = o.id_operacion" +
                        "    WHERE t.cod_guia_remision = ?" +
                        ")";
        String procesoSql = obtenerPicking +
                " SELECT o.id_operacion, o.fecha, o.hora_inicio, o.hora_fin, " +
                "       ot.descripcion AS tipo_operacion, " +
                "       p1.dni AS dni_emp_ejecutor, p2.dni AS dni_emp_supervisor " +
                "FROM operacion o " +
                "INNER JOIN operacion_tipo ot ON o.cod_tipo_operacion = ot.cod_tipo_operacion " +
                "INNER JOIN empleado e1 ON o.cod_empleado_ejecutor = e1.cod_empleado " +
                "INNER JOIN persona p1 ON e1.cod_persona = p1.cod_persona " +
                "INNER JOIN empleado e2 ON o.cod_empleado_supervisor = e2.cod_empleado " +
                "INNER JOIN persona p2 ON e2.cod_persona = p2.cod_persona " +
                "WHERE o.id_operacion = (SELECT id_op_picking FROM op_picking_cte) " +
                "   OR o.id_operacion_picking = (SELECT id_op_picking FROM op_picking_cte) " +
                "ORDER BY o.cod_tipo_operacion;";
        return jdbcTemplate.query(procesoSql, new BeanPropertyRowMapper<>(Operacion.class), entradaUsuario);
    }

    private List<Mercancia> obtenerMercancias(String entradaUsuario, boolean esPrecinto) {
        String PrecintoSql = "SELECT m1.nro_precinto, st.cod_stock, ec.nombre as nombre_stock, ect.descripcion as categoria, " +
                        "ep.descripcion as tipo, se.descripcion as segmento, ecu.descripcion as unidad, " +
                        "dm.cantidad as cantidad_transportar " +
                        "FROM mercancia m1 " +
                        "INNER JOIN mercancia m2 ON m1.id_operacion_picking = m2.id_operacion_picking " +
                        "INNER JOIN detalle_mercancia_stock dm ON m1.id_mercancia = dm.id_mercancia " +
                        "INNER JOIN stock st ON dm.id_stock = st.id_stock " +
                        "INNER JOIN elemento_catalogo ec ON st.id_elemento_catalogo = ec.id_elemento_catalogo " +
                        "INNER JOIN elemento_catalogo_tipo ect ON ec.id_elemento_catalogo_tipo = ect.id_elemento_catalogo_tipo " +
                        "INNER JOIN elemento_produccion ep ON ect.id_elemento_produccion = ep.id_elemento_produccion " +
                        "INNER JOIN elemento_catalogo_unidad ecu ON ec.cod_unidad = ecu.cod_unidad " +
                        "INNER JOIN segmento se ON ect.id_segmento = se.id_segmento " +
                        "WHERE m2.nro_precinto = ? " +
                        "ORDER BY m1.id_mercancia;";
        String RemisionSql = "SELECT m.nro_precinto, st.cod_stock, ec.nombre as nombre_stock, ect.descripcion as categoria, " +
                        "ep.descripcion as tipo, se.descripcion as segmento, ecu.descripcion as unidad, " +
                        "dm.cantidad as cantidad_transportar " +
                        "FROM traslado t " +
                        "INNER JOIN operacion o ON t.id_operacion_inicia = o.id_operacion " +
                        "INNER JOIN mercancia m ON o.id_operacion_picking = m.id_operacion_picking " +
                        "INNER JOIN detalle_mercancia_stock dm ON m.id_mercancia = dm.id_mercancia " +
                        "INNER JOIN stock st ON dm.id_stock = st.id_stock " +
                        "INNER JOIN elemento_catalogo ec ON st.id_elemento_catalogo = ec.id_elemento_catalogo " +
                        "INNER JOIN elemento_catalogo_tipo ect ON ec.id_elemento_catalogo_tipo = ect.id_elemento_catalogo_tipo " +
                        "INNER JOIN elemento_produccion ep ON ect.id_elemento_produccion = ep.id_elemento_produccion " +
                        "INNER JOIN elemento_catalogo_unidad ecu ON ec.cod_unidad = ecu.cod_unidad " +
                        "INNER JOIN segmento se ON ect.id_segmento = se.id_segmento " +
                        "WHERE t.cod_guia_remision = ? " +
                        "ORDER BY m.id_mercancia;";
        String sql = esPrecinto ? PrecintoSql : RemisionSql;
        return jdbcTemplate.query(sql, new MercanciaResultSetExtractor(), entradaUsuario);
    }

    private static class MercanciaResultSetExtractor implements ResultSetExtractor<List<Mercancia>> {
        @Override
        public List<Mercancia> extractData(ResultSet rs) throws SQLException {
            List<Mercancia> mercancias = new ArrayList<>();
            Mercancia mercanciaActual = null;
            while (rs.next()) {
                String nroPrecinto = rs.getString("nro_precinto");
                if (mercanciaActual == null || !nroPrecinto.equals(mercanciaActual.getNroPrecinto())) {
                    mercanciaActual = new Mercancia();
                    mercanciaActual.setNroPrecinto(nroPrecinto);
                    mercanciaActual.setStocks(new ArrayList<>());
                    mercancias.add(mercanciaActual);
                }
                DetalleMercancia detalle = new DetalleMercancia();
                detalle.setCodStock(rs.getString("cod_stock"));
                detalle.setNombreStock(rs.getString("nombre_stock"));
                detalle.setCategoria(rs.getString("categoria"));
                detalle.setTipo(rs.getString("tipo"));
                detalle.setSegmento(rs.getString("segmento"));
                detalle.setUnidad(rs.getString("unidad"));
                detalle.setCantidadTransportar(rs.getInt("cantidad_transportar"));
                mercanciaActual.getStocks().add(detalle);
            }
            return mercancias;
        }
    }

    public Traslado obtenerTraslado(String entradaUsuario, boolean esPrecinto) {
        String sql = "SELECT t.cod_guia_remision, r.cod_ruta, rt.descripcion AS tipo_ruta, r.distancia_total, r.duracion, " +
                "p.dni AS dni_transportista, " +
                "CONCAT(p.primer_apellido, ' ', p.segundo_apellido, ', ', p.prenombre) AS nombre_completo, " +
                "n.descripcion AS nacionalidad, v.placa AS placa_vehiculo, vm.descripcion AS modelo_vehiculo, " +
                "v.anio_fabricacion, v.capacidad_carga, v.fecha_ultimo_mantenimiento, v.fecha_ultimo_viaje " +
                "FROM traslado t " +
                "INNER JOIN transportista tr ON t.cod_transportista = tr.cod_transportista " +
                "INNER JOIN empleado e ON tr.cod_empleado = e.cod_empleado " +
                "INNER JOIN persona p ON e.cod_persona = p.cod_persona " +
                "INNER JOIN nacionalidad n ON p.cod_nacionalidad = n.cod_nacionalidad " +
                "INNER JOIN ruta r ON t.cod_ruta = r.cod_ruta " +
                "INNER JOIN ruta_tipo rt ON r.cod_ruta_tipo = rt.cod_ruta_tipo " +
                "INNER JOIN vehiculo v ON t.cod_vehiculo = v.cod_vehiculo " +
                "INNER JOIN vehiculo_modelo vm ON v.cod_vehiculo_modelo = vm.cod_vehiculo_modelo " +
                (esPrecinto ?
                                "INNER JOIN operacion o ON t.id_operacion_inicia = o.id_operacion " +
                                "INNER JOIN mercancia m ON o.id_operacion_picking = m.id_operacion_picking " +
                                "WHERE m.nro_precinto = ?;"
                        :
                                "WHERE t.cod_guia_remision = ?;");

        return jdbcTemplate.query(sql, new TrasladoResultSetExtractor(), entradaUsuario);
    }

    private static class TrasladoResultSetExtractor implements ResultSetExtractor<Traslado> {
        @Override
        public Traslado extractData(ResultSet rs) throws SQLException {
            if (rs.next()) {
                Traslado traslado = new Traslado();
                traslado.setCodGuiaRemision(rs.getString("cod_guia_remision"));
                traslado.setRuta(new Ruta(
                        rs.getInt("cod_ruta"),
                        rs.getString("tipo_ruta"),
                        rs.getFloat("distancia_total"),
                        rs.getFloat("duracion")
                ));
                traslado.setTransportista(new Transportista(
                        rs.getString("dni_transportista"),
                        rs.getString("nombre_completo"),
                        rs.getString("nacionalidad")
                ));
                traslado.setVehiculo(new Vehiculo(
                        rs.getString("placa_vehiculo"),
                        rs.getString("modelo_vehiculo"),
                        rs.getInt("anio_fabricacion"),
                        rs.getFloat("capacidad_carga"),
                        rs.getDate("fecha_ultimo_mantenimiento"),
                        rs.getDate("fecha_ultimo_viaje")
                ));
                return traslado;
            }
            return null;
        }
    }

    private static String obtenerEstadoProceso(int cantidadOperaciones) {
        if (cantidadOperaciones < 5) {
            return "En preparación para salida";
        } else if (cantidadOperaciones == 5) {
            return "Traslado en curso";
        } else if (cantidadOperaciones == 6) {
            return "Recibido y en preparación para descarga";
        } else if (cantidadOperaciones == 7) {
            return "Proceso finalizado";
        } else {
            return "Estado desconocido";
        }
    }

    private static String obtenerEstadoTraslado(int cantidadOperaciones) {
        if (cantidadOperaciones == 5) {
            return "En curso";
        } else if (cantidadOperaciones > 5) {
            return "Recibido";
        } else {
            return "Estado desconocido";
        }
    }

    private String generarNroPrecinto(int idMercancia, int idOperacion){
        String idMercanciaStr = String.format("%09d", idMercancia);
        String idOperacionStr = String.format("%09d", idOperacion);

        String luhnMercancia = String.valueOf(luhnChecksum(idMercanciaStr));
        String luhnOperacion = String.valueOf(luhnChecksum(idOperacionStr));

        return idMercanciaStr + luhnMercancia + idOperacionStr + luhnOperacion;
    }

    private static int luhnChecksum(String val) {
        int nDigits = val.length();
        int sum = 0;
        boolean isOdd = true;

        for (int i = nDigits - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(val.charAt(i));

            if (isOdd) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = digit - 9;
                }
            }
            sum += digit;
            isOdd = !isOdd;
        }
        return (10 - (sum % 10)) % 10;
    }
}
