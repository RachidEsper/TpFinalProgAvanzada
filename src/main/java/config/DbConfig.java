package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton "factory" de conexiones. Mantiene la config y entrega una
 * Connection nueva en cada getConnection().
 */
public final class DbConfig {

	// 3.1) Constantes de configuración (podés moverlas a .properties más adelante)
	private final String host = "jdbc:mysql://localhost:3306/ventas_cosmeticos";
	private final String user = "root";
	private final String pass = "";
	
	// 3.2) Constructor privado: garantiza que nadie haga new DbConfig()
	private DbConfig() {
		// 3.3) Cargar el driver una sola vez: si falla, cortamos en arranque
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("No se encontró el driver JDBC: " + host, e);
		}
	}

	// 3.4) Holder: crea la instancia única cuando alguien llama getInstance()
	private static class Holder {
		private static final DbConfig INSTANCE = new DbConfig();
	}

	// 3.5) Punto de acceso global
	public static DbConfig getInstance() {
		return Holder.INSTANCE;
	}

	// 3.6) Fábrica de conexiones: SIEMPRE devuelve una nueva
	// (ideal para luego reemplazar por un pool)
	public Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection;
			connection=DriverManager.getConnection(host,user,pass);
			return connection;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
