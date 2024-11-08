package store.service;

import java.util.List;
import store.model.OrderProduct;
import store.model.Product;
import store.model.Products;
import store.validator.OrderValidator;

public class OrderService {
    private static final int MIN_QUANTITY = 0;

    private final OrderValidator orderValidator;

    public OrderService(OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    public void order(List<OrderProduct> orderProducts, Products products) {
        for (OrderProduct orderProduct : orderProducts) {
            Product promotionProduct = products.findProductWithPromotion(orderProduct.productName());
            Product nonPromotionProduct = products.findProductWithoutPromotion(orderProduct.productName());
            int quantity = orderProduct.quantity();
            orderValidator.validate(promotionProduct, nonPromotionProduct, quantity);
            boolean isPromotionActive = promotionProduct != null && promotionProduct.getPromotion().isActive();
            quantity = decreasePromotionStock(promotionProduct, quantity);
            quantity = decreaseNonPromotionStock(nonPromotionProduct, quantity);
            decreaseInActivePromotionStock(promotionProduct, quantity, isPromotionActive);
        }
    }

    private int decreasePromotionStock(Product promotionProduct, int quantity) {
        if (promotionProduct != null
                && promotionProduct.getPromotion() != null
                && promotionProduct.getPromotion().isActive()) {
            int promotionQuantity = Math.min(promotionProduct.getQuantity(), quantity);
            promotionProduct.decreaseQuantity(promotionQuantity);
            return quantity - promotionQuantity;
        }
        return quantity;
    }

    private int decreaseNonPromotionStock(Product nonPromotionProduct, int quantity) {
        if (quantity > MIN_QUANTITY && nonPromotionProduct != null) {
            int nonPromotionQuantity = Math.min(nonPromotionProduct.getQuantity(), quantity);
            nonPromotionProduct.decreaseQuantity(nonPromotionQuantity);
            return quantity - nonPromotionQuantity;
        }
        return quantity;
    }

    private void decreaseInActivePromotionStock(Product promotionProduct,
                                                int quantity,
                                                boolean isPromotionActive) {
        if (quantity > MIN_QUANTITY && promotionProduct != null && !isPromotionActive) {
            promotionProduct.decreaseQuantity(quantity);
        }
    }
}
