package store.validator;

public enum ValidationMessage {
    INVALID_FILE_READ("파일을 읽는 중에 오류가 발생했습니다.");

    private static final String PREFIX = "[ERROR] ";

    private final String message;

    ValidationMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + message;
    }
}
