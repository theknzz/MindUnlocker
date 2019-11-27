package pt.isec.mindunlocker.exceptions;

public class PasswordDoesntMatchException extends Exception {
    public PasswordDoesntMatchException() {
        super("Password and password confirmation must match!");
    }
}
