package store.parser;

import java.time.LocalDate;

public class InputParser {
    public static int parseInteger(String input) {
        return Integer.parseInt(input);
    }

    public static LocalDate parseLocalDate(String input) {
        return LocalDate.parse(input);
    }
}
