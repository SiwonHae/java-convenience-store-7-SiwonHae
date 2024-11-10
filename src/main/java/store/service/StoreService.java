package store.service;

import java.io.IOException;
import java.util.List;
import store.model.OrderProduct;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.model.Receipt;
import store.validator.OrderValidator;
import store.validator.PromotionChoiceValidator;
import store.view.OutputView;
import store.view.io.ProductLoader;
import store.view.io.PromotionLoader;

public class StoreService {
    private final OrderService orderService;

    public StoreService(OrderService orderService) {
        this.orderService = new OrderService(new OrderValidator(), new PromotionChoiceValidator());
    }

    public Receipt order(List<OrderProduct> orderProducts, Products products) {
        return orderService.order(orderProducts, products);
    }

    public boolean isExtraBuy() {
        return orderService.choiceExtraBuy();
    }

    public Products initializeStore() {
        try {
            List<Product> loadProducts = ProductLoader.loadProducts();
            List<Promotion> loadPromotions = PromotionLoader.loadPromotions();

            initializePromotion(loadProducts, loadPromotions);

            return new Products(loadProducts);
        } catch (IOException | NumberFormatException e) {
            OutputView.printErrorMessage(e.getMessage());
            return null;
        }
    }

    private void initializePromotion(List<Product> products, List<Promotion> promotions) {
        products.forEach(product ->
                promotions.stream()
                        .filter(promotion -> product.getPromotion() != null
                                && product.getPromotion().getName().equals(promotion.getName()))
                        .findFirst()
                        .ifPresent(product::setPromotion)
        );
    }
}
