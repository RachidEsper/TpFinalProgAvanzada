package service.interfaces;

import java.util.List;

import model.Producto;

public interface IProductoService {
	
	/*
	 * private String idProducto;
	private String nombre;
	private String descripcion;
	private Double precio;
	private float descuento;
	private String urlImagen;
	private int idCategoria;
	 */
	
	Producto createProducto(String nombre,String descripcion,Double precio,float descuento,String urlImagen,int stock,int idCategoria);
	Producto findByName(String nombre);
	Producto updateProducto(String nombre,String descripcion,Double precio,float descuento,String urlImagen,int stock,int idCategoria);
	void deleteByName(String nombre);
	List<Producto> findAll();
	
	Producto findById(String idProducto);
	Producto updateProducto(Producto producto);
	boolean deleteById(String idProducto);


}
