package service.interfaces;

import java.util.List;

import model.Usuario;

public interface IUsuarioService {

	Usuario createUsuario(String nombre, String email, String password, String telefono, int tipo);

	Usuario findById(int id);

	boolean updateUsuario(int id, String nombre, String email, String password, String telefono, int tipo);

	boolean deleteUsuario(int id);

	List<Usuario> findAll();

	// Valida y actualiza un usuario a partir de los datos del formulario.
	// Carga los errores en el mapa 'errors'.
	// Devuelve el usuario actualizado o null si hubo problemas.
	Usuario actualizarUsuarioDesdeFormulario(String idParam, String nombre, String email, String telefono,
			String tipoParam, String password, java.util.Map<String, String> errors);

}
