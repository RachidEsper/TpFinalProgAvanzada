package dao.interfaces;

import java.util.List;

import model.Producto;

public interface IProductoDAO {

	// CRUD básico
	List<Producto> findAll();
	Producto findByName(String idProducto);
	Producto create(Producto producto);
	Producto update(Producto producto);
	boolean deleteByName(String nombre);
	boolean existsById(String nombre);
	int count();
	
	// Métodos específicos de negocio para Producto
	List<Producto> findByCategoria(int idCategoria);
	List<Producto> findByNombre(String nombre);
	List<Producto> findByPrecioRango(Double precioMin, Double precioMax);
	List<Producto> findConDescuento();
	boolean updatePrecio(String idProducto, Double nuevoPrecio);
	boolean updateDescuento(String idProducto, float nuevoDescuento);
	Double calcularPrecioFinal(String idProducto);
	
}
