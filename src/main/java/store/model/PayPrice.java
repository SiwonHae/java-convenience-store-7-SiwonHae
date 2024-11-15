package store.model;

public record PayPrice(int price) {
    public PayPrice(TotalPrice totalPrice, PromotionPrice promotionPrice, MembershipPrice membershipPrice) {
        this(calculatePayPrice(totalPrice, promotionPrice, membershipPrice));
    }

    private static int calculatePayPrice(TotalPrice totalPrice, PromotionPrice promotionPrice,
                                         MembershipPrice membershipPrice) {
        return totalPrice.price() - promotionPrice.price() - membershipPrice.price();
    }
}
