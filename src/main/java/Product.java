public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private String category;
    private String description;

    // Constructor sin id (para nuevos productos)
    public Product(String name, double price, int stock, String category, String description) 
            throws NullOrEmptyException, NegativeNumberException {
        setName(name);
        setPrice(price);
        setStock(stock);
        setCategory(category);
        setDescription(description);
    }

    // Constructor completo (para productos existentes)
    public Product(int id, String name, double price, int stock, String category, String description) 
            throws NullOrEmptyException, NegativeNumberException {
        this.id = id;
        setName(name);
        setPrice(price);
        setStock(stock);
        setCategory(category);
        setDescription(description);
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }

    // Setters con validaciones
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) throws NullOrEmptyException {
        if (name == null || name.trim().isEmpty()) {
            throw new NullOrEmptyException("nombre");
        }
        this.name = name.trim();
    }

    public void setPrice(double price) throws NegativeNumberException {
        if (price < 0) {
            throw new NegativeNumberException("precio");
        }
        this.price = price;
    }

    public void setStock(int stock) throws NegativeNumberException {
        if (stock < 0) {
            throw new NegativeNumberException("stock");
        }
        this.stock = stock;
    }

    public void setCategory(String category) throws NullOrEmptyException {
        if (category == null || category.trim().isEmpty()) {
            throw new NullOrEmptyException("categoría");
        }
        this.category = category.trim();
    }

    public void setDescription(String description) throws NullOrEmptyException {
        if (description == null || description.trim().isEmpty()) {
            throw new NullOrEmptyException("descripción");
        }
        this.description = description.trim();
    }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', price=%.2f, stock=%d, category='%s', description='%s'}",
                id, name, price, stock, category, description);
    }
}
//:)