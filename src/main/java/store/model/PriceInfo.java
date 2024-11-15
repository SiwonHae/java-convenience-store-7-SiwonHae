package store.model;

import java.util.List;

public class PriceInfo {
    private final TotalPrice totalPrice;
    private final PromotionPrice promotionPrice;
    private final MembershipPrice membershipPrice;
    private final PayPrice payPrice;

    public PriceInfo(List<OrderInfo> orderInfos, List<PromotionInfo> promotionInfos, boolean choiceMembership) {
        this.totalPrice = new TotalPrice(orderInfos);
        this.promotionPrice = new PromotionPrice(promotionInfos);
        this.membershipPrice = new MembershipPrice(totalPrice,
                promotionPrice.calculateApplyPromotionPrice(promotionInfos), choiceMembership);
        this.payPrice = new PayPrice(totalPrice, promotionPrice, membershipPrice);
    }

    public TotalPrice getTotalPrice() {
        return totalPrice;
    }

    public PromotionPrice getPromotionPrice() {
        return promotionPrice;
    }

    public MembershipPrice getMembershipPrice() {
        return membershipPrice;
    }

    public PayPrice getPayPrice() {
        return payPrice;
    }
}
