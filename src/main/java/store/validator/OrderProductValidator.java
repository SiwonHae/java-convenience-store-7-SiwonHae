package store.validator;

import static store.validator.ValidationMessage.INVALID_INPUT_FORMAT;

import java.util.regex.Pattern;
import store.exception.InvalidInputException;

public class OrderProductValidator {
    private static final String ORDER_FORMAT_REGEX = "\\[([^-\\]]+)-(\\d+)]\\s*(?:,\\s*\\[([^-\\]]+)-(\\d+)])*";

    public void validate(String inputOrder) {
        validateEmpty(inputOrder);
        validateOrderFormat(inputOrder);
    }

    private void validateEmpty(String inputOrder) {
        if (inputOrder == null || inputOrder.trim().isEmpty()) {
            throw new InvalidInputException(INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private static void validateOrderFormat(String inputOrder) {
        if (!isValidFormat(inputOrder)) {
            throw new InvalidInputException(INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private static boolean isValidFormat(String inputOrder) {
        return Pattern.matches(ORDER_FORMAT_REGEX, inputOrder);
    }
}
