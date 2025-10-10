package dao.interfaces;

import java.util.List;

import model.Categoria;

public interface ICategoriaDAO {

	List <Categoria> findall();
	Categoria findbyid(int idCategoria);
	/*
	 * que pasa si en vez de hacer una interfaz por cada dao,
	 * hago una interfaz generica
	 */
}
