package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbConfig {

    // 3.1) Constantes de configuración (más adelante podés pasarlas a un .properties)
    private static final String URL  = "jdbc:mysql://localhost:3306/ventas_cosmeticos";
    private static final String USER = "root";
    private static final String PASS = "";

    // 3.2) Constructor privado: garantiza que nadie haga new DbConfig()
    private DbConfig() {
        // 3.3) Cargar el driver una sola vez: si falla, cortamos en arranque
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encontró el driver JDBC MySQL", e);
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
    // No volvemos a cargar el driver acá
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
