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
import store.validator.ChoiceValidator;
import store.validator.OrderValidator;
import store.view.InputView;
import store.view.OutputView;

public class OrderService {
    private static final int MIN_QUANTITY = 0;

    private final OrderValidator orderValidator;
    private final ChoiceValidator choiceValidator;

    public OrderService(OrderValidator orderValidator, ChoiceValidator choiceValidator) {
        this.orderValidator = orderValidator;
        this.choiceValidator = choiceValidator;
    }

    public Receipt order(List<OrderProduct> orderProducts, Products products) {
        List<OrderInfo> orderInfos = new ArrayList<>();
        List<PromotionInfo> promotionInfos = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            Product promotionProduct = products.findProductWithPromotion(orderProduct.getProductName());
            Product nonPromotionProduct = products.findProductWithoutPromotion(orderProduct.getProductName());
            orderValidator.validate(promotionProduct, nonPromotionProduct, orderProduct.getQuantity());
            processOrder(orderProduct, promotionProduct, nonPromotionProduct, orderInfos, promotionInfos);
        }
        return new Receipt(orderInfos, promotionInfos, new PriceInfo(orderInfos, promotionInfos, choiceMembership()));
    }

    private void processOrder(OrderProduct orderProduct, Product promotionProduct, Product nonPromotionProduct,
                              List<OrderInfo> orderInfos, List<PromotionInfo> promotionInfos) {
        boolean addPromotion = tryApplyPromotion(orderProduct, promotionProduct);
        int remainQuantityAfterPromotion = orderProduct.getQuantity();
        if (isPromotionActive(promotionProduct)) {
            remainQuantityAfterPromotion = processPromotionQuantity(promotionProduct, promotionInfos, orderProduct,
                    addPromotion);
        }
        processNonPromotionQuantity(nonPromotionProduct, remainQuantityAfterPromotion, promotionProduct);
        if (orderProduct.getQuantity() != MIN_QUANTITY) {
            addOrderInfo(orderInfos, orderProduct, nonPromotionProduct);
        }
    }

    private boolean tryApplyPromotion(OrderProduct orderProduct, Product promotionProduct) {
        if (isPromotionActive(promotionProduct)
                && promotionProduct.getPromotion().canReceivePromotion(orderProduct.getQuantity())
                && promotionProduct.getQuantity() > orderProduct.getQuantity()) {
            if (!choicePromotion(orderProduct.getProductName())) {
                return false;
            }
            orderProduct.increaseQuantity(promotionProduct.getPromotion().getGet());
        }
        return true;
    }

    private int processPromotionQuantity(Product promotionProduct, List<PromotionInfo> promotionInfos,
                                         OrderProduct orderProduct, boolean addPromotion) {
        int updateOrderQuantity = processShortagePromotionQuantity(promotionProduct, orderProduct);
        int remainQuantityAfterPromotion = decreasePromotionQuantity(promotionProduct, updateOrderQuantity);
        if (updateOrderQuantity == remainQuantityAfterPromotion) {
            return updateOrderQuantity;
        }
        int bonusQuantity = promotionProduct.getPromotion()
                .calculateBonusQuantity(updateOrderQuantity - remainQuantityAfterPromotion);
        addPromotionInfo(promotionProduct, checkAddPromotion(promotionProduct, addPromotion, bonusQuantity),
                promotionInfos);
        return remainQuantityAfterPromotion;
    }

    private int processShortagePromotionQuantity(Product promotionProduct, OrderProduct orderProduct) {
        int orderQuantity = orderProduct.getQuantity();
        if (promotionProduct != null && orderQuantity >= promotionProduct.getQuantity()) {
            int shortageQuantity = promotionProduct.getPromotion()
                    .calculateNonPromotionQuantity(orderQuantity, promotionProduct.getQuantity());
            if (!choiceRegularPrice(promotionProduct.getName(), shortageQuantity)) {
                orderQuantity -= shortageQuantity;
            }
        }
        orderProduct.setQuantity(orderQuantity);
        return orderQuantity;
    }

    private int decreasePromotionQuantity(Product promotionProduct, int orderQuantity) {
        if (isPromotionActive(promotionProduct)) {
            int promotionQuantity = Math.min(promotionProduct.getQuantity(), orderQuantity);
            promotionProduct.decreaseQuantity(promotionQuantity);
            return orderQuantity - promotionQuantity;
        }
        return orderQuantity;
    }

    private void processNonPromotionQuantity(Product nonPromotionProduct, int orderQuantity, Product promotionProduct) {
        int remainQuantityAfterNonPromotion = decreaseNonPromotionQuantity(nonPromotionProduct, orderQuantity);
        decreaseInActivePromotionQuantity(promotionProduct, remainQuantityAfterNonPromotion);
    }

    private int decreaseNonPromotionQuantity(Product nonPromotionProduct, int orderQuantity) {
        if (orderQuantity > MIN_QUANTITY && nonPromotionProduct != null) {
            int nonPromotionQuantity = Math.min(nonPromotionProduct.getQuantity(), orderQuantity);
            nonPromotionProduct.decreaseQuantity(nonPromotionQuantity);
            return orderQuantity - nonPromotionQuantity;
        }
        return orderQuantity;
    }

    private void decreaseInActivePromotionQuantity(Product promotionProduct, int orderQuantity) {
        if (orderQuantity > MIN_QUANTITY && promotionProduct != null && !isPromotionActive(promotionProduct)) {
            promotionProduct.decreaseQuantity(orderQuantity);
        }
    }

    private void addOrderInfo(List<OrderInfo> orderInfos, OrderProduct orderProduct, Product nonPromotionProduct) {
        orderInfos.add(new OrderInfo(
                orderProduct.getProductName(),
                orderProduct.getQuantity(),
                orderProduct.getQuantity() * nonPromotionProduct.getPrice())
        );
    }

    private void addPromotionInfo(Product promotionProduct, int bonusQuantity, List<PromotionInfo> promotionInfos) {
        if (bonusQuantity > 0) {
            Promotion promotion = promotionProduct.getPromotion();
            promotionInfos.add(
                    new PromotionInfo(promotionProduct.getName(), bonusQuantity, promotionProduct.getPrice(),
                            bonusQuantity * (promotion.getBuy() + promotion.getGet())));
        }
    }

    private int checkAddPromotion(Product promotionProduct, boolean addPromotion, int bonusQuantity) {
        if (!addPromotion) {
            bonusQuantity -= promotionProduct.getPromotion().getGet();
        }
        return bonusQuantity;
    }

    private boolean choiceOption(String choice) {
        try {
            choiceValidator.validate(choice);
            return choice.equals(YES.getValue());
        } catch (IllegalArgumentException e) {
            OutputView.printErrorMessage(e.getMessage());
            return choiceOption(choice);
        }
    }

    private boolean choicePromotion(String productName) {
        return choiceOption(InputView.readPromotionChoice(productName));
    }

    private boolean choiceRegularPrice(String productName, int shortageQuantity) {
        return choiceOption(InputView.readShortageQuantityChoice(productName, shortageQuantity));
    }

    private boolean choiceMembership() {
        return choiceOption(InputView.readMembershipChoice());
    }

    public boolean choiceExtraBuy() {
        return choiceOption(InputView.readExtraBuy());
    }

    private boolean isPromotionActive(Product promotionProduct) {
        return promotionProduct != null &&
                promotionProduct.getPromotion() != null &&
                promotionProduct.getPromotion().isActive();
    }
}