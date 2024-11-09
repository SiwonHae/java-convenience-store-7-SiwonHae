package store.model;

import java.util.List;

public record PriceInfo(int totalPrice, int promotionPrice) {
    public PriceInfo(List<OrderInfo> orderInfos, List<PromotionInfo> promotionInfos) {
        this(
                calculateTotalPrice(orderInfos),
                calculatePromotionPrice(promotionInfos)
        );
    }

    private static int calculateTotalPrice(List<OrderInfo> orderInfos) {
        return orderInfos.stream()
                .mapToInt(OrderInfo::price)
                .sum();
    }

    private static int calculatePromotionPrice(List<PromotionInfo> promotionInfos) {
        return promotionInfos.stream()
                .mapToInt(promotionInfo -> promotionInfo.quantity() * promotionInfo.price())
                .sum();
    }
}
