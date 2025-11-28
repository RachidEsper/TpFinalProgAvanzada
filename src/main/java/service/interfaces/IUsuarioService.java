package service.interfaces;

import java.util.List;

import model.Usuario;

public interface IUsuarioService {
	
	Usuario createUsuario(String nombre,String email,String password,String telefono,int tipo);
	Usuario findById(int id);
	boolean updateUsuario(int id,String nombre,String email,String password,String telefono,int tipo);
	boolean deleteUsuario(int id);
	List<Usuario> findAll();

}
