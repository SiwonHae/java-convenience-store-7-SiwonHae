package store.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OrderProduct 모델 테스트")
class OrderProductTest {
    @DisplayName("구매할 상품의 수량이 1개 미만이면 예외를 발생시킨다.")
    @Test
    void throwExceptionWhenQuantityIsLessThanOne() {
        // given
        String name = "콜라";
        int quantity = 0;

        // when & then
        assertThatThrownBy(() -> new OrderProduct(name, quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }
}