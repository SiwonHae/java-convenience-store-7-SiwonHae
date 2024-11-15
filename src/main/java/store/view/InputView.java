package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private InputView() {
    }

    public static String readProduct() {
        OutputView.printReadProduct();
        return Console.readLine();
    }

    public static String readPromotionChoice(String productName) {
        OutputView.printReadPromotionChoice(productName);
        return Console.readLine();
    }

    public static String readShortageQuantityChoice(String productName, int shortageQuantity) {
        OutputView.printReadShortageQuantityChoice(productName, shortageQuantity);
        return Console.readLine();
    }

    public static String readMembershipChoice() {
        OutputView.printReadMembershipChoice();
        return Console.readLine();
    }

    public static String readExtraBuy() {
        OutputView.printReadExtraBuy();
        return Console.readLine();
    }
}
