package dao.interfaces;

import java.util.List;

import model.Usuario;
import model.TipoUsuario;

public interface IUsuarioDAO {

	// CRUD básico
	List<Usuario> findAll();
	Usuario findById(int idUsuario);
	Usuario create(Usuario usuario);
	Usuario update(Usuario usuario);
	boolean deleteById(int idUsuario);
	boolean existsById(int idUsuario);
	int count();
	
	// Métodos de autenticación y seguridad
	Usuario findByEmail(String email);
	boolean existsByEmail(String email);
	boolean validateCredentials(String email, String contrasenia);
	
	// Métodos específicos de negocio para Usuario
	List<Usuario> findByTipo(TipoUsuario tipo);
	List<Usuario> findByTipoId(int idTipoUsuario);
	boolean updateContrasenia(int idUsuario, String nuevaContrasenia);
	boolean updateEmail(int idUsuario, String nuevoEmail);
	boolean updateTelefono(int idUsuario, String nuevoTelefono);
	int countByTipo(int idTipoUsuario);
	
}
