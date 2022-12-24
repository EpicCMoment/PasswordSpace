package kozmikoda.passwordspace;

public class InvalidServiceException extends RuntimeException{
    InvalidServiceException(String service) {
        super(service + " does not exist!");
    }
}
