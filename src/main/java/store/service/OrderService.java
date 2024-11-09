package store.service;

import java.util.ArrayList;
import java.util.List;
import store.model.OrderInfo;
import store.model.OrderProduct;
import store.model.PriceInfo;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.model.PromotionInfo;
import store.model.Receipt;
import store.validator.OrderValidator;

public class OrderService {
    private static final int MIN_QUANTITY = 0;
    private final OrderValidator orderValidator;

    public OrderService(OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    public Receipt order(List<OrderProduct> orderProducts, Products products) {
        List<OrderInfo> orderInfos = new ArrayList<>();
        List<PromotionInfo> promotionInfos = new ArrayList<>();

        for (OrderProduct orderProduct : orderProducts) {
            processOrder(orderProduct, products, orderInfos, promotionInfos);
        }

        return new Receipt(orderInfos, promotionInfos, new PriceInfo(orderInfos, promotionInfos));
    }

    private void processOrder(OrderProduct orderProduct, Products products,
                              List<OrderInfo> orderInfos, List<PromotionInfo> promotionInfos) {
        Product promotionProduct = products.findProductWithPromotion(orderProduct.productName());
        Product nonPromotionProduct = products.findProductWithoutPromotion(orderProduct.productName());
        int quantity = orderProduct.quantity();
        orderValidator.validate(promotionProduct, nonPromotionProduct, quantity);

        int processedQuantity = processPromotionStock(promotionProduct, quantity,
                orderProduct.productName(), promotionInfos);
        processNonPromotionStock(nonPromotionProduct, processedQuantity, promotionProduct);

        addOrderInfo(orderInfos, orderProduct, nonPromotionProduct);
    }

    private int processPromotionStock(Product promotionProduct, int quantity,
                                      String productName, List<PromotionInfo> promotionInfos) {
        int decreasedQuantity = decreasePromotionStock(promotionProduct, quantity);

        if (quantity == decreasedQuantity) {
            return quantity;
        }

        addPromotionInfo(promotionProduct, quantity, decreasedQuantity, productName, promotionInfos);
        return decreasedQuantity;
    }

    private void addPromotionInfo(Product promotionProduct, int quantity, int decreasedQuantity,
                                  String productName, List<PromotionInfo> promotionInfos) {
        int actualQuantity = quantity - decreasedQuantity;
        Promotion promotion = promotionProduct.getPromotion();
        int bonusQuantity = promotion.calculateBonusQuantity(actualQuantity);

        promotionInfos.add(new PromotionInfo(productName, bonusQuantity, promotionProduct.getPrice()));
    }

    private void processNonPromotionStock(Product nonPromotionProduct, int quantity, Product promotionProduct) {
        int decreasedNonPromotionQuantity = decreaseNonPromotionStock(nonPromotionProduct, quantity);
        decreaseInActivePromotionStock(promotionProduct, decreasedNonPromotionQuantity);
    }

    private void addOrderInfo(List<OrderInfo> orderInfos, OrderProduct orderProduct, Product nonPromotionProduct) {
        orderInfos.add(new OrderInfo(
                orderProduct.productName(),
                orderProduct.quantity(),
                orderProduct.quantity() * nonPromotionProduct.getPrice())
        );
    }

    private boolean isPromotionActive(Product promotionProduct) {
        return promotionProduct != null &&
                promotionProduct.getPromotion() != null &&
                promotionProduct.getPromotion().isActive();
    }

    private int decreasePromotionStock(Product promotionProduct, int quantity) {
        if (isPromotionActive(promotionProduct)) {
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

    private void decreaseInActivePromotionStock(Product promotionProduct, int quantity) {
        if (quantity > MIN_QUANTITY && promotionProduct != null && !isPromotionActive(promotionProduct)) {
            promotionProduct.decreaseQuantity(quantity);
        }
    }
}