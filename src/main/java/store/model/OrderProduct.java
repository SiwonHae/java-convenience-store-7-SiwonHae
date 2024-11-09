package store.model;

import static store.validator.ValidationMessage.INVALID_INPUT;

import store.exception.InvalidInputException;

public class OrderProduct {
    private final String productName;
    private int quantity;

    private static final int MIN_QUANTITY = 1;

    public OrderProduct(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
        validateMinQuantity(quantity);
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    private void validateMinQuantity(int quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new InvalidInputException(INVALID_INPUT.getMessage());
        }
    }
}
