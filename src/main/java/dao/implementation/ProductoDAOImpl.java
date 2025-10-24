package dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.DbConfig;
import dao.interfaces.IProductoDAO;
import model.Producto;

public class ProductoDAOImpl implements IProductoDAO{

	public ProductoDAOImpl() {
		// Constructor
	}

	@Override
	public List<Producto> findAll() {
		final String SQL = "SELECT * FROM producto";
		List<Producto> productos = new ArrayList<>();
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Producto producto = new Producto();
				producto.setIdProducto(rs.getString("id_producto"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setPrecio(rs.getDouble("precio"));
				producto.setDescuento(rs.getFloat("descuento"));
				producto.setUrlImagen(rs.getString("imagen_url"));
				producto.setIdCategoria(rs.getInt("id_categoria"));
				productos.add(producto);
			}
			return productos;
		} catch (Exception e) {
			System.out.println("Error al obtener todos los productos: " + e.getMessage());
			return productos;
		}
	}

	@Override
	public Producto findById(String idProducto) {
		final String SQL = "SELECT * FROM producto WHERE id_producto = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, idProducto);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Producto producto = new Producto();
				producto.setIdProducto(rs.getString("id_producto"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setPrecio(rs.getDouble("precio"));
				producto.setDescuento(rs.getFloat("descuento"));
				producto.setUrlImagen(rs.getString("imagen_url"));
				producto.setIdCategoria(rs.getInt("id_categoria"));
				return producto;
			}
			return null;
		} catch (Exception e) {
			System.out.println("Error al buscar producto por ID: " + e.getMessage());
			return null;
		}
	}

	@Override
	public Producto create(Producto producto) {
		final String SQL = "INSERT INTO producto (id_producto, nombre, descripcion, precio, descuento, imagen_url, id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, producto.getIdProducto());
			ps.setString(2, producto.getNombre());
			ps.setString(3, producto.getDescripcion());
			ps.setDouble(4, producto.getPrecio());
			ps.setFloat(5, producto.getDescuento());
			ps.setString(6, producto.getUrlImagen());
			ps.setInt(7, producto.getIdCategoria());
			
			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				return producto;
			}
			return null;
		} catch (Exception e) {
			System.out.println("Error al crear el producto: " + e.getMessage());
			return null;
		}
	}

	@Override
	public Producto update(Producto producto) {
		final String SQL = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ?, descuento = ?, imagen_url = ?, idCategoria = ? WHERE id_producto = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, producto.getNombre());
			ps.setString(2, producto.getDescripcion());
			ps.setDouble(3, producto.getPrecio());
			ps.setFloat(4, producto.getDescuento());
			ps.setString(5, producto.getUrlImagen());
			ps.setInt(6, producto.getIdCategoria());
			ps.setString(7, producto.getIdProducto());
			
			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				return producto;
			}
			return null;
		} catch (Exception e) {
			System.out.println("Error al actualizar el producto: " + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean deleteById(String idProducto) {
		final String SQL = "DELETE FROM producto WHERE id_producto = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, idProducto);
			int affectedRows = ps.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			System.out.println("Error al eliminar el producto: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean existsById(String idProducto) {
		final String SQL = "SELECT COUNT(*) FROM producto WHERE id_producto = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, idProducto);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
			return false;
		} catch (Exception e) {
			System.out.println("Error al verificar si existe el producto: " + e.getMessage());
			return false;
		}
	}

	@Override
	public int count() {
		final String SQL = "SELECT COUNT(*) FROM producto";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (Exception e) {
			System.out.println("Error al contar productos: " + e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Producto> findByCategoria(int idCategoria) {
		final String SQL = "SELECT * FROM producto WHERE id_categoria = ?";
		List<Producto> productos = new ArrayList<>();
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idCategoria);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Producto producto = new Producto();
				producto.setIdProducto(rs.getString("id_producto"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setPrecio(rs.getDouble("precio"));
				producto.setDescuento(rs.getFloat("descuento"));
				producto.setUrlImagen(rs.getString("imagen_url"));
				producto.setIdCategoria(rs.getInt("id_categoria"));
				productos.add(producto);
			}
			return productos;
		} catch (Exception e) {
			System.out.println("Error al buscar productos por categoría: " + e.getMessage());
			return productos;
		}
	}

	@Override
	public List<Producto> findByNombre(String nombre) {
		final String SQL = "SELECT * FROM producto WHERE nombre LIKE ?";
		List<Producto> productos = new ArrayList<>();
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Producto producto = new Producto();
				producto.setIdProducto(rs.getString("id_producto"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setPrecio(rs.getDouble("precio"));
				producto.setDescuento(rs.getFloat("descuento"));
				producto.setUrlImagen(rs.getString("imagen_url"));
				producto.setIdCategoria(rs.getInt("id_categoria"));
				productos.add(producto);
			}
			return productos;
		} catch (Exception e) {
			System.out.println("Error al buscar productos por nombre: " + e.getMessage());
			return productos;
		}
	}

	@Override
	public List<Producto> findByPrecioRango(Double precioMin, Double precioMax) {
		final String SQL = "SELECT * FROM producto WHERE precio BETWEEN ? AND ?";
		List<Producto> productos = new ArrayList<>();
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setDouble(1, precioMin);
			ps.setDouble(2, precioMax);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Producto producto = new Producto();
				producto.setIdProducto(rs.getString("id_producto"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setPrecio(rs.getDouble("precio"));
				producto.setDescuento(rs.getFloat("descuento"));
				producto.setUrlImagen(rs.getString("imagen_url"));
				producto.setIdCategoria(rs.getInt("idCategoria"));
				productos.add(producto);
			}
			return productos;
		} catch (Exception e) {
			System.out.println("Error al buscar productos por rango de precio: " + e.getMessage());
			return productos;
		}
	}

	@Override
	public List<Producto> findConDescuento() {
		final String SQL = "SELECT * FROM producto WHERE descuento > 0";
		List<Producto> productos = new ArrayList<>();
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Producto producto = new Producto();
				producto.setIdProducto(rs.getString("id_producto"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setPrecio(rs.getDouble("precio"));
				producto.setDescuento(rs.getFloat("descuento"));
				producto.setUrlImagen(rs.getString("imagen_url"));
				producto.setIdCategoria(rs.getInt("idCategoria"));
				productos.add(producto);
			}
			return productos;
		} catch (Exception e) {
			System.out.println("Error al buscar productos con descuento: " + e.getMessage());
			return productos;
		}
	}

	@Override
	public boolean updatePrecio(String idProducto, Double nuevoPrecio) {
		final String SQL = "UPDATE producto SET precio = ? WHERE id_producto = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setDouble(1, nuevoPrecio);
			ps.setString(2, idProducto);
			int affectedRows = ps.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			System.out.println("Error al actualizar precio: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateDescuento(String idProducto, float nuevoDescuento) {
		final String SQL = "UPDATE producto SET descuento = ? WHERE id_producto = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setFloat(1, nuevoDescuento);
			ps.setString(2, idProducto);
			int affectedRows = ps.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			System.out.println("Error al actualizar descuento: " + e.getMessage());
			return false;
		}
	}

	@Override
	public Double calcularPrecioFinal(String idProducto) {
		Producto producto = findById(idProducto);
		if (producto != null) {
			double precioOriginal = producto.getPrecio();
			float descuento = producto.getDescuento();
			double precioFinal = precioOriginal - (precioOriginal * descuento / 100);
			return precioFinal;
		}
		return null;
	}

}
