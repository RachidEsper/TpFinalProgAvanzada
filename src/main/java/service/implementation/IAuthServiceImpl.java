package service.implementation;

import org.mindrot.jbcrypt.BCrypt;

import dao.implementation.UsuarioDAOImpl;
import dao.interfaces.IUsuarioDAO;
import exception.DataAccessException;
import model.Usuario;
import service.interfaces.IAuthService;

public class IAuthServiceImpl implements IAuthService {

	public IAuthServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Usuario login(String email, String passwordPlano) {
		// TODO Auto-generated method stub
		try {
			IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
			Usuario user = usuarioDAO.findByEmail(email);

			// Email inexistente → credenciales inválidas
			if (user == null)
				return null;

			String password = user.getPassword(); // o getContraseniaHash()
			if (password != null && !password.isEmpty()) {
				if (passwordPlano.equals(password)) {
					return user;

				}

			}
			return null;
		} catch (DataAccessException dae) {
			// Errors reales de BD: dejalos subir
			throw dae;
		} catch (RuntimeException re) {
			// Cualquier otra Runtime inesperada (p. ej. NPE al mapear columnas)
			// traducila si querés o dejala subir: pero mejor que NO se confunda con
			// credenciales invalidas
			throw re;
		}
	}

}
