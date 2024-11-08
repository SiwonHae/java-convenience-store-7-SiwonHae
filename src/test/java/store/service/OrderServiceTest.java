package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.OrderProduct;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.validator.OrderValidator;

@DisplayName("OrderService 기능 테스트")
class OrderServiceTest {
    private final OrderService orderService = new OrderService(new OrderValidator());

    @DisplayName("구매할 상품과 수량이 입력되면 해당 상품의 재고에서 차감한다.")
    @Test
    public void testDecreaseProductStock() {
        // given
        Products products = new Products(List.of(
                new Product("콜라", 1000, 10, null)
        ));
        List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 7));

        // when
        orderService.order(orderProducts, products);

        // then
        assertEquals(3, products.products().get(0).getQuantity());
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
        List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 12));

        // when
        orderService.order(orderProducts, products);

        // then
        assertEquals(0, product1.getQuantity());
        assertEquals(8, product2.getQuantity());
    }

    @DisplayName("프로모션 기간이 아니라면 프로모션이 적용되지 않는 상품의 재고를 우선적으로 차감한다.")
    @Test
    public void shouldDecreaseNonPromotionStockFirstWhenPromotionIsInActive() {
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
        List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 12));

        // when
        orderService.order(orderProducts, products);

        // then
        assertEquals(8, product1.getQuantity());
        assertEquals(0, product2.getQuantity());
    }
}