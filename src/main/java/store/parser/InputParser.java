package store.parser;

import static store.common.StoreDelimiter.BRACKET_LEFT;
import static store.common.StoreDelimiter.BRACKET_RIGHT;
import static store.common.StoreDelimiter.COMMA;
import static store.common.StoreDelimiter.HYPHEN;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.model.OrderProduct;
import store.model.Products;

public class InputParser {
    private static final String EMPTY = "";
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int QUANTITY_INDEX = 1;

    public static int parseInteger(String input) {
        return Integer.parseInt(input);
    }

    public static LocalDate parseLocalDate(String input) {
        return LocalDate.parse(input);
    }

    public static Double parseDouble(String input) {
        return Double.parseDouble(input);
    }

    public static List<OrderProduct> parseOrderProducts(String input, Products products) {
        String[] productStrings = splitOrderProducts(input);
        return createOrderProducts(productStrings, products);
    }

    private static String[] splitOrderProducts(String input) {
        return input.replace(BRACKET_LEFT.getValue(), EMPTY)
                .replace(BRACKET_RIGHT.getValue(), EMPTY)
                .split(COMMA.getValue());
    }

    private static List<OrderProduct> createOrderProducts(String[] productsInfo, Products products) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (String productInfo : productsInfo) {
            orderProducts.add(createOrderProduct(productInfo, products));
        }
        return orderProducts;
    }

    private static OrderProduct createOrderProduct(String productInfo, Products products) {
        String[] product = productInfo.split(HYPHEN.getValue());
        String productName = product[PRODUCT_NAME_INDEX].trim();
        int quantity = Integer.parseInt(product[QUANTITY_INDEX].trim());
        return new OrderProduct(productName, quantity, products);
    }
}
