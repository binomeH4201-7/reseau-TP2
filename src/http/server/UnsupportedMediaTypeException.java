package http.server;

public class UnsupportedMediaTypeException extends Exception {

    public UnsupportedMediaTypeException() {
        super();
    }

    public UnsupportedMediaTypeException(String s) {
        super(s);
    }
}
