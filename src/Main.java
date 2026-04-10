import adapters.DatabaseStorage;
import domain.Product;
import service.ProductService;
import utils.GenerationValue;

import java.sql.SQLOutput;

void main() {
    ProductService productService = new ProductService();

    Product produto = new Product(GenerationValue.generateUUID(), "123", "PRODUTO",  2f);

    produto.setPrice(3f);


}
