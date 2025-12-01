package service.implementation;

import java.util.ArrayList;
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
	public Producto createProducto(String id,String nombre, String descripcion, Double precio, float descuento, String urlImagen,int stock,
			int idCategoria) {
		IProductoDAO productoDAO = new ProductoDAOImpl();
		Producto nuevoProducto = new Producto();
		nuevoProducto.setIdProducto(id);
		nuevoProducto.setNombre(nombre);
		nuevoProducto.setDescripcion(descripcion);
		nuevoProducto.setPrecio(precio);
		nuevoProducto.setDescuento(descuento);
		nuevoProducto.setUrlImagen(urlImagen);
		nuevoProducto.setStock(stock);
		nuevoProducto.setIdCategoria(idCategoria);
		productoDAO.create(nuevoProducto);
		
		
		
		return nuevoProducto;
	}

	@Override
	public Producto findByName(String nombre) {
		IProductoDAO productoDAO = new ProductoDAOImpl();
		Producto producto = productoDAO.findById(nombre);
		return null;
	}

	@Override
	public Producto updateProducto(String nombre, String descripcion, Double precio, float descuento, String urlImagen,int stock,
			int idCategoria) {
		
		IProductoDAO productoDAO = new ProductoDAOImpl();
		Producto productoActualizado = new Producto();
		productoActualizado.setNombre(nombre);
		productoActualizado.setDescripcion(descripcion);
		productoActualizado.setPrecio(precio);
		productoActualizado.setDescuento(descuento);
		productoActualizado.setUrlImagen(urlImagen);
		productoActualizado.setStock(stock);
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

	@Override
	public Producto findById(String idProducto) {
		IProductoDAO productoDAO=new ProductoDAOImpl();
		Producto producto = productoDAO.findById(idProducto);
		return producto;
	}

	@Override
	public Producto updateProducto(Producto producto) {
		IProductoDAO productoDAO=new ProductoDAOImpl();
		Producto nuevoProducto = productoDAO.update(producto);
		return nuevoProducto;
	}

	@Override
	public boolean deleteById(String idProducto) {
		IProductoDAO productoDAO=new ProductoDAOImpl();
		if(productoDAO.deleteById(idProducto)){
			return true;
		}
		return false;
	}

	@Override
	public Producto crearProductoDesdeFormulario(
	        String idProductoParam,
	        String nombre,
	        String descripcion,
	        String precioParam,
	        String descuentoParam,
	        String urlImagen,
	        String stockParam,
	        String idCategoriaParam,
	        List<String> errores
	) {
	    if (errores == null) {
	        errores = new ArrayList<>();
	    }

	    // Normalizar separador decimal del precio ("123,45" -> "123.45")
	    if (precioParam != null) {
	        precioParam = precioParam.trim().replace(",", ".");
	    }
	    if (descuentoParam != null) {
	        descuentoParam = descuentoParam.trim().replace(",", ".");
	    }

	    // 1) Validaciones de campos obligatorios
	    if (idProductoParam == null || idProductoParam.isBlank()) {
	        errores.add("El ID del producto es obligatorio.");
	    }
	    if (nombre == null || nombre.isBlank()) {
	        errores.add("El nombre es obligatorio.");
	    }
	    if (descripcion == null || descripcion.isBlank()) {
	        errores.add("La descripción es obligatoria.");
	    }
	    if (precioParam == null || precioParam.isBlank()) {
	        errores.add("El precio es obligatorio.");
	    }
	    if (stockParam == null || stockParam.isBlank()) {
	        errores.add("El stock es obligatorio.");
	    }
	    if (idCategoriaParam == null || idCategoriaParam.isBlank()) {
	        errores.add("Debe seleccionar una categoría.");
	    }

	    // 2) Validación de ID duplicado (solo si vino algo)
	    if (idProductoParam != null && !idProductoParam.isBlank()) {
	        try {
	            Producto existente = this.findById(idProductoParam);
	            if (existente != null) {
	                errores.add("Ya existe un producto con el ID " + idProductoParam + ".");
	            }
	        } catch (Exception e) {
	            System.out.println("Error verificando ID de producto existente: " + e.getMessage());
	        }
	    }

	    // 3) Parseo de valores numéricos
	    Double  precio      = null;
	    Float   descuento   = null;
	    Integer stock       = null;
	    Integer idCategoria = null;

	    if (precioParam != null && !precioParam.isBlank()) {
	        try {
	            precio = Double.parseDouble(precioParam);
	            if (precio < 0) {
	                errores.add("El precio no puede ser negativo.");
	            }
	        } catch (NumberFormatException e) {
	            errores.add("Formato de precio inválido.");
	        }
	    }

	    if (descuentoParam != null && !descuentoParam.isBlank()) {
	        try {
	            descuento = Float.parseFloat(descuentoParam);
	            if (descuento < 0) {
	                errores.add("El descuento no puede ser negativo.");
	            }
	        } catch (NumberFormatException e) {
	            errores.add("Formato de descuento inválido.");
	        }
	    }

	    if (stockParam != null && !stockParam.isBlank()) {
	        try {
	            stock = Integer.parseInt(stockParam);
	            if (stock < 0) {
	                errores.add("El stock no puede ser negativo.");
	            }
	        } catch (NumberFormatException e) {
	            errores.add("Formato de stock inválido.");
	        }
	    }

	    if (idCategoriaParam != null && !idCategoriaParam.isBlank()) {
	        try {
	            idCategoria = Integer.parseInt(idCategoriaParam);
	        } catch (NumberFormatException e) {
	            errores.add("Formato de categoría inválido.");
	        }
	    }

	    // Si hay errores de validación → no intento crear nada
	    if (!errores.isEmpty()) {
	        return null;
	    }

	    // 4) Armar el objeto Producto con los valores ya validados/parseados
	    Producto p = new Producto();
	    p.setIdProducto(idProductoParam);
	    p.setNombre(nombre);
	    p.setDescripcion(descripcion);
	    p.setPrecio(precio);
	    p.setDescuento(descuento != null ? descuento : 0f);

	    // Normalizamos la URL de la imagen (null si viene vacía o solo espacios)
	    if (urlImagen != null && !urlImagen.isBlank()) {
	        p.setUrlImagen(urlImagen.trim());
	    } else {
	        p.setUrlImagen(null);
	    }

	    p.setStock(stock);
	    p.setIdCategoria(idCategoria);

	    // 5) Insertar en BD usando el método de creación ya existente en el service
	    Producto nuevo = this.createProducto(
	            p.getIdProducto(),
	            p.getNombre(),
	            p.getDescripcion(),
	            p.getPrecio(),
	            p.getDescuento(),
	            p.getUrlImagen(),
	            p.getStock(),
	            p.getIdCategoria()
	    );

	    if (nuevo == null) {
	        errores.add("No se pudo guardar el producto en la base de datos.");
	        return null;
	    }

	    return nuevo;
	}

	
	@Override
	public Producto actualizarProductoDesdeFormulario(
	        String idProductoParam,
	        String nombre,
	        String descripcion,
	        String precioParam,
	        String descuentoParam,
	        String urlImagen,
	        String stockParam,
	        String idCategoriaParam,
	        List<String> errores
	) {
	    if (errores == null) {
	        errores = new ArrayList<>();
	    }

	    // Normalizar separadores decimales
	    if (precioParam != null) {
	        precioParam = precioParam.trim().replace(",", ".");
	    }
	    if (descuentoParam != null) {
	        descuentoParam = descuentoParam.trim().replace(",", ".");
	    }

	    // 1) Validaciones básicas
	    if (idProductoParam == null || idProductoParam.isBlank()) {
	        errores.add("El ID del producto es obligatorio.");
	    }
	    if (nombre == null || nombre.isBlank()) {
	        errores.add("El nombre es obligatorio.");
	    }
	    if (descripcion == null || descripcion.isBlank()) {
	        errores.add("La descripción es obligatoria.");
	    }
	    if (precioParam == null || precioParam.isBlank()) {
	        errores.add("El precio es obligatorio.");
	    }
	    if (stockParam == null || stockParam.isBlank()) {
	        errores.add("El stock es obligatorio.");
	    }
	    if (idCategoriaParam == null || idCategoriaParam.isBlank()) {
	        errores.add("Debe seleccionar una categoría.");
	    }

	    Double  precio      = null;
	    Float   descuento   = null;
	    Integer stock       = null;
	    Integer idCategoria = null;

	    // 2) Parseo numéricos
	    if (precioParam != null && !precioParam.isBlank()) {
	        try {
	            precio = Double.parseDouble(precioParam);
	            if (precio < 0) {
	                errores.add("El precio no puede ser negativo.");
	            }
	        } catch (NumberFormatException e) {
	            errores.add("Formato de precio inválido.");
	        }
	    }

	    if (descuentoParam != null && !descuentoParam.isBlank()) {
	        try {
	            descuento = Float.parseFloat(descuentoParam);
	            if (descuento < 0) {
	                errores.add("El descuento no puede ser negativo.");
	            }
	        } catch (NumberFormatException e) {
	            errores.add("Formato de descuento inválido.");
	        }
	    }

	    if (stockParam != null && !stockParam.isBlank()) {
	        try {
	            stock = Integer.parseInt(stockParam);
	            if (stock < 0) {
	                errores.add("El stock no puede ser negativo.");
	            }
	        } catch (NumberFormatException e) {
	            errores.add("Formato de stock inválido.");
	        }
	    }

	    if (idCategoriaParam != null && !idCategoriaParam.isBlank()) {
	        try {
	            idCategoria = Integer.parseInt(idCategoriaParam);
	        } catch (NumberFormatException e) {
	            errores.add("Formato de categoría inválido.");
	        }
	    }

	    // Si ya hay errores, no seguimos
	    if (!errores.isEmpty()) {
	        return null;
	    }

	    // 3) Buscar producto existente
	    Producto existente = this.findById(idProductoParam);
	    if (existente == null) {
	        errores.add("No se encontró el producto a actualizar.");
	        return null;
	    }

	    // 4) Actualizar campos con lo del formulario
	    existente.setNombre(nombre);
	    existente.setDescripcion(descripcion);
	    existente.setPrecio(precio);
	    existente.setDescuento(descuento != null ? descuento : 0f);

	    if (urlImagen != null && !urlImagen.isBlank()) {
	        existente.setUrlImagen(urlImagen.trim());
	    } else {
	        existente.setUrlImagen(null);
	    }

	    existente.setStock(stock);
	    existente.setIdCategoria(idCategoria);

	    // 5) Guardar en BD
	    Producto actualizado = this.updateProducto(existente);
	    if (actualizado == null) {
	        errores.add("No se pudo actualizar el producto en la base de datos.");
	        return null;
	    }

	    return actualizado;
	}

	
	
	
	
	
	
	
	
	
}
