package store.model;

import static store.common.StoreDelimiter.HYPHEN;
import static store.common.StoreDelimiter.WHITESPACE;
import static store.common.StoreRule.NONE_STOCK;
import static store.common.StoreRule.UNIT_COUNT;
import static store.common.StoreRule.UNIT_PRICE;

import java.text.NumberFormat;
import java.util.Locale;

public class Product {
    private static final int NONE = 0;
    private static final String EMPTY = "";

    private final String name;
    private final int price;
    private int quantity;
    private Promotion promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getPrice() {
        return price;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
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
        if (promotion != null && !promotion.getName().equals("null")) {
            return WHITESPACE.getValue() + promotion;
        }
        return EMPTY;
    }
}
