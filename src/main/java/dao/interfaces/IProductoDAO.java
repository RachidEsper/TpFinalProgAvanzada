package dao.interfaces;

import java.util.List;

import model.Producto;

public interface IProductoDAO {

	// CRUD básico
	List<Producto> findAll();
	Producto findById(String idProducto);
	Producto create(Producto producto);
	Producto update(Producto producto);
	boolean deleteById(String idProducto);
	boolean existsById(String nombre);
	int count();
	boolean deleteByName(String nombre);
	
	// Métodos específicos de negocio para Producto
	List<Producto> findByCategoria(int idCategoria);
	List<Producto> findByNombre(String nombre);
	List<Producto> findByPrecioRango(Double precioMin, Double precioMax);
	List<Producto> findConDescuento();
	boolean updatePrecio(String idProducto, Double nuevoPrecio);
	boolean updateDescuento(String idProducto, float nuevoDescuento);
	Double calcularPrecioFinal(String idProducto);
	
}
