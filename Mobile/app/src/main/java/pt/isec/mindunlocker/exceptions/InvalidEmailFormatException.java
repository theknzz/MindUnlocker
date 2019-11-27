package pt.isec.mindunlocker.exceptions;

public class InvalidEmailFormatException extends Exception {
    public InvalidEmailFormatException() {
        super("Invalid email format!\nExample: (example@mindUnlocker.com)");
    }
}
