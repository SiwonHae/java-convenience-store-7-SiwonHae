package store.model;

import java.util.List;

public record Receipt(List<OrderInfo> orderInfo, List<PromotionInfo> promotionInfo, PriceInfo priceInfo) {
    public int getTotalQuantity() {
        return orderInfo.stream()
                .mapToInt(OrderInfo::quantity)
                .sum();
    }
}
