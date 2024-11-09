package store.view;

import static store.view.ViewMessage.INPUT_BUY_PRODUCT;
import static store.view.ViewMessage.PRINT_WELCOME;
import static store.view.ViewMessage.RECEIPT_DIVIDER;
import static store.view.ViewMessage.RECEIPT_INFO_FORMAT;
import static store.view.ViewMessage.RECEIPT_INFO_HEADER_FORMAT;
import static store.view.ViewMessage.RECEIPT_PREFIX;
import static store.view.ViewMessage.RECEIPT_PROMOTION;
import static store.view.ViewMessage.RECEIPT_PROMOTION_FORMAT;

import java.util.List;
import store.model.OrderInfo;
import store.model.Product;
import store.model.PromotionInfo;
import store.model.Receipt;

public class OutputView {
    private static final String EMPTY = "";
    private static final String NAME = "상품명";
    private static final String QUANTITY = "수량";
    private static final String PRICE = "금액";
    private static final String TOTAL_PRICE = "총구매액";
    private static final String PROMOTION_DISCOUNT = "행사할인";

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

    public static void printReceipt(Receipt receipt) {
        printReceiptHeader();
        printReceiptOrderInfo(receipt);
        printReceiptPromotionInfo(receipt);
        printReceiptPriceInfo(receipt);
    }

    private static void printReceiptHeader() {
        printMessage();
        printMessage(RECEIPT_PREFIX.getMessage());
        printMessage(RECEIPT_INFO_HEADER_FORMAT.getMessage(), NAME, QUANTITY, PRICE);
    }

    private static void printReceiptOrderInfo(Receipt receipt) {
        for (OrderInfo orderInfo : receipt.orderInfo()) {
            printMessage(RECEIPT_INFO_FORMAT.getMessage(), orderInfo.productName(), orderInfo.quantity(),
                    orderInfo.price());
        }
    }

    private static void printReceiptPromotionInfo(Receipt receipt) {
        printMessage(RECEIPT_PROMOTION.getMessage());
        for (PromotionInfo promotionInfo : receipt.promotionInfo()) {
            printMessage(RECEIPT_PROMOTION_FORMAT.getMessage(), promotionInfo.productName(), promotionInfo.quantity());
        }
    }

    private static void printReceiptPriceInfo(Receipt receipt) {
        printMessage(RECEIPT_DIVIDER.getMessage());
        printMessage(RECEIPT_INFO_FORMAT.getMessage(), TOTAL_PRICE, receipt.getTotalQuantity(),
                receipt.priceInfo().totalPrice());
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

    private static void printMessage(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
}
