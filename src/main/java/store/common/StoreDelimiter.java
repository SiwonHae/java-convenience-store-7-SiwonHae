package store.common;

public enum StoreDelimiter {
    COMMA(","),
    HYPHEN("-"),
    WHITESPACE(" ");

    private final String value;

    StoreDelimiter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
