package store.view;

import static store.view.ViewMessage.INPUT_BUY_ITEM;
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

    public static void printReadItem() {
        printMessage();
        printMessage(INPUT_BUY_ITEM.getMessage());
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
