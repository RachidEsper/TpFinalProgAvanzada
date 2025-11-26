package service.implementation;

import java.util.List;
import dao.interfaces.IProductoDAO;
import dao.implementation.ProductoDAOImpl;
import model.Producto;
import service.interfaces.IProductoService;

public class ProductoServiceImpl implements IProductoService{

	public ProductoServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Producto createProducto(String nombre, String descripcion, Double precio, float descuento, String urlImagen,
			int idCategoria) {
		IProductoDAO productoDAO = new ProductoDAOImpl();
		Producto nuevoProducto = new Producto();
		nuevoProducto.setNombre(nombre);
		nuevoProducto.setDescripcion(descripcion);
		nuevoProducto.setPrecio(precio);
		nuevoProducto.setDescuento(descuento);
		nuevoProducto.setUrlImagen(urlImagen);
		nuevoProducto.setIdCategoria(idCategoria);
		productoDAO.create(nuevoProducto);
		
		
		
		return nuevoProducto;
	}

	@Override
	public Producto findByName(String nombre) {
		IProductoDAO productoDAO = new ProductoDAOImpl();
		Producto producto = productoDAO.findByName(nombre);
		return null;
	}

	@Override
	public Producto updateProducto(String nombre, String descripcion, Double precio, float descuento, String urlImagen,
			int idCategoria) {
		
		IProductoDAO productoDAO = new ProductoDAOImpl();
		Producto productoActualizado = new Producto();
		productoActualizado.setNombre(nombre);
		productoActualizado.setDescripcion(descripcion);
		productoActualizado.setPrecio(precio);
		productoActualizado.setDescuento(descuento);
		productoActualizado.setUrlImagen(urlImagen);
		productoActualizado.setIdCategoria(idCategoria);
		productoDAO.update(productoActualizado);
		return productoActualizado;
	}

	@Override
	public void deleteByName(String name) {
		
		IProductoDAO productoDAO = new ProductoDAOImpl();
		productoDAO.deleteByName(name);
		
		
	}

	@Override
	public List<Producto> findAll() {
		IProductoDAO productoDAO = new ProductoDAOImpl();
		List<Producto> productos = productoDAO.findAll();
		return productos;
	}

}
