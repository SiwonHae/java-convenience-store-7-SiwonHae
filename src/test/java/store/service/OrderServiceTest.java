package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.OrderProduct;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.validator.OrderValidator;
import store.validator.PromotionChoiceValidator;

@DisplayName("OrderService 기능 테스트")
class OrderServiceTest {
    private final OrderService orderService = new OrderService(new OrderValidator(), new PromotionChoiceValidator());

    private Products products;
    private List<OrderProduct> orderProducts;
    private Product product1;
    private Product product2;
    private Promotion activePromotion;
    private Promotion inActivePromotion;

    @BeforeEach
    void setup() {
        activePromotion = new Promotion(
                "탄산2+1",
                2,
                1,
                LocalDate.parse("2024-11-01"),
                LocalDate.parse("2024-12-31")
        );
        inActivePromotion = new Promotion(
                "탄산2+1",
                2,
                1,
                LocalDate.parse("2024-12-01"),
                LocalDate.parse("2024-12-31")
        );
        product2 = new Product("콜라", 1000, 10, null);
        orderProducts = List.of(new OrderProduct("콜라", 12));
    }

    @DisplayName("프로모션 기간 중이라면 프로모션 상품의 재고를 우선적으로 차감한다.")
    @Test
    public void shouldDecreasePromotionStockFirstWhenPromotionIsActive() {
        // given
        product1 = new Product("콜라", 1000, 10, activePromotion);
        products = new Products(List.of(product1, product2));

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
        product1 = new Product("콜라", 1000, 10, inActivePromotion);
        products = new Products(List.of(product1, product2));

        // when
        orderService.order(orderProducts, products);

        // then
        assertEquals(8, product1.getQuantity());
        assertEquals(0, product2.getQuantity());
    }
}