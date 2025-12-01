package dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.DbConfig;
import dao.interfaces.IPedidoDAO;
import model.DetallePedido;
import model.Pedido;
import model.Producto;
import model.Usuario;

public class PedidoDAOImpl implements IPedidoDAO {

	public PedidoDAOImpl() {
		// Constructor
	}

	@Override
	public List<Pedido> findAll() {
		final String SQL = "SELECT * FROM pedido";
		List<Pedido> pedidos = new ArrayList<>();
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Pedido pedido = new Pedido();
				pedido.setIdPedido(rs.getInt("id_pedido"));
				pedido.setFecha(rs.getDate("fecha"));
				pedido.setEstado(rs.getBoolean("estado"));
				pedido.setDireccionEntrega(rs.getString("direccion_entrega"));
				pedido.setTotal(rs.getDouble("total"));

				// Crear objeto Usuario con solo el ID
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(rs.getInt("id_usuario"));
				pedido.setUsuario(usuario);

				pedidos.add(pedido);
			}
			return pedidos;
		} catch (Exception e) {
			System.out.println("Error al obtener todos los pedidos: " + e.getMessage());
			return pedidos;
		}
	}

	@Override
	public Pedido findById(int idPedido) {
		final String SQL = "SELECT * FROM pedido WHERE id_pedido = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idPedido);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Pedido pedido = new Pedido();
				pedido.setIdPedido(rs.getInt("id_pedido"));
				pedido.setFecha(rs.getDate("fecha"));
				pedido.setEstado(rs.getBoolean("estado"));
				pedido.setDireccionEntrega(rs.getString("direccion_entrega"));
				pedido.setTotal(rs.getDouble("total"));

				// Crear objeto Usuario con solo el ID
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(rs.getInt("id_usuario"));
				pedido.setUsuario(usuario);

				return pedido;
			}
			return null;
		} catch (Exception e) {
			System.out.println("Error al buscar pedido por ID: " + e.getMessage());
			return null;
		}
	}

	@Override
	public Pedido create(Pedido pedido) {
		final String SQL = "INSERT INTO pedido (fecha, id_usuario, estado, direccion_entrega, total) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setDate(1, pedido.getFecha());
			ps.setInt(2, pedido.getUsuario().getIdUsuario());
			ps.setBoolean(3, pedido.getEstado());
			ps.setString(4, pedido.getDireccionEntrega());
			ps.setDouble(5, pedido.getTotal());

			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new Exception("Creating pedido failed, no rows affected.");
			}

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					pedido.setIdPedido(generatedKeys.getInt(1));
				} else {
					throw new Exception("Creating pedido failed, no ID obtained.");
				}
			}
			return pedido;
		} catch (Exception e) {
			System.out.println("Error al crear el pedido: " + e.getMessage());
			return null;
		}
	}

	@Override
	public Pedido update(Pedido pedido) {
		final String SQL = "UPDATE pedido SET fecha = ?, id_usuario = ?, estado = ?, direccion_entrega = ?, total = ? WHERE id_pedido = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setDate(1, pedido.getFecha());
			ps.setInt(2, pedido.getUsuario().getIdUsuario());
			ps.setBoolean(3, pedido.getEstado());
			ps.setString(4, pedido.getDireccionEntrega());
			ps.setDouble(5, pedido.getTotal());
			ps.setInt(6, pedido.getIdPedido());

			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				return pedido;
			}
			return null;
		} catch (Exception e) {
			System.out.println("Error al actualizar el pedido: " + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean deleteById(int idPedido) {
		final String SQL = "DELETE FROM pedido WHERE id_pedido = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idPedido);
			int affectedRows = ps.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			System.out.println("Error al eliminar el pedido: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean existsById(int idPedido) {
		final String SQL = "SELECT COUNT(*) FROM pedido WHERE id_pedido = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idPedido);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
			return false;
		} catch (Exception e) {
			System.out.println("Error al verificar si existe el pedido: " + e.getMessage());
			return false;
		}
	}

	@Override
	public int count() {
		final String SQL = "SELECT COUNT(*) FROM pedido";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (Exception e) {
			System.out.println("Error al contar pedidos: " + e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Pedido> findByUsuarioId(int idUsuario) {
		final String SQL = "SELECT * FROM pedido WHERE id_usuario = ?";
		List<Pedido> pedidos = new ArrayList<>();
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idUsuario);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Pedido pedido = new Pedido();
				pedido.setIdPedido(rs.getInt("id_pedido"));
				pedido.setFecha(rs.getDate("fecha"));
				pedido.setEstado(rs.getBoolean("estado"));
				pedido.setDireccionEntrega(rs.getString("direccion_entrega"));
				pedido.setTotal(rs.getDouble("total"));

				// Crear objeto Usuario con solo el ID
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(rs.getInt("id_usuario"));
				pedido.setUsuario(usuario);

				pedidos.add(pedido);
			}
			return pedidos;
		} catch (Exception e) {
			System.out.println("Error al buscar pedidos por usuario: " + e.getMessage());
			return pedidos;
		}
	}

	@Override
	public List<Pedido> findByEstado(boolean estado) {
		final String SQL = "SELECT * FROM pedido WHERE estado = ?";
		List<Pedido> pedidos = new ArrayList<>();
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setBoolean(1, estado);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Pedido pedido = new Pedido();
				pedido.setIdPedido(rs.getInt("id_pedido"));
				pedido.setFecha(rs.getDate("fecha"));
				pedido.setEstado(rs.getBoolean("estado"));
				pedido.setDireccionEntrega(rs.getString("direccion_entrega"));
				pedido.setTotal(rs.getDouble("total"));

				// Crear objeto Usuario con solo el ID
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(rs.getInt("id_usuario"));
				pedido.setUsuario(usuario);

				pedidos.add(pedido);
			}
			return pedidos;
		} catch (Exception e) {
			System.out.println("Error al buscar pedidos por estado: " + e.getMessage());
			return pedidos;
		}
	}

	@Override
	public List<DetallePedido> findDetallesByPedidoId(int idPedido) {
	    // Hacemos JOIN con producto para traer también el nombre
	    final String SQL = 
	        "SELECT d.*, p.nombre AS nombre_producto " +
	        "FROM detalle_pedido d " +
	        "JOIN producto p ON d.id_producto = p.id_producto " +
	        "WHERE d.id_pedido = ?";

	    List<DetallePedido> detalles = new ArrayList<>();
	    try (Connection connection = DbConfig.getInstance().getConnection();
	         PreparedStatement ps = connection.prepareStatement(SQL)) {

	        ps.setInt(1, idPedido);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            DetallePedido detalle = new DetallePedido();
	            detalle.setIdDetalle(rs.getInt("id_detalle"));
	            detalle.setCantidad(rs.getInt("cantidad"));
	            detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));

	            // Pedido solo con ID
	            Pedido pedido = new Pedido();
	            pedido.setIdPedido(rs.getInt("id_pedido"));
	            detalle.setPedido(pedido);

	            // Producto con ID + NOMBRE
	            Producto producto = new Producto();
	            producto.setIdProducto(rs.getString("id_producto"));
	            producto.setNombre(rs.getString("nombre_producto")); 
	            detalle.setProducto(producto);

	            detalles.add(detalle);
	        }
	        return detalles;
	    } catch (Exception e) {
	        System.out.println("Error al buscar detalles del pedido: " + e.getMessage());
	        return detalles;
	    }
	}


	@Override
	public boolean addDetallePedido(DetallePedido detalle) {
		final String SQL = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario) "
				+ "VALUES (?, ?, ?, ?) " + "ON DUPLICATE KEY UPDATE " + "cantidad = cantidad + VALUES(cantidad), "
				+ "precio_unitario = VALUES(precio_unitario)";

		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {

			ps.setInt(1, detalle.getPedido().getIdPedido());
			ps.setString(2, detalle.getProducto().getIdProducto());
			ps.setInt(3, detalle.getCantidad());
			ps.setDouble(4, detalle.getPrecioUnitario());

			int affectedRows = ps.executeUpdate();
			return affectedRows > 0;

		} catch (Exception e) {
			System.out.println("Error al agregar/actualizar detalle del pedido: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateEstadoPedido(int idPedido, boolean nuevoEstado) {
		final String SQL = "UPDATE pedido SET estado = ? WHERE id_pedido = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setBoolean(1, nuevoEstado);
			ps.setInt(2, idPedido);
			int affectedRows = ps.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			System.out.println("Error al actualizar estado del pedido: " + e.getMessage());
			return false;
		}
	}

	@Override
	public Double calcularTotalPedido(int idPedido) {
		final String SQL = "SELECT SUM(cantidad * precio_unitario) as total FROM detalle_pedido WHERE id_pedido = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idPedido);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("total");
			}
			return 0.0;
		} catch (Exception e) {
			System.out.println("Error al calcular total del pedido: " + e.getMessage());
			return null;
		}
	}

	@Override
	public DetallePedido findDetalleByPedidoAndProducto(int idPedido, String idProducto) {
		final String SQL = "SELECT * FROM detalle_pedido WHERE id_pedido = ? AND id_producto = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idPedido);
			ps.setString(2, idProducto);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					DetallePedido d = new DetallePedido();
					d.setIdDetalle(rs.getInt("id_detalle"));
					d.setCantidad(rs.getInt("cantidad"));
					d.setPrecioUnitario(rs.getDouble("precio_unitario"));

					Pedido p = new Pedido();
					p.setIdPedido(rs.getInt("id_pedido"));
					d.setPedido(p);

					Producto prod = new Producto();
					prod.setIdProducto(rs.getString("id_producto"));
					d.setProducto(prod);

					return d;
				}
			}
		} catch (Exception e) {
			System.out.println("Error en findDetalleByPedidoAndProducto: " + e.getMessage());
		}
		return null;
	}

	@Override
	public boolean updateCantidadDetalle(int idDetalle, int nuevaCantidad) {
		final String SQL = "UPDATE detalle_pedido SET cantidad = ? WHERE id_detalle = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, nuevaCantidad);
			ps.setInt(2, idDetalle);
			int affected = ps.executeUpdate();
			return affected > 0;
		} catch (Exception e) {
			System.out.println("Error en updateCantidadDetalle: " + e.getMessage());
			return false;
		}
	}

}
