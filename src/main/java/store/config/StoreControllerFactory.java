package store.config;

import store.controller.StoreController;
import store.service.OrderService;
import store.service.StoreService;
import store.validator.ChoiceValidator;
import store.validator.OrderProductValidator;
import store.validator.OrderValidator;

public class StoreControllerFactory {
    public static StoreController create() {
        return new StoreController(
                new OrderProductValidator(),
                new StoreService(new OrderService(new OrderValidator(), new ChoiceValidator()))
        );
    }
}
