package store.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Products(List<Product> products) {
    public Products(List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    public Product findProductWithPromotion(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName) && product.getPromotion() != null)
                .findFirst()
                .orElse(null);
    }

    public Product findProductWithoutPromotion(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName) && product.getPromotion() == null)
                .findFirst()
                .orElse(null);
    }

    public List<Product> products() {
        return Collections.unmodifiableList(products);
    }
}
