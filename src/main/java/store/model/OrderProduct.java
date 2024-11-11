package store.model;

import static store.validator.ValidationMessage.INVALID_INPUT;
import static store.validator.ValidationMessage.NONE_PRODUCT_NAME;

import store.exception.InvalidInputException;

public class OrderProduct {
    private final String productName;
    private int quantity;

    private static final int MIN_QUANTITY = 1;

    public OrderProduct(String productName, int quantity, Products products) {
        validateMinQuantity(quantity);
        validateProductName(productName, products);
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private void validateMinQuantity(int quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new InvalidInputException(INVALID_INPUT.getMessage());
        }
    }

    private void validateProductName(String productName, Products products) {
        if (products.products().stream().noneMatch(product -> product.getName().equals(productName))) {
            throw new InvalidInputException(NONE_PRODUCT_NAME.getMessage());
        }
    }
}
