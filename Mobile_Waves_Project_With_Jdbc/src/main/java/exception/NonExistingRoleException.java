package exception;

public class NonExistingRoleException extends Exception{
    public NonExistingRoleException() {
    }

    public NonExistingRoleException(String message) {
        super(message);
    }

    public NonExistingRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistingRoleException(Throwable cause) {
        super(cause);
    }
}
