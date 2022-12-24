package kozmikoda.passwordspace;

public class IncorrectPasswordException extends RuntimeException{
    IncorrectPasswordException() {
        super("Password is incorrect.");
    }
}
