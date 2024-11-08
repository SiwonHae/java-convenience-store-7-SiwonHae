package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Products 모델 테스트")
class ProductsTest {
    @DisplayName("구매할 상품과 수량이 입력되면 해당 상품의 재고에서 차감한다.")
    @Test
    public void testDecreaseProductStock() {
        // given
        Products products = new Products(List.of(
                new Product("콜라", 1000, 10, null)
        ));
        OrderProduct orderProduct = new OrderProduct("콜라", 3);

        // when
        products.decreaseProductStock(orderProduct.productName(), orderProduct.quantity());

        // then
        assertEquals(7, products.products().get(0).getQuantity());
    }
}