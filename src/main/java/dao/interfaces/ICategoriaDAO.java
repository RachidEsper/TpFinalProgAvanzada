package dao.interfaces;

import java.util.List;

import model.Categoria;

public interface ICategoriaDAO {

	List <Categoria> findall();
	Categoria findbyid(int idCategoria);
	Categoria create(Categoria categoria);
	Categoria update(Categoria categoria);
	boolean deleteById(int idCategoria);
	boolean existsById(int idCategoria);
	Categoria findbyname(String nombre);
	int count();
	
	
	/*
	 * que pasa si en vez de hacer una interfaz por cada dao,
	 * hago una interfaz generica
	 */
}
