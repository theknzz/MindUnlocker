package pt.isec.mindunlocker.api.register;

import java.io.Serializable;

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
