package store.model;

import java.util.List;

public record PromotionPrice(int price) {
    public PromotionPrice(List<PromotionInfo> promotionInfos) {
        this(calculatePromotionPrice(promotionInfos));
    }

    private static int calculatePromotionPrice(List<PromotionInfo> promotionInfos) {
        return promotionInfos.stream()
                .mapToInt(promotionInfo -> promotionInfo.bonusQuantity() * promotionInfo.price())
                .sum();
    }

    public int calculateApplyPromotionPrice(List<PromotionInfo> promotionInfos) {
        return promotionInfos.stream()
                .mapToInt(promotionInfo -> promotionInfo.totalQuantity() * promotionInfo.price())
                .sum();
    }
}
