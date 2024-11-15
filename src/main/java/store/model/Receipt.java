package store.model;

import java.util.List;

public record Receipt(List<OrderInfo> orderInfos, List<PromotionInfo> promotionInfos, PriceInfo priceInfo) {
    public int getTotalOrderQuantity() {
        return orderInfos.stream()
                .mapToInt(OrderInfo::quantity)
                .sum();
    }
}
