package store.view;

public enum ViewMessage {
    PRINT_WELCOME("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다."),
    INPUT_BUY_PRODUCT("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    INPUT_PROMOTION_CHOICE("현재 %s(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    INPUT_SHORTAGE_STOCK_CHOICE("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    RECEIPT_PREFIX("==============W 편의점================"),
    RECEIPT_INFO_HEADER_FORMAT("%-15s%-6s%10s"),
    RECEIPT_INFO_FORMAT("%-15s%-6d%,10d"),
    RECEIPT_PROMOTION("=============증\t정==============="),
    RECEIPT_PROMOTION_FORMAT("%-15s%-6d"),
    RECEIPT_DIVIDER("===================================="),
    RECEIPT_PRICE_FORMAT("%-15s%,16d");

    private final String message;

    ViewMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
