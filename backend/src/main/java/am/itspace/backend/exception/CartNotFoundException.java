package am.itspace.backend.exception;

public class CartNotFoundException extends RuntimeException{

  public CartNotFoundException(String message) {
    super(message);
  }

}
