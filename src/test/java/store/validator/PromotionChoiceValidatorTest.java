package store.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("PromotionChoiceValidator 검증 테스트")
class PromotionChoiceValidatorTest {
    private final PromotionChoiceValidator validator = new PromotionChoiceValidator();

    @DisplayName("잘못된 형식의 상품 구입에 대한 예외 처리")
    @ValueSource(strings = {"", "     ", "love", "123"})
    @ParameterizedTest
    void throwExceptionWhenInputIsInvalidFormat(String input) {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(input));
    }
}