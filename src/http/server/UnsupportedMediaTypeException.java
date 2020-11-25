package http.server;

/**
 * Exception to notify an unsupported media type mentioned or asked in a request.
 *
 * @author BUONOMO Phanie, BATEL Arthur
 */
public class UnsupportedMediaTypeException extends Exception {

    public UnsupportedMediaTypeException() {
        super();
    }

    public UnsupportedMediaTypeException(String s) {
        super(s);
    }
}
