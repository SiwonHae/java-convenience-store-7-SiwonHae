package store.model;

import static store.validator.ValidationMessage.INVALID_INPUT;

import store.exception.InvalidInputException;

public record OrderProduct(String productName, int quantity) {
    private static final int MIN_QUANTITY = 1;

    public OrderProduct {
        validateMinQuantity(quantity);
    }

    private void validateMinQuantity(int quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new InvalidInputException(INVALID_INPUT.getMessage());
        }
    }
}
