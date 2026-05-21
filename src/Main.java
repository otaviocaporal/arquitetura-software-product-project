import crawler.PriceCrawler;
import domain.Product;
import domain.ProductLink;
import service.ProductService;

public class Main {
    public static void main(String[] args) {
        ProductService productService = new ProductService();

        // Cadastrar produto de exemplo
        Product produto = new Product("PS5-01", "PlayStation 5", null);
        
        // Adicionar links de lojas diferentes
        produto.addLink(new ProductLink("Amazon", "https://www.amazon.com.br/Console-PlayStation-5-Edi%C3%A7%C3%A3o-Digital/dp/B0BHTY49N6"));
        produto.addLink(new ProductLink("Kabum", "https://www.kabum.com.br/produto/478566/console-playstation-5-digital-edition-ps5"));
        
        productService.create(produto);

        System.out.println("Lista inicial de produtos:");
        productService.listAll();

        // Executar o crawler
        System.out.println("\n--------------------------\n");
        PriceCrawler crawler = new PriceCrawler(productService);
        crawler.run();

        // Exibir a lista de produtos após o crawler
        System.out.println("\n--------------------------\n");
        System.out.println("Lista de produtos após execução do crawler:");
        productService.listAll();
    }
}
