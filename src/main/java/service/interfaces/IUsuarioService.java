package service.interfaces;

import java.util.List;

import model.Usuario;

public interface IUsuarioService {
	
	Usuario createUsuario(String nombre,String email,String password,String telefono,int tipo);
	Usuario findById(int id);
	Usuario updateUsuario(String nombre,String email,String password,String telefono,int tipo);
	void deleteUsuario(int id);
	List<Usuario> findAll();

}
