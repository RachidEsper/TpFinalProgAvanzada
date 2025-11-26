package service.implementation;

import java.util.List;
import dao.interfaces.IUsuarioDAO;
import dao.implementation.UsuarioDAOImpl;
import exception.BusinessException;
import model.TipoUsuario;
import model.Usuario;
import service.interfaces.IUsuarioService;

public class UsuarioServiceImpl implements IUsuarioService{

	public UsuarioServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Usuario createUsuario(String nombre,String email,String password,String telefono,int tipo) {
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
		// Aquí podrías agregar una validación para verificar si el email ya existe
		// Usuario existente = usuarioDAO.findByEmail(email);
		// if (existente != null) throw new BusinessException("El email ya está registrado.");
		
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
	public Usuario updateUsuario(String nombre,String email,String password,String telefono,int tipo) {
		Usuario nuevoUsuario = new Usuario();
		IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
		
		// TODO Auto-generated method stub
		nuevoUsuario.setNombre(nombre);
		nuevoUsuario.setEmail(email);
		nuevoUsuario.setPassword(password);
		TipoUsuario tipoUsuario = new TipoUsuario();
		tipoUsuario.setIdTipoUsuario(tipo);
		nuevoUsuario.setTipo(tipoUsuario);
		usuarioDAO.update(nuevoUsuario);
		return nuevoUsuario;
	
		
		}

	@Override
	public void deleteUsuario(int id) {
		IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
		usuarioDAO.deleteById(id);
		
	}

	@Override
	public List<Usuario> findAll() {
		// TODO Auto-generated method stub
		IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
		List<Usuario> usuarios = usuarioDAO.findAll();
		
		return usuarios;
	}
	
	

}
