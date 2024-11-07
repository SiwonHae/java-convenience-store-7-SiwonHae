package store.view.io;

import static store.common.StoreDelimiter.COMMA;
import static store.parser.InputParser.parseInteger;
import static store.validator.ValidationMessage.INVALID_FILE_READ;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import store.exception.FileException;
import store.model.Product;

public class ProductLoader {
    private static final String PATH_PRODUCT = "src/main/resources/products.md";
    private static final String HEADER_PREFIX = "name";
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;

    public static List<Product> loadProducts() throws FileException {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_PRODUCT))) {
            return br.lines()
                    .filter(line -> !FileLoaderUtil.isHeader(line, HEADER_PREFIX))
                    .map(ProductLoader::parseProduct)
                    .collect(Collectors.toList());
        } catch (IOException | NumberFormatException e) {
            throw new FileException(INVALID_FILE_READ.getMessage());
        }
    }

    private static Product parseProduct(String line) {
        String[] tokens = line.split(COMMA.getValue());

        String name = tokens[NAME_INDEX];
        int price = parseInteger(tokens[PRICE_INDEX]);
        int quantity = parseInteger(tokens[QUANTITY_INDEX]);
        String promotion = null;
        if (tokens.length > PROMOTION_INDEX) {
            promotion = tokens[PROMOTION_INDEX];
        }
        return new Product(name, price, quantity, promotion);
    }
}
