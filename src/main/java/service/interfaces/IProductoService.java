package service.interfaces;

import java.util.List;

import model.Producto;

public interface IProductoService {

	/*
	 * private String idProducto; private String nombre; private String descripcion;
	 * private Double precio; private float descuento; private String urlImagen;
	 * private int idCategoria;
	 */

	Producto createProducto(String id, String nombre, String descripcion, Double precio, float descuento,
			String urlImagen, int stock, int idCategoria);

	Producto findByName(String nombre);

	Producto updateProducto(String nombre, String descripcion, Double precio, float descuento, String urlImagen,
			int stock, int idCategoria);

	void deleteByName(String nombre);

	List<Producto> findAll();

	Producto crearProductoDesdeFormulario(String idProductoParam, String nombre, String descripcion, String precioParam,
			String descuentoParam, String urlImagen, String stockParam, String idCategoriaParam, List<String> errores);

	Producto findById(String idProducto);

	Producto updateProducto(Producto producto);

	boolean deleteById(String idProducto);

	// Actualiza un producto a partir de los parámetros del formulario de edición.
	// Devuelve el producto actualizado o null si hay errores (se cargan en la lista
	// errores).
	Producto actualizarProductoDesdeFormulario(String idProductoParam, String nombre, String descripcion,
			String precioParam, String descuentoParam, String urlImagen, String stockParam, String idCategoriaParam,
			java.util.List<String> errores);

}
