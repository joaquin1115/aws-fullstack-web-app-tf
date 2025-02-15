package com.sanfernando.sanfernando.dao.daoImpl;

import com.sanfernando.sanfernando.dao.ReclamoDao;
import com.sanfernando.sanfernando.dtos.requests.reclamos.ReclamoFormCreateRequest;
import com.sanfernando.sanfernando.dtos.responses.reclamos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ReclamoDaoImpl implements ReclamoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ReclamoListaResponse> obtenerReclamosLista() {
        String sql =
                "SELECT " +
                        "r.cod_reclamo, " +
                        "er.descripcion, " +
                        "r.fecha_reclamo, " +
                        "c.nombre || ' (' || c.ruc || ')' AS \"cliente (ruc)\", " +
                        "ct.tipo_cliente, " +
                        "rt.cod_tipo_reclamo, " +
                        "c1.nombre AS \"área responsable\" " +
                        "FROM reclamo r " +
                        "INNER JOIN estado_reclamo er ON r.cod_estado_reclamo = er.cod_estado_reclamo " +
                        "INNER JOIN representante re ON r.cod_representante = re.cod_representante " +
                        "INNER JOIN cliente c ON re.cod_cliente = c.cod_cliente " +
                        "INNER JOIN cliente_tipo ct ON c.cod_cliente_tipo = ct.cod_cliente_tipo " +
                        "INNER JOIN reclamo_tipo rt ON r.cod_tipo_reclamo = rt.cod_tipo_reclamo " +
                        "INNER JOIN seguimiento s ON r.cod_seguimiento = s.cod_seguimiento " +
                        "INNER JOIN cliente c1 ON s.cod_cliente_interno = c1.cod_cliente " +
                        "ORDER BY cod_reclamo";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                ReclamoListaResponse.builder()
                        .codReclamo(rs.getString("cod_reclamo"))
                        .estadoReclamo(rs.getString("descripcion"))
                        .fechaReclamo(rs.getString("fecha_reclamo"))
                        .clienteRuc(rs.getString("cliente (ruc)"))
                        .tipoCliente(rs.getString("tipo_cliente"))
                        .tipoReclamo(rs.getString("cod_tipo_reclamo"))
                        .areaRes(rs.getString("área responsable"))
                        .build()
        );
    }

    @Override
    public void insertarReclamo(ReclamoFormCreateRequest reclamoDTO) throws Exception {
        // 1. Get client and representative data
        String clientQuery =
                "SELECT c.cod_cliente, r.cod_representante, p.direccion, r.correo_empresarial " +
                        "FROM cliente c " +
                        "JOIN representante r ON c.cod_cliente = r.cod_cliente " +
                        "JOIN persona p ON r.cod_persona = p.cod_persona " +
                        "WHERE c.nombre = ? AND p.prenombre = ? AND r.cargo = ?";

        Record clientData = jdbcTemplate.queryForObject(clientQuery,
                (rs, rowNum) -> new Record(
                        rs.getInt("cod_cliente"),
                        rs.getInt("cod_representante")
                ),
                reclamoDTO.getNombCliente(),
                reclamoDTO.getNomRepresentante(),
                reclamoDTO.getCargoRepresentante()
        );

        if (clientData == null) {
            throw new Exception("No se encontró el cliente o representante especificado");
        }

        // 2. Insert seguimiento and get ID
        String seguimientoSql =
                "INSERT INTO seguimiento (cod_cliente_interno, cod_tipo_accion, comentario, fecha_resolucion, numero_caso) " +
                        "VALUES (?, (SELECT cod_tipo_accion FROM accion_tipo WHERE descripcion = ?), ?, ?, ?) " +
                        "RETURNING cod_seguimiento";

        KeyHolder seguimientoKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(seguimientoSql, new String[]{"cod_seguimiento"});
            ps.setInt(1, clientData.codCliente());
            ps.setString(2, reclamoDTO.getAccionSolicitada());
            ps.setString(3, reclamoDTO.getComentario());
            ps.setDate(4, java.sql.Date.valueOf(reclamoDTO.getFechaEsperada()));
            ps.setInt(5, Integer.parseInt(reclamoDTO.getNroCaso()));
            return ps;
        }, seguimientoKeyHolder);

        int codSeguimiento = seguimientoKeyHolder.getKey().intValue();

        // 3. Insert reclamo and get ID
        String reclamoSql =
                "INSERT INTO reclamo (cod_representante, cod_pedido, cod_seguimiento, cod_tipo_reclamo, " +
                        "cod_nivel_urgencia, cod_estado_reclamo, comentario, fecha_suceso, fecha_reclamo) " +
                        "VALUES (?, (SELECT cod_pedido FROM pedido WHERE cod_ticket = ?), ?, " +
                        "(SELECT cod_tipo_reclamo FROM reclamo_tipo WHERE descripcion = ?), " +
                        "(SELECT cod_nivel_urgencia FROM nivel_urgencia WHERE descripcion = ?), " +
                        "(SELECT cod_estado_reclamo FROM estado_reclamo WHERE descripcion = ?), ?, ?, CURRENT_DATE) " +
                        "RETURNING cod_reclamo";

        KeyHolder reclamoKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(reclamoSql, new String[]{"cod_reclamo"});
            ps.setInt(1, clientData.codRepresentante());
            ps.setInt(2, reclamoDTO.getCodticket());
            ps.setInt(3, codSeguimiento);
            ps.setString(4, reclamoDTO.getTipoReclamo());
            ps.setString(5, reclamoDTO.getUrgencia());
            ps.setString(6, reclamoDTO.getEstadoReclamo());
            ps.setString(7, reclamoDTO.getDescripcionProblema());
            ps.setDate(8, java.sql.Date.valueOf(reclamoDTO.getFechaIncidencia()));
            return ps;
        }, reclamoKeyHolder);

        int codReclamo = reclamoKeyHolder.getKey().intValue();

        // 4. Insert evidencia
        String evidenciaSql =
                "INSERT INTO evidencia (cod_reclamo, cod_tipo_evidencia, cod_tipo_archivo, nombre_evidencia) " +
                        "VALUES (?, (SELECT cod_tipo_evidencia FROM evidencia_tipo WHERE descripcion = ?), " +
                        "(SELECT cod_tipo_archivo FROM archivo_tipo WHERE descripcion = ?), ?)";

        jdbcTemplate.update(evidenciaSql,
                codReclamo,
                reclamoDTO.getTipoEvidencia(),
                reclamoDTO.getTipoArchivo(),
                reclamoDTO.getNombreEvidencia()
        );
    }

    @Override
    public List<ReclamoRepresentanteListaResponse> obtenerRepresentantesLista(int empresaId) {
        String sql =
                "SELECT " +
                        "re.cod_representante, " +
                        "CONCAT(pe.prenombre, ' ', pe.primer_apellido, ' ', pe.segundo_apellido) AS representante, " +
                        "re.cargo, " +
                        "re.correo_empresarial, " +
                        "pe.direccion " +
                        "FROM representante AS re " +
                        "INNER JOIN cliente AS cl ON cl.cod_cliente = re.cod_cliente " +
                        "INNER JOIN persona AS pe ON pe.cod_persona = re.cod_persona " +
                        "WHERE cl.cod_cliente = ?";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> ReclamoRepresentanteListaResponse.builder()
                        .codRepresentante(rs.getInt("cod_representante"))
                        .nombreRepresentante(rs.getString("representante"))
                        .cargoRepresentante(rs.getString("cargo"))
                        .correoEmpresarial(rs.getString("correo_empresarial"))
                        .direccion(rs.getString("direccion"))
                        .build(),
                empresaId
        );
    }

    @Override
    public List<ReclamoEmpresaListaResponse> obtenerEmpresas() {
        String sql = "SELECT cl.cod_cliente, cl.nombre FROM cliente AS cl";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> ReclamoEmpresaListaResponse.builder()
                        .idCliente(rs.getInt("cod_cliente"))
                        .nombre(rs.getString("nombre"))
                        .build()
        );
    }

    @Override
    public List<ReclamoTicketListaResponse> obtenerTickets(int idRepresentante) {
        String sql =
                "SELECT ti.cod_ticket FROM pedido AS pe " +
                        "INNER JOIN representante AS re ON re.cod_representante = pe.cod_representante " +
                        "INNER JOIN ticket AS ti ON ti.cod_ticket = pe.cod_ticket " +
                        "WHERE re.cod_representante = ?";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> ReclamoTicketListaResponse.builder()
                        .idTicket(rs.getInt("cod_ticket"))
                        .build(),
                idRepresentante
        );
    }

    @Override
    public List<ReclamoTicketProductoListaResponse> obtenerProductos(int idTicket) {
        String sql =
                "SELECT ec.id_elemento_catalogo, ec.nombre FROM ticket AS ti " +
                        "INNER JOIN detalle_ticket_producto AS dtp ON dtp.cod_ticket = ti.cod_ticket " +
                        "INNER JOIN elemento_catalogo AS ec ON ec.id_elemento_catalogo = dtp.id_elemento_catalogo " +
                        "WHERE ti.cod_ticket = ?";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> ReclamoTicketProductoListaResponse.builder()
                        .idProducto(rs.getInt("id_elemento_catalogo"))
                        .nombre(rs.getString("nombre"))
                        .build(),
                idTicket
        );
    }

    @Override
    public List<ReclamoTicketProductoDetalleResponse> obtenerProductoDetalle(int idTicket, int idProducto) {
        String sql =
                "SELECT ti.fecha_entrega, dtp.cantidad, st.nro_lote, ec.nombre " +
                        "FROM ticket AS ti " +
                        "INNER JOIN detalle_ticket_producto AS dtp ON dtp.cod_ticket = ti.cod_ticket " +
                        "INNER JOIN elemento_catalogo AS ec ON ec.id_elemento_catalogo = dtp.id_elemento_catalogo " +
                        "LEFT JOIN stock AS st ON st.id_elemento_catalogo = ec.id_elemento_catalogo " +
                        "WHERE ti.cod_ticket = ? AND ec.id_elemento_catalogo = ?";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> ReclamoTicketProductoDetalleResponse.builder()
                        .fecha(rs.getString("fecha_entrega"))
                        .cantidad(rs.getInt("cantidad"))
                        .nroLote(rs.getString("nro_lote"))
                        .nombre(rs.getString("nombre"))
                        .build(),
                idTicket, idProducto
        );
    }

    @Override
    public ReclamoVisorResponse obtenerVisor(int idReclamo) {
        ReclamoVisorResponse visor = new ReclamoVisorResponse();

        // Get client info
        String clienteSql =
                "SELECT " +
                        "cl.nombre, " +
                        "CONCAT(pe.prenombre,' ',pe.primer_apellido,' ',pe.segundo_apellido) representante, " +
                        "cargo, " +
                        "correo_empresarial, " +
                        "pe.direccion " +
                        "FROM reclamo r " +
                        "INNER JOIN representante AS re ON re.cod_representante = r.cod_representante " +
                        "INNER JOIN cliente AS cl ON cl.cod_cliente = re.cod_cliente " +
                        "INNER JOIN persona AS pe ON pe.cod_persona = re.cod_persona " +
                        "WHERE r.cod_reclamo = ?";

        visor.setCliente(jdbcTemplate.queryForObject(clienteSql,
                (rs, rowNum) -> ReclamoVisorClienteResponse.builder()
                        .nombre(rs.getString("nombre"))
                        .representante(rs.getString("representante"))
                        .cargo(rs.getString("cargo"))
                        .correoEmpresarial(rs.getString("correo_empresarial"))
                        .direccion(rs.getString("direccion"))
                        .build(),
                idReclamo
        ));

        // Get detail info
        String detalleSql =
                "SELECT " +
                        "ti.cod_ticket, " +
                        "ec.nombre, " +
                        "ti.fecha_entrega, " +
                        "st.nro_lote, " +
                        "dtip.cantidad " +
                        "FROM reclamo r " +
                        "INNER JOIN pedido AS pe ON r.cod_pedido = pe.cod_pedido " +
                        "INNER JOIN ticket AS ti ON pe.cod_ticket = ti.cod_ticket " +
                        "INNER JOIN detalle_ticket_producto AS dtip ON ti.cod_ticket = dtip.cod_ticket " +
                        "INNER JOIN elemento_catalogo AS ec ON dtip.id_elemento_catalogo = ec.id_elemento_catalogo " +
                        "INNER JOIN stock AS st ON ec.id_elemento_catalogo = st.id_elemento_catalogo " +
                        "WHERE r.cod_reclamo = ?";

        visor.setDetalle(jdbcTemplate.queryForObject(detalleSql,
                (rs, rowNum) -> ReclamoVisorDetalleResponse.builder()
                        .idTicket(rs.getInt("cod_ticket"))
                        .fechaEntrega(rs.getString("fecha_entrega"))
                        .cantidad(rs.getInt("cantidad"))
                        .nroLote(rs.getString("nro_lote"))
                        .nombre(rs.getString("nombre"))
                        .build(),
                idReclamo
        ));

        // Get naturaleza info
        String naturalezaSql =
                "SELECT " +
                        "rt.descripcion, " +
                        "r.comentario, " +
                        "r.fecha_suceso, " +
                        "r.fecha_reclamo, " +
                        "nu.descripcion AS nivel_urgencia " +
                        "FROM reclamo r " +
                        "INNER JOIN reclamo_tipo AS rt ON r.cod_tipo_reclamo = rt.cod_tipo_reclamo " +
                        "INNER JOIN nivel_urgencia AS nu ON r.cod_nivel_urgencia = nu.cod_nivel_urgencia " +
                        "WHERE r.cod_reclamo = ?";

        visor.setNaturaleza(jdbcTemplate.queryForObject(naturalezaSql,
                (rs, rowNum) -> ReclamoVisorNaturalezaResponse.builder()
                        .descripcion(rs.getString("descripcion"))
                        .comentario(rs.getString("comentario"))
                        .fechaSuceso(rs.getString("fecha_suceso"))
                        .fechaReclamo(rs.getString("fecha_reclamo"))
                        .descripcionNivelUrgencia(rs.getString("nivel_urgencia"))
                        .build(),
                idReclamo
        ));

        // Get evidencias
        String evidenciasSql =
                "SELECT " +
                        "CONCAT(ev.nombre_evidencia,'.',at.descripcion) evidencia " +
                        "FROM reclamo r " +
                        "INNER JOIN evidencia AS ev ON r.cod_reclamo = ev.cod_reclamo " +
                        "INNER JOIN archivo_tipo AS at ON ev.cod_tipo_archivo = at.cod_tipo_archivo " +
                        "WHERE r.cod_reclamo = ?";

        List<ReclamoVisorEvidenciaResponse> evidencias = jdbcTemplate.query(evidenciasSql,
                (rs, rowNum) -> ReclamoVisorEvidenciaResponse.builder()
                        .evidencia(rs.getString("evidencia"))
                        .build(),
                idReclamo
        );
        visor.setEvidencias(evidencias);

        // Get resolución info
        String resolucionSql =
                "SELECT " +
                        "cl.nombre, " +
                        "at.descripcion, " +
                        "se.comentario " +
                        "FROM reclamo r " +
                        "INNER JOIN seguimiento AS se ON r.cod_seguimiento = se.cod_seguimiento " +
                        "INNER JOIN accion_tipo AS at ON se.cod_tipo_accion = at.cod_tipo_accion " +
                        "INNER JOIN cliente AS cl ON se.cod_cliente_interno = cl.cod_cliente " +
                        "WHERE r.cod_reclamo = ?";

        visor.setResolucion(jdbcTemplate.queryForObject(resolucionSql,
                (rs, rowNum) -> ReclamoVisorResolucionResponse.builder()
                        .nombre(rs.getString("nombre"))
                        .descripcion(rs.getString("descripcion"))
                        .comentario(rs.getString("comentario"))
                        .build(),
                idReclamo
        ));

        // Get seguimiento info
        String seguimientoSql =
                "SELECT " +
                        "se.fecha_resolucion, " +
                        "se.numero_caso, " +
                        "er.descripcion " +
                        "FROM reclamo r " +
                        "INNER JOIN seguimiento AS se ON r.cod_seguimiento = se.cod_seguimiento " +
                        "INNER JOIN estado_reclamo AS er ON r.cod_estado_reclamo = er.cod_estado_reclamo " +
                        "WHERE r.cod_reclamo = ?";

        visor.setSeguimiento(jdbcTemplate.queryForObject(seguimientoSql,
                (rs, rowNum) -> ReclamoVisorSeguimientoResponse.builder()
                        .fechaResolucion(rs.getString("fecha_resolucion"))
                        .numeroCaso(rs.getString("numero_caso"))
                        .descripcionEstadoReclamo(rs.getString("descripcion"))
                        .build(),
                idReclamo
        ));

        return visor;
    }

    // Helper record for client data
    private record Record(int codCliente, int codRepresentante) {}
}