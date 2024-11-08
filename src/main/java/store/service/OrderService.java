package store.service;

import java.util.List;
import store.model.OrderProduct;
import store.model.Products;

public class OrderService {
    public void order(List<OrderProduct> orderProducts, Products products) {
        for (OrderProduct orderProduct : orderProducts) {
            products.decreaseProductStock(orderProduct.productName(), orderProduct.quantity());
        }
    }
}
