package store.view.io;

public class FileLoaderUtil {
    private FileLoaderUtil() {
    }

    public static boolean isHeader(String line, String headerPrefix) {
        return line.startsWith(headerPrefix);
    }
}
