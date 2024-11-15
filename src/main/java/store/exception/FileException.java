package store.exception;

import java.io.IOException;

public class FileException extends IOException {
    public FileException(String message) {
        super(message);
    }
}
