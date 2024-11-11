package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import camp.nextstep.edu.missionutils.Console;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.OrderProduct;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.validator.ChoiceValidator;
import store.validator.OrderValidator;

@DisplayName("OrderService 기능 테스트")
class OrderServiceTest {
    private final OrderService orderService = new OrderService(new OrderValidator(), new ChoiceValidator());

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
    }

    @AfterEach
    void cleanup() {
        System.setIn(System.in);
        Console.close();
    }

    private void setupInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @DisplayName("프로모션 기간 중이라면 프로모션 상품의 재고를 우선적으로 차감한다.")
    @Test
    public void shouldDecreasePromotionStockFirstWhenPromotionIsActive() {
        // given
        Product product1 = new Product("콜라", 1000, 10, activePromotion);
        Product product2 = new Product("콜라", 1000, 10, null);
        Products products = new Products(List.of(product1, product2));
        List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 12));

        setupInput("Y\nY\n");

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
        Product product1 = new Product("콜라", 1000, 10, inActivePromotion);
        Product product2 = new Product("콜라", 1000, 10, null);
        Products products = new Products(List.of(product1, product2));
        List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 12));

        setupInput("Y\nY\n");

        // when
        orderService.order(orderProducts, products);

        // then
        assertEquals(8, product1.getQuantity());
        assertEquals(0, product2.getQuantity());
    }

    @DisplayName("프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우에"
            + " Y를 입력하여 증정 상품을 추가한 경우")
    @Test
    public void testPromotionChoiceIsYes() {
        // given
        Promotion activePromotion = new Promotion(
                "MD추천상품",
                1,
                1,
                LocalDate.parse("2024-11-01"),
                LocalDate.parse("2024-12-31")
        );
        Product product1 = new Product("오렌지주스", 1800, 9, activePromotion);
        Product product2 = new Product("오렌지주스", 1800, 0, null);
        Products products = new Products(List.of(product1, product2));
        List<OrderProduct> orderProducts = List.of(new OrderProduct("오렌지주스", 1));

        setupInput("Y\nY\n");

        // when
        orderService.order(orderProducts, products);

        // then
        assertEquals(7, product1.getQuantity());
        assertEquals(0, product2.getQuantity());
    }

    @DisplayName("프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우에"
            + " N을 입력하여 증정 상품을 추가하지 않은 경우")
    @Test
    public void testPromotionChoiceIsNo() {
        // given
        Promotion activePromotion = new Promotion(
                "MD추천상품",
                1,
                1,
                LocalDate.parse("2024-11-01"),
                LocalDate.parse("2024-12-31")
        );
        Product product1 = new Product("오렌지주스", 1800, 9, activePromotion);
        Product product2 = new Product("오렌지주스", 1800, 0, null);
        Products products = new Products(List.of(product1, product2));
        List<OrderProduct> orderProducts = List.of(new OrderProduct("오렌지주스", 1));

        setupInput("N\nY\n");

        // when
        orderService.order(orderProducts, products);

        // then
        assertEquals(8, product1.getQuantity());
        assertEquals(0, product2.getQuantity());
    }

    @DisplayName("프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우에 "
            + "Y를 입력하여 일부 수량에 대해 정가로 결제한 경우")
    @Test
    public void testShortageChoiceIsYes() {
        // given
        Product product1 = new Product("콜라", 1000, 7, activePromotion);
        Product product2 = new Product("콜라", 1000, 10, null);
        Products products = new Products(List.of(product1, product2));
        List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 10));

        setupInput("Y\nY\n");

        // when
        orderService.order(orderProducts, products);

        // then
        assertEquals(0, product1.getQuantity());
        assertEquals(7, product2.getQuantity());
    }

    @DisplayName("프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우에 "
            + "N을 입력하여 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한 경우")
    @Test
    public void testShortageChoiceIsNo() {
        // given
        Product product1 = new Product("콜라", 1000, 7, activePromotion);
        Product product2 = new Product("콜라", 1000, 10, null);
        Products products = new Products(List.of(product1, product2));
        List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 10));

        setupInput("N\nY\n");

        // when
        orderService.order(orderProducts, products);

        // then
        assertEquals(1, product1.getQuantity());
        assertEquals(10, product2.getQuantity());
    }
}