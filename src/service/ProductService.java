package service;

import adapters.DatabaseStorage;
import domain.Product;

public class ProductService extends BaseService {
    public ProductService() {
        this.armazenamento = new DatabaseStorage<>(Product.class);
    }

    public java.util.List<Product> getAllProducts() {
        java.util.List<domain.EntityInterface> entities = this.getAll();
        java.util.List<Product> products = new java.util.ArrayList<>();
        if (entities != null) {
            for (domain.EntityInterface e : entities) {
                products.add((Product) e);
            }
        }
        return products;
    }
}
