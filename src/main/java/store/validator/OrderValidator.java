package store.validator;

import static store.validator.ValidationMessage.EXCEED_STOCK_QUANTITY;

import store.exception.InvalidInputException;
import store.model.Product;

public class OrderValidator {

    public void validate(Product promotionProduct, Product nonPromotionProduct, int quantity) {
        int totalQuantity = calculateTotalQuantity(promotionProduct, nonPromotionProduct);
        validateStock(totalQuantity, quantity);
    }

    private int calculateTotalQuantity(Product promotionProduct, Product nonPromotionProduct) {
        int totalQuantity = 0;
        if (promotionProduct != null) {
            totalQuantity += promotionProduct.getQuantity();
        }
        if (nonPromotionProduct != null) {
            totalQuantity += nonPromotionProduct.getQuantity();
        }
        return totalQuantity;
    }

    private void validateStock(int totalQuantity, int quantity) {
        if (totalQuantity < quantity) {
            throw new InvalidInputException(EXCEED_STOCK_QUANTITY.getMessage());
        }
    }

}
