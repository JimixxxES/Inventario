import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/store_db";
    private static final String USER = "root";
    private static final String PASSWORD = "AdoLover*24/10/2002";

    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a MySQL establecida correctamente");
            return connection;
        } catch (SQLException e) {
            System.err.println("Error conectando a la base de datos: " + e.getMessage());
            System.err.println("Verifica que:");
            System.err.println("   - MySQL esté ejecutándose");
            System.err.println("   - La base de datos 'store_db' exista");
            System.err.println("   - Usuario y contraseña sean correctos");
            throw e;
        }
    }
}
