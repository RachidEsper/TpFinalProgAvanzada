package dao.implementation;

import java.util.ArrayList;
import java.util.List;

import config.DbConfig;
import dao.interfaces.ICategoriaDAO;
import model.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class CategoriaDAOImpl implements ICategoriaDAO {

	public CategoriaDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * El id es dato nuestro, para el usuario final solo se usara el nombre de la
	 * categoria
	 */
	/*
	 * Cuando cargamos una categoria, validar que no exista por nombre, probar usar
	 * uppercase en logica de negocio
	 */

	@Override
	public List<Categoria> findall() {
		// Metodo funcionando
		final String SQL = "SELECT * FROM categoria";
		List<Categoria> categorias = new ArrayList<>();
		// try with resources: abre y cierra la conexion sola
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery()) {
			// Recorro el ResultSet y voy creando las categorias
			while (rs.next()) {
				Categoria categoria = new Categoria();
				categoria.setIdCategoria(rs.getInt("id_categoria"));
				categoria.setNombre(rs.getString("nombre"));
				categoria.setDescripcion(rs.getString("descripcion"));
				categorias.add(categoria);

			}
			return categorias;
		} catch (SQLException e) {
			System.out.println("Error al obtener todas las categorias: " + e.getMessage());
		}

		return null;
	}

	@Override
	public Categoria findbyid(int idCategoria) {
		// Aca se escribe la query(consulta)
		// Metodo funcionando
		final String SQL = "SELECT * FROM categoria WHERE  = ?";
		// try with resources: abre y cierra la conexion sola
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idCategoria);
			try (ResultSet rs = ps.executeQuery()) {
				// Recorro el ResultSet y voy creando la categoria
				if (rs.next()) {
					Categoria categoria = new Categoria();
					categoria.setIdCategoria(rs.getInt("id_Categoria"));
					categoria.setNombre(rs.getString("nombre"));
					categoria.setDescripcion(rs.getString("descripcion"));

					return categoria;
				}
			}

		} catch (SQLException e) {
			System.out.println("Error al obtener la categoria por ID: " + e.getMessage());
		}
		return null;
	}

	@Override
	public Categoria create(Categoria categoria) {
		final String SQL = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, categoria.getNombre());
			if (categoria.getDescripcion() == null) {
				ps.setNull(2, Types.VARCHAR);
			} else {
				ps.setString(2, categoria.getDescripcion());
			}

			int affected = ps.executeUpdate();
			if (affected == 0) {
				return null;
			}

			// Leer id autogenerado y setearlo en el objeto
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					int id = keys.getInt(1);
					categoria.setIdCategoria(id);
				}
			}
			System.out.println("Categoria creada con ID: " + categoria.getIdCategoria());
			return categoria;
		} catch (SQLException e) {
			System.out.println("Error creando categoria: " + e.getMessage());
			return null;
		}
	}

	@Override
	public Categoria update(Categoria categoria) {
		final String SQL = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {

			ps.setString(1, categoria.getNombre());
			if (categoria.getDescripcion() == null) {
				ps.setNull(2, Types.VARCHAR);
			} else {
				ps.setString(2, categoria.getDescripcion());
			}
			ps.setInt(3, categoria.getIdCategoria());

			int affected = ps.executeUpdate();
			if (affected == 0) {
				return null;
			}

			return categoria;
		} catch (SQLException e) {
			System.out.println("Error actualizando categoria: " + e.getMessage());
			return null;
		}

	}

	@Override
	public boolean deleteById(int idCategoria) {
		final String SQL = "DELETE FROM categoria WHERE id_categoria = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {

			ps.setInt(1, idCategoria);
			int affected = ps.executeUpdate();
			System.out.println("Categoria eliminada con ID: " + idCategoria);
			// Si affected > 0 significa que se eliminó al menos una fila
			return affected > 0;

		} catch (SQLException e) {
			System.out.println("Error eliminando categoria: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean existsById(int idCategoria) {
		final String SQL = "SELECT COUNT(*) FROM categoria WHERE id_categoria = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {

			ps.setInt(1, idCategoria);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt(1);
					// Si count > 0 significa que existe al menos un registro con ese ID
					return count > 0;
				}
			}

		} catch (SQLException e) {
			System.out.println("Error verificando existencia de categoria: " + e.getMessage());
		}
		return false;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Categoria findbyname(String nombre) {
		final String SQL = "SELECT * FROM categoria WHERE nombre = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, nombre);
			try (ResultSet rs = ps.executeQuery()) {
				// Recorro el ResultSet y voy creando la categoria
				if (rs.next()) {
					Categoria categoria = new Categoria();
					categoria.setIdCategoria(rs.getInt("id_Categoria"));
					categoria.setNombre(rs.getString("nombre"));
					categoria.setDescripcion(rs.getString("descripcion"));

					return categoria;
				}
			}

		} catch (SQLException e) {
			System.out.println("Error al obtener la categoria por NOMBRE: " + e.getMessage());
			return null;
		}
		
		
		
		return null;
	}
}
