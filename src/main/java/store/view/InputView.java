package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private InputView() {
    }

    public static String readProduct() {
        OutputView.printReadProduct();
        return Console.readLine();
    }
}
