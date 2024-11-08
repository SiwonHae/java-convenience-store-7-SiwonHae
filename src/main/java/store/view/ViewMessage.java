package store.view;

public enum ViewMessage {
    PRINT_WELCOME("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다."),
    INPUT_BUY_PRODUCT("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");

    private final String message;

    ViewMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
