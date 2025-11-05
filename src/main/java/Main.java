import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ProductDAO productDAO = new ProductDAO();
    
    public static void main(String[] args) {
        System.out.println("SISTEMA DE GESTION DE INVENTARIO - TIENDA SEGUNDA MANO");
        System.out.println("=======================================================\n");
        
        mostrarMenuPrincipal();
    }
    
    private static void mostrarMenuPrincipal() {
        int opcion;
        
        do {
            System.out.println("\n────────── MENU PRINCIPAL ──────────");
            System.out.println("1. Agregar nuevo producto");
            System.out.println("2. Ver todos los productos");
            System.out.println("3. Buscar producto por ID");
            System.out.println("4. Actualizar producto");
            System.out.println("5. Eliminar producto");
            System.out.println("6. Ver productos por categoria");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            
            opcion = leerEntero();
            
            switch (opcion) {
                case 1:
                    agregarProducto();
                    break;
                case 2:
                    verTodosLosProductos();
                    break;
                case 3:
                    buscarProductoPorId();
                    break;
                case 4:
                    actualizarProducto();
                    break;
                case 5:
                    eliminarProducto();
                    break;
                case 6:
                    verProductosPorCategoria();
                    break;
                case 0:
                    System.out.println("Gracias por usar el sistema!");
                    break;
                default:
                    System.out.println("Opcion no valida. Intente nuevamente.");
            }
        } while (opcion != 0);
        
        scanner.close();
    }
    
    private static void agregarProducto() {
        System.out.println("\n────────── AGREGAR NUEVO PRODUCTO ──────────");
        
        try {
            System.out.print("Nombre del producto: ");
            String nombre = leerTextoNoVacio("nombre");
            
            System.out.print("Precio: ");
            double precio = leerDoublePositivo();
            
            System.out.print("Stock disponible: ");
            int stock = leerEnteroPositivo();
            
            System.out.print("Categoria: ");
            String categoria = leerTextoNoVacio("categoria");
            
            System.out.print("Descripcion: ");
            String descripcion = leerTextoNoVacio("descripcion");
            
            Product nuevoProducto = new Product(nombre, precio, stock, categoria, descripcion);
            Product productoGuardado = productDAO.addProduct(nuevoProducto);
            
            System.out.println("Producto agregado exitosamente!");
            System.out.println("ID asignado: " + productoGuardado.getId());
            
        } catch (CustomException e) {
            System.out.println("Error de validacion: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }
    
    private static void verTodosLosProductos() {
        System.out.println("\n────────── TODOS LOS PRODUCTOS ──────────");
        
        try {
            List<Product> productos = productDAO.getAllProducts();
            
            if (productos.isEmpty()) {
                System.out.println("No hay productos en el inventario.");
            } else {
                System.out.println("Total de productos: " + productos.size());
                System.out.println("┌─────┬──────────────────────────┬──────────┬───────┬────────────────┬────────────────────────────┐");
                System.out.println("│ ID  │ Nombre                   │ Precio   │ Stock │ Categoría      │ Descripción                │");
                System.out.println("├─────┼──────────────────────────┼──────────┼───────┼────────────────┼────────────────────────────┤");
                
                for (Product p : productos) {
                    System.out.printf("│ %-3d │ %-24s │ $%-7.2f │ %-5d │ %-14s │ %-26s │\n",
                            p.getId(), 
                            acortarTexto(p.getName(), 24),
                            p.getPrice(),
                            p.getStock(),
                            acortarTexto(p.getCategory(), 14),
                            acortarTexto(p.getDescription(), 26));
                }
                System.out.println("└─────┴──────────────────────────┴──────────┴───────┴────────────────┴────────────────────────────┘");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
    }
    
    private static void buscarProductoPorId() {
        System.out.println("\n────────── BUSCAR PRODUCTO POR ID ──────────");
        
        try {
            System.out.print("Ingrese el ID del producto: ");
            int id = leerEnteroPositivo();
            
            Product producto = productDAO.getProductById(id);
            
            if (producto != null) {
                System.out.println("\nPRODUCTO ENCONTRADO:");
                System.out.println("┌────────────────────────────────────────────────────────┐");
                System.out.printf("│ ID:          %-43d │\n", producto.getId());
                System.out.printf("│ Nombre:      %-43s │\n", producto.getName());
                System.out.printf("│ Precio:      $%-41.2f │\n", producto.getPrice());
                System.out.printf("│ Stock:       %-43d │\n", producto.getStock());
                System.out.printf("│ Categoría:   %-43s │\n", producto.getCategory());
                System.out.printf("│ Descripción: %-43s │\n", producto.getDescription());
                System.out.println("└────────────────────────────────────────────────────────┘");
            } else {
                System.out.println("No se encontro ningun producto con ID: " + id);
            }
            
        } catch (NegativeNumberException e) {
            System.out.println("Error de validacion: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
    }
    
    private static void actualizarProducto() {
        System.out.println("\n────────── ACTUALIZAR PRODUCTO ──────────");
        
        try {
            System.out.print("Ingrese el ID del producto a actualizar: ");
            int id = leerEnteroPositivo();
            
            Product productoExistente = productDAO.getProductById(id);
            
            if (productoExistente == null) {
                System.out.println("No se encontro ningun producto con ID: " + id);
                return;
            }
            
            // Mostrar datos actuales
            System.out.println("\nDATOS ACTUALES:");
            System.out.println("┌────────────────────────────────────────────────────────┐");
            System.out.printf("│ Nombre:      %-43s │\n", productoExistente.getName());
            System.out.printf("│ Precio:      $%-41.2f │\n", productoExistente.getPrice());
            System.out.printf("│ Stock:       %-43d │\n", productoExistente.getStock());
            System.out.printf("│ Categoría:   %-43s │\n", productoExistente.getCategory());
            System.out.printf("│ Descripción: %-43s │\n", productoExistente.getDescription());
            System.out.println("└────────────────────────────────────────────────────────┘");
            
            System.out.println("\nIngrese los nuevos datos (deje vacio para mantener el valor actual):");
            
            // Nombre
            System.out.print("Nuevo nombre [" + productoExistente.getName() + "]: ");
            String nuevoNombre = scanner.nextLine();
            if (!nuevoNombre.trim().isEmpty()) {
                productoExistente.setName(nuevoNombre);
            }
            
            // Precio
            System.out.print("Nuevo precio [" + productoExistente.getPrice() + "]: ");
            String precioInput = scanner.nextLine();
            if (!precioInput.trim().isEmpty()) {
                try {
                    double nuevoPrecio = Double.parseDouble(precioInput);
                    if (nuevoPrecio < 0) {
                        System.out.println("Precio no puede ser negativo, se mantiene el actual.");
                    } else {
                        productoExistente.setPrice(nuevoPrecio);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Precio no valido, se mantiene el actual.");
                }
            }
            
            // Stock
            System.out.print("Nuevo stock [" + productoExistente.getStock() + "]: ");
            String stockInput = scanner.nextLine();
            if (!stockInput.trim().isEmpty()) {
                try {
                    int nuevoStock = Integer.parseInt(stockInput);
                    if (nuevoStock < 0) {
                        System.out.println("Stock no puede ser negativo, se mantiene el actual.");
                    } else {
                        productoExistente.setStock(nuevoStock);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Stock no valido, se mantiene el actual.");
                }
            }
            
            // Categoria
            System.out.print("Nueva categoria [" + productoExistente.getCategory() + "]: ");
            String nuevaCategoria = scanner.nextLine();
            if (!nuevaCategoria.trim().isEmpty()) {
                productoExistente.setCategory(nuevaCategoria);
            }
            
            // Descripcion
            System.out.print("Nueva descripcion [" + productoExistente.getDescription() + "]: ");
            String nuevaDescripcion = scanner.nextLine();
            if (!nuevaDescripcion.trim().isEmpty()) {
                productoExistente.setDescription(nuevaDescripcion);
            }
            
            // Confirmar actualizacion
            System.out.print("\nEsta seguro de actualizar el producto? (s/n): ");
            String confirmacion = scanner.nextLine();
            
            if (confirmacion.equalsIgnoreCase("s")) {
                boolean actualizado = productDAO.updateProduct(productoExistente);
                if (actualizado) {
                    System.out.println("Producto actualizado exitosamente!");
                } else {
                    System.out.println("Error al actualizar el producto.");
                }
            } else {
                System.out.println("Actualizacion cancelada.");
            }
            
        } catch (CustomException e) {
            System.out.println("Error de validacion: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
    }
    
    private static void eliminarProducto() {
        System.out.println("\n────────── ELIMINAR PRODUCTO ──────────");
        
        try {
            System.out.print("Ingrese el ID del producto a eliminar: ");
            int id = leerEnteroPositivo();
            
            Product producto = productDAO.getProductById(id);
            
            if (producto == null) {
                System.out.println("No se encontro ningun producto con ID: " + id);
                return;
            }
            
            System.out.println("\nPRODUCTO A ELIMINAR:");
            System.out.println("┌────────────────────────────────────────────────────────┐");
            System.out.printf("│ Nombre:      %-43s │\n", producto.getName());
            System.out.printf("│ Precio:      $%-41.2f │\n", producto.getPrice());
            System.out.printf("│ Categoría:   %-43s │\n", producto.getCategory());
            System.out.println("└────────────────────────────────────────────────────────┘");
            
            System.out.print("\nEsta SEGURO de que desea eliminar este producto? (s/n): ");
            String confirmacion = scanner.nextLine();
            
            if (confirmacion.equalsIgnoreCase("s")) {
                boolean eliminado = productDAO.deleteProduct(id);
                if (eliminado) {
                    System.out.println("Producto eliminado exitosamente!");
                } else {
                    System.out.println("Error al eliminar el producto.");
                }
            } else {
                System.out.println("Eliminacion cancelada.");
            }
            
        } catch (NegativeNumberException e) {
            System.out.println("Error de validacion: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
    }
    
    private static void verProductosPorCategoria() {
        System.out.println("\n────────── PRODUCTOS POR CATEGORIA ──────────");
        System.out.print("Ingrese la categoria a buscar: ");
        String categoria = scanner.nextLine();
        
        try {
            List<Product> productos = productDAO.getAllProducts();
            List<Product> productosFiltrados = productos.stream()
                    .filter(p -> p.getCategory().toLowerCase().contains(categoria.toLowerCase()))
                    .toList();
            
            if (productosFiltrados.isEmpty()) {
                System.out.println("No hay productos en la categoria: " + categoria);
            } else {
                System.out.println("\nProductos en categoria '" + categoria + "': " + productosFiltrados.size());
                System.out.println("┌─────┬──────────────────────────┬──────────┬───────┬────────────────────────────┐");
                System.out.println("│ ID  │ Nombre                   │ Precio   │ Stock │ Descripción                │");
                System.out.println("├─────┼──────────────────────────┼──────────┼───────┼────────────────────────────┤");
                
                for (Product p : productosFiltrados) {
                    System.out.printf("│ %-3d │ %-24s │ $%-7.2f │ %-5d │ %-26s │\n",
                            p.getId(), 
                            acortarTexto(p.getName(), 24),
                            p.getPrice(),
                            p.getStock(),
                            acortarTexto(p.getDescription(), 26));
                }
                System.out.println("└─────┴──────────────────────────┴──────────┴───────┴────────────────────────────┘");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
    }
    
    // Metodos auxiliares mejorados con validaciones
    private static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada no valida. Ingrese un numero entero: ");
            }
        }
    }
    
    private static int leerEnteroPositivo() {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine());
                if (valor < 0) {
                    System.out.print("El numero no puede ser negativo. Ingrese un numero positivo: ");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.print("Entrada no valida. Ingrese un numero entero: ");
            }
        }
    }
    
    private static double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada no valida. Ingrese un numero decimal: ");
            }
        }
    }
    
    private static double leerDoublePositivo() {
        while (true) {
            try {
                double valor = Double.parseDouble(scanner.nextLine());
                if (valor < 0) {
                    System.out.print("El numero no puede ser negativo. Ingrese un numero positivo: ");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.print("Entrada no valida. Ingrese un numero decimal: ");
            }
        }
    }
    
    private static String leerTextoNoVacio(String campo) {
        while (true) {
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.print("El campo '" + campo + "' no puede estar vacio. Ingrese un valor: ");
        }
    }
    
    private static String acortarTexto(String texto, int longitud) {
        if (texto.length() <= longitud) {
            return texto;
        }
        return texto.substring(0, longitud - 3) + "...";
    }
}
//:)