package store.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Products 모델 테스트")
class ProductsTest {
    @DisplayName("존재하지 않는 상품을 구매하려고 하면 예외를 발생한다.")
    @Test
    void throwExceptionWhenProductNameIsInvalid() {
        // given
        List<Product> productList = List.of(
                new Product("콜라", 1000, 10, "탄산2+1")
        );
        Products products = new Products(productList);
        String name = "펩시콜라";

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> products.validateProductName(name));
    }
}