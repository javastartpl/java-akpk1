package pl.javastart.library.exception;

public class PublicationAlreadyExistsException extends RuntimeException {
    public PublicationAlreadyExistsException(String message) {
        super(message);
    }
}