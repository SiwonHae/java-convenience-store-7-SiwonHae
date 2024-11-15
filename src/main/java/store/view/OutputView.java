package store.view;

import static store.common.StoreDelimiter.HYPHEN;
import static store.view.ViewMessage.INPUT_BUY_PRODUCT;
import static store.view.ViewMessage.INPUT_EXTRA_BUY;
import static store.view.ViewMessage.INPUT_MEMBERSHIP_CHOICE;
import static store.view.ViewMessage.INPUT_PROMOTION_CHOICE;
import static store.view.ViewMessage.INPUT_SHORTAGE_QUANTITY_CHOICE;
import static store.view.ViewMessage.LOCALE_INTEGER;
import static store.view.ViewMessage.PRINT_WELCOME;
import static store.view.ViewMessage.RECEIPT_DISCOUNT_FORMAT;
import static store.view.ViewMessage.RECEIPT_DIVIDER;
import static store.view.ViewMessage.RECEIPT_INFO_FORMAT;
import static store.view.ViewMessage.RECEIPT_INFO_HEADER_FORMAT;
import static store.view.ViewMessage.RECEIPT_PAY_PRICE_FORMAT;
import static store.view.ViewMessage.RECEIPT_PREFIX;
import static store.view.ViewMessage.RECEIPT_PROMOTION;
import static store.view.ViewMessage.RECEIPT_PROMOTION_FORMAT;

import java.util.List;
import store.model.OrderInfo;
import store.model.Product;
import store.model.PromotionInfo;
import store.model.Receipt;

public class OutputView {
    private static final String NAME = "상품명";
    private static final String QUANTITY = "수량";
    private static final String PRICE = "금액";
    private static final String TOTAL_PRICE = "총구매액";
    private static final String PROMOTION_DISCOUNT = "행사할인";
    private static final String MEMBERSHIP_DISCOUNT = "멤버십할인";
    private static final String PAY_PRICE = "내실돈";
    private static final String EMPTY = "";

    private OutputView() {
    }

    public static void printNewLine() {
        printMessage();
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

    public static void printReadPromotionChoice(String productName) {
        printMessage();
        printMessage(INPUT_PROMOTION_CHOICE.getMessage(), productName);
    }

    public static void printReadShortageQuantityChoice(String productName, int shortageQuantity) {
        printMessage();
        printMessage(INPUT_SHORTAGE_QUANTITY_CHOICE.getMessage(), productName, shortageQuantity);
    }

    public static void printReadMembershipChoice() {
        printMessage();
        printMessage(INPUT_MEMBERSHIP_CHOICE.getMessage());
    }

    public static void printReadExtraBuy() {
        printMessage();
        printMessage(INPUT_EXTRA_BUY.getMessage());
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
        for (OrderInfo orderInfo : receipt.orderInfos()) {
            printMessage(RECEIPT_INFO_FORMAT.getMessage(), orderInfo.productName(), orderInfo.quantity(),
                    orderInfo.price());
        }
    }

    private static void printReceiptPromotionInfo(Receipt receipt) {
        printMessage(RECEIPT_PROMOTION.getMessage());
        for (PromotionInfo promotionInfo : receipt.promotionInfos()) {
            printMessage(RECEIPT_PROMOTION_FORMAT.getMessage(), promotionInfo.productName(),
                    promotionInfo.bonusQuantity());
        }
    }

    private static void printReceiptPriceInfo(Receipt receipt) {
        printMessage(RECEIPT_DIVIDER.getMessage());
        printMessage(RECEIPT_INFO_FORMAT.getMessage(), TOTAL_PRICE,
                receipt.getTotalOrderQuantity(), receipt.priceInfo().getTotalPrice().price());
        printMessage(RECEIPT_DISCOUNT_FORMAT.getMessage(), PROMOTION_DISCOUNT, EMPTY,
                HYPHEN.getValue() + formatInteger(receipt.priceInfo().getPromotionPrice().price()));
        printMessage(RECEIPT_DISCOUNT_FORMAT.getMessage(), MEMBERSHIP_DISCOUNT, EMPTY,
                HYPHEN.getValue() + formatInteger(receipt.priceInfo().getMembershipPrice().price()));
        printMessage(RECEIPT_PAY_PRICE_FORMAT.getMessage(), PAY_PRICE, EMPTY,
                receipt.priceInfo().getPayPrice().price());
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

    private static String formatInteger(Object... args) {
        return String.format(LOCALE_INTEGER.getMessage(), args);
    }
}
