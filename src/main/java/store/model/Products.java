package store.model;

import static store.validator.ValidationMessage.EXCEED_STOCK_QUANTITY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import store.exception.InvalidInputException;

public record Products(List<Product> products) {
    private static final int MIN_QUANTITY = 0;

    public Products(List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    public void decreaseProductStock(String productName, int quantity) {
        Product promotionProduct = findProductWithPromotion(productName);
        Product nonPromotionProduct = findProductWithoutPromotion(productName);
        validateInitialStock(promotionProduct, nonPromotionProduct, quantity);
        boolean isPromotionActive = promotionProduct != null && promotionProduct.getPromotion().isActive();
        quantity = processPromotionStockDecrease(promotionProduct, quantity, isPromotionActive);
        quantity = processNonPromotionStockDecrease(nonPromotionProduct, quantity);
        processInActivePromotionStockDecrease(promotionProduct, quantity, isPromotionActive);
    }

    private int processPromotionStockDecrease(Product promotionProduct, int quantity, boolean isPromotionActive) {
        if (isPromotionActive) {
            quantity = decreasePromotionStock(promotionProduct, quantity);
        }

        return quantity;
    }

    private int processNonPromotionStockDecrease(Product nonPromotionProduct, int quantity) {
        if (quantity > MIN_QUANTITY && nonPromotionProduct != null) {
            int nonPromotionQuantity = Math.min(nonPromotionProduct.getQuantity(), quantity);
            decreaseNonPromotionStock(nonPromotionProduct, nonPromotionQuantity);
            quantity -= nonPromotionQuantity;
        }
        return quantity;
    }

    private void processInActivePromotionStockDecrease(Product promotionProduct,
                                                       int quantity,
                                                       boolean isPromotionActive) {
        if (quantity > MIN_QUANTITY && promotionProduct != null && !isPromotionActive) {
            promotionProduct.decreaseQuantity(quantity);
        }
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

    private int decreasePromotionStock(Product product, int quantity) {
        if (product == null || product.getPromotion() == null || !product.getPromotion().isActive()) {
            return quantity;
        }

        int promotionQuantity = Math.min(product.getQuantity(), quantity);
        product.decreaseQuantity(promotionQuantity);
        return quantity - promotionQuantity;
    }

    private void decreaseNonPromotionStock(Product product, int quantity) {
        if (product == null || quantity <= MIN_QUANTITY) {
            return;
        }

        product.decreaseQuantity(quantity);
    }

    private void validateInitialStock(Product promotionProduct, Product nonPromotionProduct, int quantity) {
        int totalQuantity = calculateTotalQuantity(promotionProduct, nonPromotionProduct);
        validateStock(totalQuantity, quantity);
    }

    private void validateStock(int totalQuantity, int quantity) {
        if (totalQuantity < quantity) {
            throw new InvalidInputException(EXCEED_STOCK_QUANTITY.getMessage());
        }
    }

    private Product findProductWithPromotion(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName) && product.getPromotion() != null)
                .findFirst()
                .orElse(null);
    }

    private Product findProductWithoutPromotion(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName) && product.getPromotion() == null)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Product> products() {
        return Collections.unmodifiableList(products);
    }
}
