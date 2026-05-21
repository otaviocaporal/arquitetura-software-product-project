package crawler;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import domain.Product;
import domain.ProductLink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import service.ProductService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceCrawler {

    private final ProductService productService;

    public PriceCrawler(ProductService productService) {
        this.productService = productService;
    }

    public void run() {
        System.out.println("Iniciando Crawler de Preços...");
        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        try (Playwright playwright = Playwright.create()) {
            APIRequestContext requestContext = playwright.request().newContext();

            for (Product product : products) {
                System.out.println("Verificando produto: " + product.getName());
                
                Float minPrice = null;
                String bestStore = null;

                if (product.getLinks() == null || product.getLinks().isEmpty()) {
                    System.out.println("  Sem links cadastrados para este produto.");
                    continue;
                }

                for (ProductLink link : product.getLinks()) {
                    System.out.println("  Acessando " + link.getStoreName() + " (" + link.getUrl() + ")...");
                    try {
                        APIResponse response = requestContext.get(link.getUrl());
                        if (response.ok()) {
                            String html = new String(response.body());
                            Float price = extractPriceFromHtml(html);
                            
                            if (price != null) {
                                System.out.println("    Preço encontrado na " + link.getStoreName() + ": R$ " + price);
                                if (minPrice == null || price < minPrice) {
                                    minPrice = price;
                                    bestStore = link.getStoreName();
                                }
                            } else {
                                System.out.println("    Não foi possível encontrar o preço na " + link.getStoreName());
                            }
                        } else {
                            System.out.println("    Erro ao acessar " + link.getUrl() + " - Status: " + response.status());
                        }
                    } catch (Exception e) {
                        System.out.println("    Erro ao processar " + link.getStoreName() + ": " + e.getMessage());
                    }
                }

                if (minPrice != null) {
                    System.out.println("  Menor preço para " + product.getName() + ": R$ " + minPrice + " na " + bestStore);
                    product.setPrice(minPrice, bestStore);
                    productService.edit(product);
                } else {
                    System.out.println("  Não foi possível determinar o preço atual para o produto: " + product.getName());
                }
            }
        }
        System.out.println("Crawler finalizado.");
    }

    private Float extractPriceFromHtml(String html) {
        // Implementação simplificada usando expressão regular para encontrar padrões de preço em reais.
        // O JSoup é usado para limpar o HTML e obter apenas o texto da página, facilitando a busca.
        Document doc = Jsoup.parse(html);
        String text = doc.text();
        
        // Expressão regular genérica para pegar valores no formato R$ 1.234,56 ou R$ 1234,56 ou R$1.234,56 ou R$1234,56
        Pattern pattern = Pattern.compile("R\\$\\s*([\\d\\.]+)\\,(\\d{2})");
        Matcher matcher = pattern.matcher(text);
        
        Float lowestFound = null;
        
        while (matcher.find()) {
            try {
                String valueStr = matcher.group(1).replace(".", "") + "." + matcher.group(2);
                float currentPrice = Float.parseFloat(valueStr);
                
                // Pega o menor preço encontrado na página
                if (lowestFound == null || currentPrice < lowestFound) {
                    // Evita pegar preços irreais baixos que podem ser outro elemento na página
                    if (currentPrice > 0) {
                        lowestFound = currentPrice;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        
        return lowestFound;
    }
}
