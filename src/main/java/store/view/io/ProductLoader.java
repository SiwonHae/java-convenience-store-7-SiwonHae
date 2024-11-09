package store.view.io;

import static store.common.StoreDelimiter.COMMA;
import static store.parser.InputParser.parseInteger;
import static store.validator.ValidationMessage.INVALID_FILE_READ;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.exception.FileException;
import store.model.Product;
import store.model.Promotion;

public class ProductLoader {
    private static final String PATH_PRODUCT = "src/main/resources/products.md";
    private static final String HEADER_PREFIX = "name";
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;
    private static final int DEFAULT_QUANTITY = 0;

    public static List<Product> loadProducts() throws FileException {
        Map<String, List<Product>> productMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_PRODUCT))) {
            br.lines()
                    .filter(line -> !FileLoaderUtil.isHeader(line, HEADER_PREFIX))
                    .forEach(line -> addProductToMap(productMap, parseProduct(line)));
        } catch (IOException | NumberFormatException e) {
            throw new FileException(INVALID_FILE_READ.getMessage());
        }
        addNonPromotionProduct(productMap);
        return convertMapToList(productMap);
    }

    private static void addProductToMap(Map<String, List<Product>> productMap, Product product) {
        productMap.computeIfAbsent(product.getName(), k -> new ArrayList<>()).add(product);
    }

    private static void addNonPromotionProduct(Map<String, List<Product>> productMap) {
        productMap.entrySet().stream()
                .filter(entry -> entry.getValue().stream().noneMatch(product -> product.getPromotion() == null))
                .forEach(entry -> entry.getValue().add(
                        new Product(entry.getKey(), entry.getValue().getFirst().getPrice(), DEFAULT_QUANTITY, null)
                ));
    }

    private static List<Product> convertMapToList(Map<String, List<Product>> productMap) {
        return productMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static Product parseProduct(String line) {
        String[] tokens = line.split(COMMA.getValue());
        String name = tokens[NAME_INDEX];
        int price = parseInteger(tokens[PRICE_INDEX]);
        int quantity = parseInteger(tokens[QUANTITY_INDEX]);
        String promotionName = tokens[PROMOTION_INDEX];
        Promotion promotion = null;
        if (!promotionName.equals("null")) {
            promotion = new Promotion(promotionName);
            return new Product(name, price, quantity, promotion);
        }
        return new Product(name, price, quantity, promotion);
    }
}
