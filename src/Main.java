import adapters.DatabaseStorage;
import domain.Product;
import service.ProductService;

import java.sql.SQLOutput;

void main() {
    ProductService productService = new ProductService();
    productService.create(new Product(productService.generateUUID(), "123", "PRODUTO",  new BigDecimal(10.9)));

    productService.listAll();
}
