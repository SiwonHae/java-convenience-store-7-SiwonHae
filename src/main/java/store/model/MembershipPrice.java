package store.model;

import static store.common.StoreRule.MEMBERSHIP_DISCOUNT_RATE;
import static store.common.StoreRule.MEMBERSHIP_PRICE_LIMIT;
import static store.parser.InputParser.parseDouble;
import static store.parser.InputParser.parseInteger;

public record MembershipPrice(int price) {
    public MembershipPrice(TotalPrice totalPrice, int applyPromotionPrice, boolean choiceMembership) {
        this(calculateMembershipPrice(totalPrice, applyPromotionPrice, choiceMembership));
    }

    private static int calculateMembershipPrice(TotalPrice totalPrice,
                                                int applyPromotionPrice,
                                                boolean choiceMembership) {
        if (!choiceMembership) {
            return 0;
        }
        double membershipPrice =
                (totalPrice.price() - applyPromotionPrice) * parseDouble(MEMBERSHIP_DISCOUNT_RATE.getValue());
        return (int) Math.min(membershipPrice, parseInteger(MEMBERSHIP_PRICE_LIMIT.getValue()));
    }
}
