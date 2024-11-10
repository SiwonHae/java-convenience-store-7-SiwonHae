package store.model;

import static store.common.StoreRule.MEMBERSHIP_DISCOUNT_RATE;
import static store.common.StoreRule.MEMBERSHIP_PRICE_LIMIT;
import static store.parser.InputParser.parseDouble;
import static store.parser.InputParser.parseInteger;

import java.util.List;

public record PriceInfo(int totalPrice, int promotionPrice, int membershipPrice, int payPrice) {
    public PriceInfo(List<OrderInfo> orderInfos, List<PromotionInfo> promotionInfos, boolean choiceMembership) {
        this(
                calculateTotalPrice(orderInfos),
                calculatePromotionPrice(promotionInfos),
                calculateMembershipPrice(orderInfos, promotionInfos, choiceMembership),
                calculatePayPrice(orderInfos, promotionInfos, choiceMembership)
        );
    }

    private static int calculateTotalPrice(List<OrderInfo> orderInfos) {
        return orderInfos.stream()
                .mapToInt(OrderInfo::price)
                .sum();
    }

    private static int calculatePromotionPrice(List<PromotionInfo> promotionInfos) {
        return promotionInfos.stream()
                .mapToInt(promotionInfo -> promotionInfo.bonusQuantity() * promotionInfo.price())
                .sum();
    }

    private static int calculateApplyPromotionPrice(List<PromotionInfo> promotionInfos) {
        return promotionInfos.stream()
                .mapToInt(promotionInfo -> promotionInfo.totalQuantity() * promotionInfo.price())
                .sum();
    }

    private static int calculateMembershipPrice(List<OrderInfo> orderInfos, List<PromotionInfo> promotionInfos,
                                                boolean choiceMembership) {
        if (!choiceMembership) {
            return 0;
        }

        int totalPrice = calculateTotalPrice(orderInfos);
        int promotionPrice = calculateApplyPromotionPrice(promotionInfos);
        double membershipPrice = (totalPrice - promotionPrice) * parseDouble(MEMBERSHIP_DISCOUNT_RATE.getValue());

        return (int) Math.min(membershipPrice, parseInteger(MEMBERSHIP_PRICE_LIMIT.getValue()));
    }

    private static int calculatePayPrice(List<OrderInfo> orderInfos, List<PromotionInfo> promotionInfos,
                                         boolean choiceMembership) {
        int totalPrice = calculateTotalPrice(orderInfos);
        int promotionPrice = calculatePromotionPrice(promotionInfos);
        int membershipPrice = calculateMembershipPrice(orderInfos, promotionInfos, choiceMembership);

        return totalPrice - promotionPrice - membershipPrice;
    }
}
