package service.interfaces;

import model.Usuario;

public interface IAuthService {

	// buscar si usuario existe
	// ver si la contraseña es correcta
	// devolver el usuario si todo ok
	// devolver null si no ok
	
	Usuario login(String email, String contrasenia);
	
	
	
	
	
}
