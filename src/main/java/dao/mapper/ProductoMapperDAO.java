package dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Producto;

/**
 * Clase Mapper encargada de convertir datos crudos de la base de datos (ResultSet)
 * en objetos Java de tipo Producto.
 * 
 * Responsabilidades:
 * - Mapear una fila del ResultSet a un objeto Producto
 * - Mapear múltiples filas del ResultSet a una lista de Productos
 * 
 * Beneficios:
 * - Centraliza la lógica de mapeo en un solo lugar
 * - Evita duplicación de código en los métodos del DAO
 * - Facilita mantenimiento (si cambia la estructura, se modifica solo aquí)
 */
public class ProductoMapperDAO {
	// poner constructor privado y los metodos estaticos
	/**
	 * Convierte UNA fila del ResultSet en un objeto Producto.
	 * 
	 * Se usa en métodos que retornan UN SOLO producto, como:
	 * - findById(String idProducto)
	 * - create(Producto producto)
	 * 
	 * @param rs ResultSet posicionado en la fila actual
	 * @return Objeto Producto con los datos de esa fila
	 * @throws SQLException Si hay error al leer las columnas
	 */
	public Producto mapRowToProducto(ResultSet rs) throws SQLException {
		// Crear un nuevo objeto Producto vacío
		Producto producto = new Producto(
			// Los parámetros van en el orden del constructor de Producto
			rs.getString("idProducto"),      // String idProducto
			rs.getString("nombre"),           // String nombre
			rs.getString("descripcion"),      // String descripcion
			rs.getDouble("precio"),           // Double precio
			rs.getFloat("descuento"),         // float descuento
			rs.getString("urlImagen"),        // String urlImagen
			rs.getInt("idCategoria")          // int idCategoria
		);
		
		return producto;
	}
	
	/**
	 * Convierte TODAS las filas del ResultSet en una lista de Productos.
	 * 
	 * Se usa en métodos que retornan MÚLTIPLES productos, como:
	 * - findAll()
	 * - findByCategoria(int idCategoria)
	 * - findByNombre(String nombre)
	 * - findConDescuento()
	 * 
	 * @param rs ResultSet con múltiples filas
	 * @return Lista de objetos Producto
	 * @throws SQLException Si hay error al leer las columnas
	 */
	public List<Producto> mapResultSetToList(ResultSet rs) throws SQLException {
		// Crear lista vacía para almacenar los productos
		List<Producto> productos = new ArrayList<>();
		
		// Iterar sobre todas las filas del ResultSet
		while (rs.next()) {
			// Por cada fila, usar el método mapRowToProducto
			// Esto evita duplicar código
			Producto producto = mapRowToProducto(rs);
			
			// Agregar el producto a la lista
			productos.add(producto);
		}
		
		return productos;
	}
	
	/**
	 * Extrae un valor Double del ResultSet de forma segura.
	 * 
	 * Útil para métodos como:
	 * - calcularPrecioFinal(String idProducto)
	 * 
	 * @param rs ResultSet posicionado en la fila
	 * @param columnName Nombre de la columna a extraer
	 * @return El valor Double o null si no existe
	 * @throws SQLException Si hay error al leer la columna
	 */
	public Double mapToDouble(ResultSet rs, String columnName) throws SQLException {
		double value = rs.getDouble(columnName);
		// wasNull() verifica si el último valor leído era NULL en la DB
		return rs.wasNull() ? null : value;
	}
	
	/**
	 * Extrae un valor Integer del ResultSet de forma segura.
	 * 
	 * Útil para métodos como:
	 * - count()
	 * - countByCategoria()
	 * 
	 * @param rs ResultSet posicionado en la fila
	 * @param columnName Nombre de la columna a extraer
	 * @return El valor Integer
	 * @throws SQLException Si hay error al leer la columna
	 */
	public int mapToInt(ResultSet rs, String columnName) throws SQLException {
		return rs.getInt(columnName);
	}

}
