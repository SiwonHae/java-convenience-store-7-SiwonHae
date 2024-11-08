package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @DisplayName("입력된 수량만큼 재고가 감소되어야 한다.")
    @Test
    public void testDecreaseQuantity() {
        // given
        Product product = new Product("콜라", 1000, 10, "탄산2+1");
        int quantity = 5;

        // when
        product.decreaseQuantity(quantity);

        // then
        assertEquals(5, product.getQuantity());
    }
}