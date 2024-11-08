package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.OrderProduct;
import store.model.Product;
import store.model.Products;

@DisplayName("OrderService 기능 테스트")
class OrderServiceTest {
    private final OrderService orderService = new OrderService();

    @DisplayName("상품 주문 시 상품의 재고가 차감되어야 한다.")
    @Test
    public void testOrder() {
        // given
        Products products = new Products(List.of(
                new Product("콜라", 1000, 10, null),
                new Product("에너지바", 2000, 5, null))
        );
        OrderProduct orderProduct1 = new OrderProduct("콜라", 3);
        OrderProduct orderProduct2 = new OrderProduct("에너지바", 5);
        List<OrderProduct> orderProducts = List.of(orderProduct1, orderProduct2);

        // when
        orderService.order(orderProducts, products);

        // then
        assertEquals(7, products.products().get(0).getQuantity());
        assertEquals(0, products.products().get(1).getQuantity());
    }
}