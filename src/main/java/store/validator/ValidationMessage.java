package store.validator;

public enum ValidationMessage {
    INVALID_FILE_READ("파일을 읽는 중에 오류가 발생했습니다."),
    INVALID_INPUT_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");

    private static final String PREFIX = "[ERROR] ";

    private final String message;

    ValidationMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + message;
    }
}
