/*

package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.DetallePedido;
import model.Pedido;
import model.Producto;

public class Test {

    // ---------- SQL ----------
    private static final String INSERT_SQL = """
        INSERT INTO DetallePedido (idPedido, idProducto, cantidad, precioUnitario)
        VALUES (?, ?, ?, ?)
    """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT idDetalle, idPedido, idProducto, cantidad, precioUnitario
        FROM DetallePedido
        WHERE idDetalle = ?
    """;

    private static final String SELECT_BY_PEDIDO_SQL = """
        SELECT idDetalle, idPedido, idProducto, cantidad, precioUnitario
        FROM DetallePedido
        WHERE idPedido = ?
        ORDER BY idDetalle
    """;

    private static final String SELECT_BY_PRODUCTO_SQL = """
        SELECT idDetalle, idPedido, idProducto, cantidad, precioUnitario
        FROM DetallePedido
        WHERE idProducto = ?
        ORDER BY idDetalle
    """;

    private static final String UPDATE_SQL = """
        UPDATE DetallePedido
        SET idPedido = ?, idProducto = ?, cantidad = ?, precioUnitario = ?
        WHERE idDetalle = ?
    """;

    private static final String DELETE_BY_ID_SQL = """
        DELETE FROM DetallePedido WHERE idDetalle = ?
    """;

    private static final String DELETE_BY_PEDIDO_SQL = """
        DELETE FROM DetallePedido WHERE idPedido = ?
    """;

    private static final String COUNT_BY_PEDIDO_SQL = """
        SELECT COUNT(*) FROM DetallePedido WHERE idPedido = ?
    """;

    private static final String TOTAL_PEDIDO_SQL = """
        SELECT COALESCE(SUM(cantidad * precioUnitario), 0)
        AS total
        FROM DetallePedido
        WHERE idPedido = ?
    """;

    // ---------- CRUD ----------
    /** Crea un detalle. Devuelve el mismo objeto con idDetalle seteado. */
   /* public DetallePedido create(Connection con, DetallePedido d) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, d.getPedido().getIdPedido());       // ajusta getter si difiere
            ps.setString(2, d.getProducto().getIdProducto()); // ajusta getter si difiere
            ps.setInt(3, d.getCantidad());
            ps.setDouble(4, d.getPrecioUnitario());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    d.setIdDetalle(rs.getInt(1));
                }
            }
            return d;
        }
    }

    *//** Obtiene un detalle por id. Retorna null si no existe. *//*
    public DetallePedido findById(Connection con, int idDetalle) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, idDetalle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        }
    }

    *//** Lista todos los detalles de un pedido. *//*
    public List<DetallePedido> findByPedido(Connection con, int idPedido) throws SQLException {
        List<DetallePedido> out = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SELECT_BY_PEDIDO_SQL)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapRow(rs));
            }
        }
        return out;
    }

    *//** Lista todos los detalles por producto. *//*
    public List<DetallePedido> findByProducto(Connection con, String idProducto) throws SQLException {
        List<DetallePedido> out = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SELECT_BY_PRODUCTO_SQL)) {
            ps.setString(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapRow(rs));
            }
        }
        return out;
    }

    *//** Actualiza un detalle existente (match por idDetalle). Devuelve true si afectó una fila. *//*
    public boolean update(Connection con, DetallePedido d) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {
            ps.setInt(1, d.getPedido().getIdPedido());
            ps.setString(2, d.getProducto().getIdProducto());
            ps.setInt(3, d.getCantidad());
            ps.setDouble(4, d.getPrecioUnitario());
            ps.setInt(5, d.getIdDetalle());
            return ps.executeUpdate() == 1;
        }
    }

    *//** Borra un detalle por id. *//*
    public boolean deleteById(Connection con, int idDetalle) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(DELETE_BY_ID_SQL)) {
            ps.setInt(1, idDetalle);
            return ps.executeUpdate() == 1;
        }
    }

    *//** Borra todos los detalles de un pedido (útil al rearmar un carrito/factura). *//*
    public int deleteByPedido(Connection con, int idPedido) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(DELETE_BY_PEDIDO_SQL)) {
            ps.setInt(1, idPedido);
            return ps.executeUpdate(); // cantidad de filas borradas
        }
    }

    // ---------- Utilitarios de consulta ----------
    public int countByPedido(Connection con, int idPedido) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(COUNT_BY_PEDIDO_SQL)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public double totalByPedido(Connection con, int idPedido) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(TOTAL_PEDIDO_SQL)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getDouble("total");
            }
        }
    }

    // ---------- Batch para rendimiento (ej.: crear varios detalles juntos) ----------
    public int[] createBatch(Connection con, List<DetallePedido> detalles) throws SQLException {
        boolean oldAuto = con.getAutoCommit();
        con.setAutoCommit(false);
        try (PreparedStatement ps = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            for (DetallePedido d : detalles) {
                ps.setInt(1, d.getPedido().getIdPedido());
                ps.setString(2, d.getProducto().getIdProducto());
                ps.setInt(3, d.getCantidad());
                ps.setDouble(4, d.getPrecioUnitario());
                ps.addBatch();
            }
            int[] res = ps.executeBatch();

            // Opcional: leer keys generadas en orden
            try (ResultSet rs = ps.getGeneratedKeys()) {
                int i = 0;
                while (rs.next() && i < detalles.size()) {
                    detalles.get(i++).setIdDetalle(rs.getInt(1));
                }
            }

            con.commit();
            return res;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(oldAuto);
        }
    }

    // ---------- Mapper ----------
    private DetallePedido mapRow(ResultSet rs) throws SQLException {
        int idDetalle = rs.getInt("idDetalle");
        int idPedido = rs.getInt("idPedido");
        String idProducto = rs.getString("idProducto");
        int cantidad = rs.getInt("cantidad");
        double precioUnitario = rs.getDouble("precioUnitario");

        // Ajustá estos constructores si tus clases difieren
        Pedido p = new Pedido(idPedido);
        Producto pr = new Producto(idProducto);

        return new DetallePedido(idDetalle, p, pr, cantidad, precioUnitario);
    }
}
 */