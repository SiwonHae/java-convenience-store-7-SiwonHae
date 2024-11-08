package store.common;

public enum StoreDelimiter {
    COMMA(","),
    HYPHEN("-"),
    WHITESPACE(" "),
    BRACKET_LEFT("["),
    BRACKET_RIGHT("]");

    private final String value;

    StoreDelimiter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
