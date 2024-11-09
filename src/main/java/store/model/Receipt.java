package store.model;

import java.util.List;

public record Receipt(List<OrderInfo> orderInfo, List<PromotionInfo> promotionInfo, PriceInfo priceInfo) {
    public int getTotalQuantity() {
        return orderInfo.stream()
                .mapToInt(OrderInfo::quantity)
                .sum();
    }

    public int getTotalPromotionPrice() {
        return promotionInfo.stream()
                .mapToInt(PromotionInfo::price)
                .sum() * -1;
    }
}
