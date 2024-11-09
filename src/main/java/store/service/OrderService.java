package store.service;

import static store.common.StoreRule.YES;

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
import store.validator.PromotionChoiceValidator;
import store.view.InputView;

public class OrderService {
    private static final int MIN_QUANTITY = 0;

    private final OrderValidator orderValidator;
    private final PromotionChoiceValidator promotionChoiceValidator;

    public OrderService(OrderValidator orderValidator, PromotionChoiceValidator promotionChoiceValidator) {
        this.orderValidator = orderValidator;
        this.promotionChoiceValidator = promotionChoiceValidator;
    }

    public Receipt order(List<OrderProduct> orderProducts, Products products) {
        List<OrderInfo> orderInfos = new ArrayList<>();
        List<PromotionInfo> promotionInfos = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            Product promotionProduct = products.findProductWithPromotion(orderProduct.getProductName());
            Product nonPromotionProduct = products.findProductWithoutPromotion(orderProduct.getProductName());

            requestPromotion(orderProduct, promotionProduct);
            processOrder(orderProduct, promotionProduct, nonPromotionProduct, orderInfos, promotionInfos);
        }
        return new Receipt(orderInfos, promotionInfos, new PriceInfo(orderInfos, promotionInfos));
    }

    private void requestPromotion(OrderProduct orderProduct, Product promotionProduct) {
        if (promotionProduct != null && promotionProduct.getPromotion()
                .calculateQuantityForPromotion(orderProduct.getQuantity())
                && isPromotionActive(promotionProduct)) {
            if (promotionProduct.getQuantity() > orderProduct.getQuantity() && choicePromotion(
                    orderProduct.getProductName())) {
                orderProduct.increaseQuantity();
            }
        }
    }

    private void processOrder(OrderProduct orderProduct, Product promotionProduct, Product nonPromotionProduct,
                              List<OrderInfo> orderInfos, List<PromotionInfo> promotionInfos) {
        int quantity = orderProduct.getQuantity();
        orderValidator.validate(promotionProduct, nonPromotionProduct, quantity);

        int processedQuantity = processPromotionStock(promotionProduct, quantity, promotionInfos);
        processNonPromotionStock(nonPromotionProduct, processedQuantity, promotionProduct);

        addOrderInfo(orderInfos, orderProduct, nonPromotionProduct);
    }

    private int processPromotionStock(Product promotionProduct, int quantity, List<PromotionInfo> promotionInfos) {
        int shortageQuantity = processShortageQuantity(promotionProduct, quantity);
        int decreasedQuantity = decreasePromotionStock(promotionProduct, shortageQuantity);
        if (shortageQuantity == decreasedQuantity) {
            return shortageQuantity;
        }
        int actualQuantity = shortageQuantity - decreasedQuantity;
        Promotion promotion = promotionProduct.getPromotion();
        int bonusQuantity = promotion.calculateBonusQuantity(actualQuantity);
        addPromotionInfo(promotionProduct, bonusQuantity, promotionInfos);
        return decreasedQuantity;
    }

    private int processShortageQuantity(Product promotionProduct, int quantity) {
        if (promotionProduct != null && quantity >= promotionProduct.getQuantity()) {
            int shortageQuantity = promotionProduct.getPromotion()
                    .calculateNonPromotionQuantity(quantity, promotionProduct.getQuantity());
            if (!choiceRegularPrice(promotionProduct.getName(), shortageQuantity)) {
                quantity -= shortageQuantity;
            }
        }

        return quantity;
    }

    private boolean choiceRegularPrice(String productName, int shortageQuantity) {
        String choice = InputView.readShortageStockChoice(productName, shortageQuantity);
        promotionChoiceValidator.validate(choice);
        return choice.equals(YES.getValue());
    }

    private void addPromotionInfo(Product promotionProduct, int bonusQuantity, List<PromotionInfo> promotionInfos) {
        if (bonusQuantity > 0) {
            promotionInfos.add(
                    new PromotionInfo(promotionProduct.getName(), bonusQuantity, promotionProduct.getPrice()));
        }
    }

    private boolean choicePromotion(String productName) {
        String choice = InputView.readPromotionChoice(productName);
        promotionChoiceValidator.validate(choice);
        return choice.equals(YES.getValue());
    }

    private void processNonPromotionStock(Product nonPromotionProduct, int quantity, Product promotionProduct) {
        int decreasedNonPromotionQuantity = decreaseNonPromotionStock(nonPromotionProduct, quantity);
        decreaseInActivePromotionStock(promotionProduct, decreasedNonPromotionQuantity);
    }

    private void addOrderInfo(List<OrderInfo> orderInfos, OrderProduct orderProduct, Product nonPromotionProduct) {
        orderInfos.add(new OrderInfo(
                orderProduct.getProductName(),
                orderProduct.getQuantity(),
                orderProduct.getQuantity() * nonPromotionProduct.getPrice())
        );
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

    private boolean isPromotionActive(Product promotionProduct) {
        return promotionProduct != null &&
                promotionProduct.getPromotion() != null &&
                promotionProduct.getPromotion().isActive();
    }
}