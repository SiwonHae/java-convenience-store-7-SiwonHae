package store.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("OrderProductValidator 검증 테스트")
class OrderProductValidatorTest {
    private final OrderProductValidator validator = new OrderProductValidator();

    @DisplayName("잘못된 형식의 상품 구입에 대한 예외 처리")
    @ValueSource(strings = {"", "     ", "[콜라-3", "콜라-3]", "[콜라 3]",
            "[콜라-3],[에너지바-]", "[콜라-3]a,a[에너지바-3]", "콜라-3,에너지바-3"})
    @ParameterizedTest
    void throwExceptionWhenInputIsInvalidFormat(String input) {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(input));
    }
}