package dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import config.DbConfig;
import dao.interfaces.IUsuarioDAO;
import model.TipoUsuario;
import model.Usuario;

public class UsuarioDAOImpl implements IUsuarioDAO {

	public UsuarioDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Usuario> findAll() {
		String SQL = "SELECT * FROM usuario";
		List<Usuario> usuarios = new java.util.ArrayList<>();
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(rs.getInt("id_usuario"));
				usuario.setNombre(rs.getString("nombre"));
				usuario.setEmail(rs.getString("email"));
				usuario.setPassword(rs.getString("contrasenia"));
				usuario.setTelefono(rs.getString("telefono"));

				// Utilizo el constructor vacioCrear el objeto TipoUsuario y asignarlo al
				// usuario
				TipoUsuario tipoUsuario = new TipoUsuario();
				tipoUsuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
				usuario.setTipo(tipoUsuario);

				usuarios.add(usuario);
			}
		} catch (Exception e) {
			System.out.println("Error al obtener todos los usuarios: " + e.getMessage());
		}

		return usuarios;
	}

	@Override
	public Usuario findById(int idUsuario) {
		String SQL = "SELECT * FROM usuario WHERE id_usuario = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery()) {
			ps.setInt(1, idUsuario);
			if (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(rs.getInt("id_usuario"));
				usuario.setNombre(rs.getString("nombre"));
				usuario.setEmail(rs.getString("email"));
				usuario.setPassword(rs.getString("contrasenia"));
				usuario.setTelefono(rs.getString("telefono"));
				// Utilizo el constructor vacioCrear el objeto TipoUsuario y asignarlo al
				// usuario
				TipoUsuario tipoUsuario = new TipoUsuario();
				tipoUsuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
				usuario.setTipo(tipoUsuario);
				return usuario;
			}

		} catch (Exception e) {
			System.out.println("Error al obtener el usuario por ID: " + e.getMessage());
		}
		return null;
	}

	@Override
	public Usuario create(Usuario usuario) {
		final String SQL = "INSERT INTO usuario (nombre, email, contrasenia, telefono, id_tipo_usuario) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, usuario.getNombre());
			ps.setString(2, usuario.getEmail());
			ps.setString(3, usuario.getPassword());
			ps.setString(4, usuario.getTelefono());
			ps.setInt(5, usuario.getTipo().getIdTipoUsuario());

			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new Exception("Creating user failed, no rows affected.");
			}

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					usuario.setIdUsuario(generatedKeys.getInt(1));
				} else {
					throw new Exception("Creating user failed, no ID obtained.");
				}
			}
			return usuario;
		} catch (Exception e) {
			System.out.println("Error al crear el usuario: " + e.getMessage());
			return null;
		}

	}

	@Override
	public Usuario update(Usuario usuario) {
		final String SQL = "UPDATE usuario SET nombre = ?, email = ?, contrasenia = ?, telefono = ?, id_tipo_usuario = ? WHERE id_usuario = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, usuario.getNombre());
			ps.setString(2, usuario.getEmail());
			ps.setString(3, usuario.getPassword());
			ps.setString(4, usuario.getTelefono());
			ps.setInt(5, usuario.getTipo().getIdTipoUsuario());
			ps.setInt(6, usuario.getIdUsuario());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new Exception("Updating user failed, no rows affected.");
			} else {
				return usuario;
			}
		} catch (Exception e) {
			System.out.println("Error al actualizar el usuario: " + e.getMessage());
		}

		return null;
	}

	@Override
	public boolean deleteById(int idUsuario) {
		final String SQL = "DELETE FROM usuario WHERE idUsuario = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idUsuario);
			int affectedRows = ps.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			System.out.println("Error al eliminar el usuario: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean existsById(int idUsuario) {
		final String SQL = "SELECT COUNT(*) FROM usuario WHERE idUsuario = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idUsuario);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
			return false;
		} catch (Exception e) {
			System.out.println("Error al verificar si existe el usuario: " + e.getMessage());
			return false;
		}
	}

	@Override
	public int count() {
		final String SQL = "SELECT COUNT(*) FROM usuario";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (Exception e) {
			System.out.println("Error al contar usuarios: " + e.getMessage());
			return 0;
		}
	}

	@Override
	public Usuario findByEmail(String email) {
		final String SQL = "SELECT * FROM usuario WHERE email = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(rs.getInt("id_usuario"));
				usuario.setNombre(rs.getString("nombre"));
				usuario.setEmail(rs.getString("email"));
				usuario.setPassword(rs.getString("contrasenia"));
				usuario.setTelefono(rs.getString("telefono"));
				
				TipoUsuario tipoUsuario = new TipoUsuario();
				tipoUsuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
				usuario.setTipo(tipoUsuario);
				
				return usuario;
			}
			return null;
		} catch (Exception e) {
			System.out.println("Error al buscar usuario por email: " + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean existsByEmail(String email) {
		final String SQL = "SELECT COUNT(*) FROM usuario WHERE email = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
			return false;
		} catch (Exception e) {
			System.out.println("Error al verificar si existe el email: " + e.getMessage());
			return false;
		}
	}


	@Override
	public List<Usuario> findByTipo(TipoUsuario tipo) {
		final String SQL = "SELECT * FROM usuario WHERE tipoUsuarioId = ?";
		List<Usuario> usuarios = new java.util.ArrayList<>();
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, tipo.getIdTipoUsuario());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(rs.getInt("id_usuario"));
				usuario.setNombre(rs.getString("nombre"));
				usuario.setEmail(rs.getString("email"));
				usuario.setPassword(rs.getString("contrasenia"));
				usuario.setTelefono(rs.getString("telefono"));
				
				TipoUsuario tipoUsuario = new TipoUsuario();
				tipoUsuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
				usuario.setTipo(tipoUsuario);
				
				usuarios.add(usuario);
			}
			return usuarios;
		} catch (Exception e) {
			System.out.println("Error al buscar usuarios por tipo: " + e.getMessage());
			return usuarios;
		}
	}

	@Override
	public List<Usuario> findByTipoId(int idTipoUsuario) {
		final String SQL = "SELECT * FROM usuario WHERE id_tipo_usuario = ?";
		List<Usuario> usuarios = new java.util.ArrayList<>();
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idTipoUsuario);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(rs.getInt("id_usuario"));
				usuario.setNombre(rs.getString("nombre"));
				usuario.setEmail(rs.getString("email"));
				usuario.setPassword(rs.getString("contrasenia"));
				usuario.setTelefono(rs.getString("telefono"));
				
				TipoUsuario tipoUsuario = new TipoUsuario();
				tipoUsuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
				usuario.setTipo(tipoUsuario);
				
				usuarios.add(usuario);
			}
			return usuarios;
		} catch (Exception e) {
			System.out.println("Error al buscar usuarios por tipo ID: " + e.getMessage());
			return usuarios;
		}
	}

	@Override
	public boolean updateContrasenia(int idUsuario, String nuevaContrasenia) {
		final String SQL = "UPDATE usuario SET contrasenia = ? WHERE id_usuario = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, nuevaContrasenia);
			ps.setInt(2, idUsuario);
			int affectedRows = ps.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			System.out.println("Error al actualizar contraseña: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateEmail(int idUsuario, String nuevoEmail) {
		final String SQL = "UPDATE usuario SET email = ? WHERE id_usuario = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, nuevoEmail);
			ps.setInt(2, idUsuario);
			int affectedRows = ps.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			System.out.println("Error al actualizar email: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateTelefono(int idUsuario, String nuevoTelefono) {
		final String SQL = "UPDATE usuario SET telefono = ? WHERE id_usuario = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, nuevoTelefono);
			ps.setInt(2, idUsuario);
			int affectedRows = ps.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			System.out.println("Error al actualizar teléfono: " + e.getMessage());
			return false;
		}
	}

	@Override
	public int countByTipo(int idTipoUsuario) {
		final String SQL = "SELECT COUNT(*) FROM usuario WHERE id_tipo_usuario = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setInt(1, idTipoUsuario);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (Exception e) {
			System.out.println("Error al contar usuarios por tipo: " + e.getMessage());
			return 0;
		}
	}

	@Override
	public boolean validateCredentials(String email, String contrasenia) {
		final String SQL = "SELECT COUNT(*) FROM usuario WHERE email = ? AND contrasenia = ?";
		try (Connection connection = DbConfig.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(SQL)) {
			ps.setString(1, email);
			ps.setString(2, contrasenia);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
			return false;
		} catch (Exception e) {
			System.out.println("Error al validar credenciales: " + e.getMessage());
			return false;
		}
	}

}
