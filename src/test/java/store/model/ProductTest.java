package store.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Product 모델 테스트")
class ProductTest {
    @DisplayName("현재 상품의 재고보다 많은 수량을 구입하려고 하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenQuantityExceedStock() {
        // given
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        int quantity = 15;

        // when & then
        assertThrows(IllegalArgumentException.class, () -> product.validateStock(quantity));
    }
}