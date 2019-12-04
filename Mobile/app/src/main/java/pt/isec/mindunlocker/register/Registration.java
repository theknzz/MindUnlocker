package pt.isec.mindunlocker.register;

import java.io.Serializable;

/**
 * Class that wraps the request body of the registration
 */
public class Registration implements Serializable {
    protected String Email;
    protected String Password;
    protected String ConfirmPassword;
    protected String Name;

    public Registration(String email, String password, String confirmPassword, String name) {
        this.Email = email;
        this.Password = password;
        this.ConfirmPassword = confirmPassword;
        this.Name = name;
    }
}
