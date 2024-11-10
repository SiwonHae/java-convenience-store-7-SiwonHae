package store;

import store.config.StoreControllerFactory;
import store.controller.StoreController;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = StoreControllerFactory.create();
        storeController.run();
    }
}
