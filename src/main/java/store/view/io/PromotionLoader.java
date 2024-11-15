package store.view.io;

import static store.common.StoreDelimiter.COMMA;
import static store.parser.InputParser.parseInteger;
import static store.parser.InputParser.parseLocalDate;
import static store.validator.ValidationMessage.INVALID_FILE_READ;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import store.exception.FileException;
import store.model.Promotion;

public class PromotionLoader {
    private static final String PATH_PROMOTION = "src/main/resources/promotions.md";
    private static final String HEADER_PREFIX = "name";
    private static final int NAME_INDEX = 0;
    private static final int BUY_INDEX = 1;
    private static final int GET_INDEX = 2;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;

    public static List<Promotion> loadPromotions() throws FileException {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_PROMOTION))) {
            return br.lines()
                    .filter(line -> !FileLoaderUtil.isHeader(line, HEADER_PREFIX))
                    .map(PromotionLoader::parsePromotion)
                    .collect(Collectors.toList());
        } catch (IOException | NumberFormatException e) {
            throw new FileException(INVALID_FILE_READ.getMessage());
        }
    }

    private static Promotion parsePromotion(String line) {
        String[] tokens = line.split(COMMA.getValue());

        String name = tokens[NAME_INDEX];
        int buy = parseInteger(tokens[BUY_INDEX]);
        int get = parseInteger(tokens[GET_INDEX]);
        LocalDate startDate = parseLocalDate(tokens[START_DATE_INDEX]);
        LocalDate endDate = parseLocalDate(tokens[END_DATE_INDEX]);
        return new Promotion(name, buy, get, startDate, endDate);
    }
}
