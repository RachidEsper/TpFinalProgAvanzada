package model;

public class Producto {
	private String idProducto;
	private String nombre;
	private String descripcion;
	private Double precio;
	private float descuento;
	private String urlImagen;
	private int idCategoria;

	public Producto(String idProducto, String nombre, String descripcion, Double precio, float descuento,
			String urlImagen, int idCategoria) {

		this.idProducto = idProducto;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.descuento = descuento;
		this.urlImagen = urlImagen;
		this.idCategoria = idCategoria;
	}

	public Producto() {
		// Constructor vacío
	}

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public float getDescuento() {
		return descuento;
	}

	public void setDescuento(float descuento) {
		this.descuento = descuento;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}

}
