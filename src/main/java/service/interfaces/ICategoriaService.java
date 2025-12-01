package service.interfaces;

import java.util.List;

import model.Categoria;

public interface ICategoriaService {

	
	
	List<Categoria> findAll();
	Categoria createCategoria(String nombre, String descripcion);
	Categoria updateCategoria(int idCategoria, String nombre, String descripcion);
	boolean deleteByName(String nombre);
	Categoria findByName(String nombre);
	
	
}
