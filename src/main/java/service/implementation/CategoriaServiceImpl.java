package service.implementation;

import java.util.List;

import model.Categoria;
import service.interfaces.ICategoriaService;
import dao.implementation.CategoriaDAOImpl;
import dao.interfaces.ICategoriaDAO;

public class CategoriaServiceImpl implements ICategoriaService {

	ICategoriaDAO categoriaDAO = new CategoriaDAOImpl();
	
	public CategoriaServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public List<Categoria> findAll() {
		// TODO Auto-generated method stub
		List<Categoria> categorias = categoriaDAO.findall();
		return categorias;
	}

	

	@Override
	public Categoria createCategoria(String nombre, String descripcion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Categoria updateCategoria(int idCategoria, String nombre, String descripcion) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Categoria findByName(String nombre) {
		Categoria categoria = categoriaDAO.findbyname(nombre);
		if(categoria != null) {
			return categoria;
		}
		return null;
	}


	@Override
	public boolean deleteByName(String nombre) {
		// TODO Auto-generated method stub
		return false;
	}

}
