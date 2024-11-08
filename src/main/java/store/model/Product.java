package store.model;

import static store.common.StoreDelimiter.HYPHEN;
import static store.common.StoreDelimiter.WHITESPACE;
import static store.common.StoreRule.NONE_STOCK;
import static store.common.StoreRule.UNIT_COUNT;
import static store.common.StoreRule.UNIT_PRICE;
import static store.validator.ValidationMessage.EXCEED_STOCK_QUANTITY;

import java.text.NumberFormat;
import java.util.Locale;
import store.exception.InvalidInputException;

public class Product {
    private static final String NULL = "null";
    private static final int NONE = 0;
    private static final String EMPTY = "";

    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public void validateStock(int quantity) {
        if (this.quantity < quantity) {
            throw new InvalidInputException(EXCEED_STOCK_QUANTITY.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return HYPHEN.getValue() + WHITESPACE.getValue()
                + name + WHITESPACE.getValue()
                + getFormattedPrice() + WHITESPACE.getValue()
                + getQuantityInfo()
                + getPromotionInfo();
    }

    private String getFormattedPrice() {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
        return formatter.format(price) + UNIT_PRICE.getValue();
    }

    private String getQuantityInfo() {
        if (quantity == NONE) {
            return NONE_STOCK.getValue();
        }
        return quantity + UNIT_COUNT.getValue();
    }

    private String getPromotionInfo() {
        if (promotion != null && !promotion.equals(NULL)) {
            return WHITESPACE.getValue() + promotion;
        }
        return EMPTY;
    }
}
