package store.model;

import static store.validator.ValidationMessage.NONE_PRODUCT_NAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import store.exception.InvalidInputException;

public record Products(List<Product> products) {
    public Products(List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    public void decreaseProductStock(String productName, int quantity) {
        Product product = findProductByName(productName);
        product.validateStock(quantity);
        product.decreaseQuantity(quantity);
    }

    private Product findProductByName(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException(NONE_PRODUCT_NAME.getMessage()));
    }

    @Override
    public List<Product> products() {
        return Collections.unmodifiableList(products);
    }
}
