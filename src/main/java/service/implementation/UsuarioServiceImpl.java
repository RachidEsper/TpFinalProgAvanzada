package service.implementation;

import java.util.List;
import java.util.Map;

import dao.interfaces.IUsuarioDAO;
import dao.implementation.PedidoDAOImpl;
import dao.implementation.UsuarioDAOImpl;
import exception.BusinessException;
import model.TipoUsuario;
import model.Usuario;
import service.interfaces.IUsuarioService;
import dao.interfaces.IPedidoDAO;

public class UsuarioServiceImpl implements IUsuarioService {

	public UsuarioServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Usuario createUsuario(String nombre, String email, String password, String telefono, int tipo) {
		// ===== VALIDACIONES DE NEGOCIO =====

		// Validar nombre
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new BusinessException("El nombre es obligatorio.");
		}
		if (nombre.trim().length() < 2) {
			throw new BusinessException("El nombre debe tener al menos 2 caracteres.");
		}

		// Validar email
		if (email == null || email.trim().isEmpty()) {
			throw new BusinessException("El email es obligatorio.");
		}
		if (!email.contains("@") || !email.contains(".")) {
			throw new BusinessException("El email debe tener un formato válido.");
		}

		// Validar contraseña
		if (password == null || password.trim().isEmpty()) {
			throw new BusinessException("La contraseña es obligatoria.");
		}
		if (password.length() < 4) {
			throw new BusinessException("La contraseña debe tener al menos 4 caracteres.");
		}

		// Validar tipo de usuario
		if (tipo != 1 && tipo != 2) {
			throw new BusinessException("El tipo de usuario debe ser 1 (Administrador) o 2 (Usuario Normal).");
		}

		// ===== VERIFICAR EMAIL DUPLICADO =====
		IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
		 //para verificar si el email ya existe
		Usuario existente = usuarioDAO.findByEmail(email);
		if (existente != null) throw new BusinessException("El email ya está registrado.");

		// ===== CREAR USUARIO =====
		Usuario nuevoUsuario = new Usuario();
		nuevoUsuario.setNombre(nombre.trim());
		nuevoUsuario.setEmail(email.trim().toLowerCase());
		nuevoUsuario.setPassword(password);
		nuevoUsuario.setTelefono(telefono != null ? telefono.trim() : null);

		TipoUsuario tipoUsuario = new TipoUsuario();
		tipoUsuario.setIdTipoUsuario(tipo);
		nuevoUsuario.setTipo(tipoUsuario);

		usuarioDAO.create(nuevoUsuario);
		return nuevoUsuario;
	}

	@Override
	public Usuario findById(int id) {
		IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
		Usuario usuario = usuarioDAO.findById(id);
		if (usuario != null) {
			return usuario;
		}

		return null;
	}

	@Override
	public boolean updateUsuario(int id, String nombre, String email, String password, String telefono, int tipo) {

		IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();

		Usuario nuevoUsuario = new Usuario();
		nuevoUsuario.setIdUsuario(id); // 👈 FUNDAMENTAL
		nuevoUsuario.setNombre(nombre);
		nuevoUsuario.setEmail(email);
		nuevoUsuario.setPassword(password);
		nuevoUsuario.setTelefono(telefono); // 👈 usar el parámetro

		TipoUsuario tipoUsuario = new TipoUsuario();
		tipoUsuario.setIdTipoUsuario(tipo);
		nuevoUsuario.setTipo(tipoUsuario);

		Usuario actualizado = usuarioDAO.update(nuevoUsuario);
		return (actualizado != null); // 👈 true solo si el DAO actualizó
	}

	@Override
	public boolean deleteUsuario(int id) {
		IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
		IPedidoDAO pedidoDAO = new PedidoDAOImpl();
		
		if (pedidoDAO.existsByUsuarioId(id)) {
			// No se puede eliminar el usuario porque tiene pedidos asociados
			return false;
		}
		usuarioDAO.deleteById(id);
		return true;
	}

	@Override
	public List<Usuario> findAll() {
		// TODO Auto-generated method stub
		IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
		List<Usuario> usuarios = usuarioDAO.findAll();

		return usuarios;
	}

	@Override
	public Usuario actualizarUsuarioDesdeFormulario(String idParam, String nombre, String email, String telefono,
			String tipoParam, String password, Map<String, String> errors) {
		if (errors == null) {
			errors = new java.util.HashMap<>();
		}

		// 1) Validaciones básicas de campos
		if (idParam == null || idParam.isBlank()) {
			errors.put("id", "ID de usuario inválido.");
		}

		if (nombre == null || nombre.isBlank()) {
			errors.put("nombre", "El nombre es obligatorio.");
		}

		if (email == null || email.isBlank()) {
			errors.put("email", "El email es obligatorio.");
		}

		if (tipoParam == null || tipoParam.isBlank()) {
			errors.put("idTipoUsuario", "Debe seleccionar un tipo de usuario.");
		}

		Integer id = null;
		Integer idTipo = null;

		// Parseo de ID
		if (idParam != null && !idParam.isBlank()) {
			try {
				id = Integer.parseInt(idParam);
			} catch (NumberFormatException e) {
				errors.put("id", "Formato de ID inválido.");
			}
		}

		// Parseo de tipo usuario
		if (tipoParam != null && !tipoParam.isBlank()) {
			try {
				idTipo = Integer.parseInt(tipoParam);
			} catch (NumberFormatException e) {
				errors.put("idTipoUsuario", "Formato de tipo de usuario inválido.");
			}
		}

		// Si ya hay errores de validación, cortamos acá
		if (!errors.isEmpty()) {
			return null;
		}

		// 2) Buscar usuario actual en BD
		Usuario uDb = this.findById(id);
		if (uDb == null) {
			errors.put("global", "El usuario ya no existe.");
			return null;
		}

		// 3) Resolver contraseña final:
		// - si se ingresó una nueva, usamos esa
		// - si vino vacío, mantenemos la anterior
		String passwordFinal;
		if (password != null && !password.isBlank()) {
			passwordFinal = password;
			// 💡 si más adelante querés hashear, este es el lugar
		} else {
			passwordFinal = uDb.getPassword();
		}

		// 4) Actualizar usando el método de service/DAO 
		boolean ok = this.updateUsuario(id, nombre, email, passwordFinal, telefono, idTipo);

		if (!ok) {
			errors.put("global", "No se pudo actualizar el usuario en la base de datos.");
			return null;
		}

		// 5) Actualizar el objeto en memoria (opcional, pero prolijo)
		uDb.setNombre(nombre);
		uDb.setEmail(email);
		uDb.setTelefono(telefono);
		uDb.setPassword(passwordFinal);

		if (uDb.getTipo() == null) {
			TipoUsuario t = new TipoUsuario();
			t.setIdTipoUsuario(idTipo);
			uDb.setTipo(t);
		} else {
			uDb.getTipo().setIdTipoUsuario(idTipo);
		}

		return uDb;
	}

}
