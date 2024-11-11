package store.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ChoiceValidator 검증 테스트")
class ChoiceValidatorTest {
    private final ChoiceValidator validator = new ChoiceValidator();

    @DisplayName("잘못된 형식의 여부 입력에 대한 예외 처리")
    @ValueSource(strings = {"", "     ", "love", "123", "y", "n"})
    @ParameterizedTest
    void throwExceptionWhenInputIsInvalidFormat(String input) {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(input));
    }
}