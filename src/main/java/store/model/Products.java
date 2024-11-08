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

    public void validateProductName(String name) {
        if (!containsProductName(name)) {
            throw new InvalidInputException(NONE_PRODUCT_NAME.getMessage());
        }
    }

    @Override
    public List<Product> products() {
        return Collections.unmodifiableList(products);
    }

    private boolean containsProductName(String name) {
        return products.stream()
                .anyMatch(product -> product.getName().equals(name));
    }
}
