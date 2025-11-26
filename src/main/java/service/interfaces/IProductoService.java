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
	
	Producto createProducto(String nombre,String descripcion,Double precio,float descuento,String urlImagen,int idCategoria);
	Producto findByName(String nombre);
	Producto updateProducto(String nombre,String descripcion,Double precio,float descuento,String urlImagen,int idCategoria);
	void deleteByName(String nombre);
	List<Producto> findAll();
	

}
