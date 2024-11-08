package store.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.OrderProduct;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;

@DisplayName("OrderValidator 검증 테스트")
class OrderValidatorTest {
    private final OrderValidator validator = new OrderValidator();

    @DisplayName("구매 수량이 재고 수량을 초과한 경우 예와가 발생한다.")
    @Test
    void throwExceptionWhenQuantityExceedStock() {
        // given
        Promotion promotion = new Promotion(
                "탄산2+1",
                2,
                1,
                LocalDate.parse("2024-11-01"),
                LocalDate.parse("2024-12-31")
        );
        Product product1 = new Product("콜라", 1000, 10, promotion);
        Product product2 = new Product("콜라", 1000, 10, null);
        Products products = new Products(List.of(product1, product2));
        List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 30));

        // when
        Product promotionProduct = products.findProductWithPromotion(orderProducts.get(0).productName());
        Product nonPromotionProduct = products.findProductWithoutPromotion(orderProducts.get(0).productName());
        int quantity = orderProducts.get(0).quantity();

        // then
        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(promotionProduct, nonPromotionProduct, quantity));
    }
}