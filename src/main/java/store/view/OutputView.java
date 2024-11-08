package store.view;

import static store.view.ViewMessage.INPUT_BUY_PRODUCT;
import static store.view.ViewMessage.PRINT_WELCOME;

import java.util.List;
import store.model.Product;

public class OutputView {
    private OutputView() {
    }

    public static void printWelcome() {
        printMessage(PRINT_WELCOME.getMessage());
    }

    public static void printProductList(List<Product> products) {
        printMessage();
        for (Product product : products) {
            printMessage(product.toString());
        }
    }

    public static void printReadProduct() {
        printMessage();
        printMessage(INPUT_BUY_PRODUCT.getMessage());
    }

    public static void printErrorMessage(String message) {
        printMessage(message);
    }

    private static void printMessage() {
        System.out.println();
    }

    private static void printMessage(String message) {
        System.out.println(message);
    }
}
