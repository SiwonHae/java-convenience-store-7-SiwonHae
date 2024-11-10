package store.controller;

import static store.parser.InputParser.parseOrderProducts;

import store.model.Products;
import store.model.Receipt;
import store.service.StoreService;
import store.validator.OrderProductValidator;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final OrderProductValidator orderProductValidator;
    private final StoreService storeService;

    public StoreController(OrderProductValidator orderProductValidator, StoreService storeService) {
        this.orderProductValidator = orderProductValidator;
        this.storeService = storeService;
    }

    public void run() {
        Products products = initializeStore();
        if (products == null) {
            return;
        }
        do {
            OutputView.printWelcome();
            OutputView.printProductList(products.products());
            Receipt receipt = order(products);
            OutputView.printReceipt(receipt);
        } while (storeService.isExtraBuy());
    }

    private Products initializeStore() {
        return storeService.initializeStore();
    }

    private Receipt order(Products products) {
        while (true) {
            try {
                String inputItem = InputView.readProduct();
                orderProductValidator.validate(inputItem);
                return storeService.order(parseOrderProducts(inputItem), products);
            } catch (IllegalArgumentException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
    }
}
