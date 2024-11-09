package store.validator;

import static store.common.StoreRule.NO;
import static store.common.StoreRule.YES;
import static store.validator.ValidationMessage.INVALID_INPUT_FORMAT;

import store.exception.InvalidInputException;

public class PromotionChoiceValidator {
    public void validate(String choice) {
        validateEmpty(choice);
        validateChoice(choice);
    }

    private void validateEmpty(String choice) {
        if (choice == null || choice.trim().isEmpty()) {
            throw new InvalidInputException(INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private void validateChoice(String choice) {
        if (!choice.equalsIgnoreCase(YES.getValue()) && !choice.equalsIgnoreCase(NO.getValue())) {
            throw new InvalidInputException(INVALID_INPUT_FORMAT.getMessage());
        }
    }
}
