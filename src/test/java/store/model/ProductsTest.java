package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
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

    @DisplayName("프로모션 기간 중이라면 프로모션 상품의 재고를 우선적으로 차감한다.")
    @Test
    public void shouldDecreasePromotionStockFirstWhenPromotionIsActive() {
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
        OrderProduct orderProduct = new OrderProduct("콜라", 12);

        // when
        products.decreaseProductStock(orderProduct.productName(), orderProduct.quantity());

        // then
        assertEquals(0, product1.getQuantity());
        assertEquals(8, product2.getQuantity());
    }

    @DisplayName("프로모션 기간이 아니라면 프로모션이 적용되지 않는 상품의 재고를 우선적으로 차감한다.")
    @Test
    public void testDecreaseQuantity() {
        // given
        Promotion promotion = new Promotion(
                "탄산2+1",
                2,
                1,
                LocalDate.parse("2024-12-01"),
                LocalDate.parse("2024-12-31")
        );
        Product product1 = new Product("콜라", 1000, 10, promotion);
        Product product2 = new Product("콜라", 1000, 10, null);
        Products products = new Products(List.of(product1, product2));
        OrderProduct orderProduct = new OrderProduct("콜라", 12);

        // when
        products.decreaseProductStock(orderProduct.productName(), orderProduct.quantity());

        // then
        assertEquals(8, product1.getQuantity());
        assertEquals(0, product2.getQuantity());
    }
}