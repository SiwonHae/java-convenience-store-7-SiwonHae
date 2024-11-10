package store.common;

public enum StoreRule {
    UNIT_PRICE("원"),
    UNIT_COUNT("개"),
    NONE_STOCK("재고 없음"),
    YES("Y"),
    NO("N"),
    MEMBERSHIP_PRICE_LIMIT("8000"),
    MEMBERSHIP_DISCOUNT_RATE("0.3");

    private final String value;

    StoreRule(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
