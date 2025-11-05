import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    
    public Product addProduct(Product product) throws SQLException, CustomException {
        // Validar producto antes de insertar
        validarProducto(product);
        
        String sql = "INSERT INTO products (name, price, stock, category, description) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStock());
            stmt.setString(4, product.getCategory());
            stmt.setString(5, product.getDescription());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Error al crear el producto, ninguna fila afectada.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                    return product;
                } else {
                    throw new SQLException("Error al crear el producto, no se obtuvo el ID.");
                }
            }
        }
    }

    public Product getProductById(int id) throws SQLException, NegativeNumberException {
        if (id <= 0) {
            throw new NegativeNumberException("ID");
        }
        
        String sql = "SELECT * FROM products WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("category"),
                        rs.getString("description")
                    );
                } else {
                    return null;
                }
            } catch (NullOrEmptyException e) {
                throw new SQLException("Error en datos de la base de datos: " + e.getMessage());
            }
        }
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                try {
                    Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("category"),
                        rs.getString("description")
                    );
                    products.add(product);
                } catch (CustomException e) {
                    System.err.println("Advertencia: Producto con ID " + rs.getInt("id") + 
                                     " tiene datos inválidos: " + e.getMessage());
                }
            }
        }
        return products;
    }

    public boolean updateProduct(Product product) throws SQLException, CustomException {
        // Validar producto antes de actualizar
        validarProducto(product);
        
        if (product.getId() <= 0) {
            throw new NegativeNumberException("ID");
        }
        
        String sql = "UPDATE products SET name = ?, price = ?, stock = ?, category = ?, description = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStock());
            stmt.setString(4, product.getCategory());
            stmt.setString(5, product.getDescription());
            stmt.setInt(6, product.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean deleteProduct(int id) throws SQLException, NegativeNumberException {
        if (id <= 0) {
            throw new NegativeNumberException("ID");
        }
        
        String sql = "DELETE FROM products WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    // Método auxiliar para validar producto
    private void validarProducto(Product product) throws CustomException {
        if (product == null) {
            throw new NullOrEmptyException("producto");
        }
        
        // Este método asegura que el producto completo sea válido
        try {
            product.setName(product.getName());
            product.setPrice(product.getPrice());
            product.setStock(product.getStock());
            product.setCategory(product.getCategory());
            product.setDescription(product.getDescription());
        } catch (CustomException e) {
            throw e; // Re-lanzar la excepción original
        }
    }
}
