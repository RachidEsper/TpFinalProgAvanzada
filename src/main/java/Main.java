import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Categoria;
import model.Usuario;
import config.DbConfig;
import dao.implementation.CategoriaDAOImpl;
import dao.implementation.UsuarioDAOImpl;
import dao.interfaces.ICategoriaDAO;
import dao.interfaces.IUsuarioDAO;
public class Main {
	public static void main(String[] args) {
		// 4.1) Usamos el Singleton y probamos un SELECT 1 (smoke test)
/*		try (Connection cn = DbConfig.getInstance().getConnection();
				PreparedStatement ps = cn.prepareStatement("SELECT 1");
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				System.out.println("Conexión OK. Resultado: " + rs.getInt(1));
			}
		} catch (Exception e) {
			// 4.2) Mostrar el error completo para diagnosticar en esta etapa
			e.printStackTrace();
		}
		*/
		// Hacer Login completo
		// Listar productos
		// Crear un producto
		// Modificar un producto
		// Usuarios
		
		ICategoriaDAO categoriaDAO = new CategoriaDAOImpl();
		
		Categoria nuevaCategoria = new Categoria();
		nuevaCategoria.setNombre("Perfumes");
		nuevaCategoria.setDescripcion("Fragancias y aromas");
		
		System.out.println("----- LISTA DE CATEGORIAS -----");
		for(Categoria cat : categoriaDAO.findall()) {
			System.out.println("ID: "+cat.getIdCategoria()+
					" Nombre: "+cat.getNombre()
						+" Descripcion: "+cat.getDescripcion());
		}
		
/*		System.out.println("----- BUSCAR CATEGORIA POR NOMBRE -----");
		if(categoria!=null) {
			System.out.println("ID: "+categoria.getIdCategoria()+
					" Nombre: "+categoria.getNombre()
						+" Descripcion: "+categoria.getDescripcion());
		}
		else {
			System.out.println("No se encontro la categoria");
		}
*/	
		IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
		System.out.println("----- LISTA DE USUARIOS -----");
		for(Usuario user : usuarioDAO.findAll()) {
			System.out.println("ID: "+user.getIdUsuario()+
					" Nombre: "+user.getNombre()
						+" Email: "+user.getEmail());
			
		}
		
		
		
		/*
		 * public static void main(String[] args) { // aca hago pruebas de test
		 * unitarios try (Connection conn = Db.config.getInstance().getConnection();
		 * PreparedStatement ps = conn.prepareStatement("SELECT * FROM Usuario");
		 * ResultSet rs = ps.executeQuery()) {
		 * 
		 * if(rs.next()) { System.out.println("Estoy conectado"); }
		 * 
		 * } catch(Exception e) { e.printStackTrace(); } }
		 */
	}
}
