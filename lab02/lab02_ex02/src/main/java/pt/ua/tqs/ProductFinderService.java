package pt.ua.tqs;

import java.util.Optional;

public class ProductFinderService {
    private final ISimpleHttpClient httpClient;
    public String API_PRODUCTS = "https://fakestoreapi.com/products";

    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Optional<Product> findProductDetails(Integer id) {
        String json = httpClient.doHttpGet(API_PRODUCTS + "/" + id);

        if (json == null || json.isEmpty()) {
            return Optional.empty();
        }
        try {
            String idStr = json.split("\"id\":")[1].split(",")[0].trim();
            Product product = getProduct(json, idStr);
            return Optional.of(product);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Product getProduct(String json, String idStr) {
        String image = json.split("\"image\":\"")[1].split("\"")[0];
        String description = json.split("\"description\":\"")[1].split("\"")[0];
        String priceStr = json.split("\"price\":")[1].split(",")[0].trim();
        String title = json.split("\"title\":\"")[1].split("\"")[0];
        String category = json.split("\"category\":\"")[1].split("\"")[0];

        Double price = Double.parseDouble(priceStr);
        return new Product(idStr, image, description, price, title, category);
    }

}
