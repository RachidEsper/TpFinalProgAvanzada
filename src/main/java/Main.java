import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Categoria;
import config.DbConfig;
import dao.implementation.CategoriaDAOImpl;

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
		CategoriaDAOImpl categoriaDAO = new CategoriaDAOImpl();
		
		
		
		//System.out.println("ID: "+categoriaDAO.findbyid(5).getIdCategoria()+
			//	" Nombre: "+categoriaDAO.findbyid(5).getNombre()
				//+" Descripcion: "+categoriaDAO.findbyid(5).getDescripcion());
		
		categoriaDAO.deleteById(6);
		
		
		System.out.println("Probando metodo findById:");
		System.out.println("ID: "+categoriaDAO.findbyid(1).getIdCategoria()+
				" Nombre: "+categoriaDAO.findbyid(1).getNombre()
				+" Descripcion: "+categoriaDAO.findbyid(1).getDescripcion());
		
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
