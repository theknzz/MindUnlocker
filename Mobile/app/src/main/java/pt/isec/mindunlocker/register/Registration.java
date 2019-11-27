package pt.isec.mindunlocker.register;

import java.util.HashMap;

public class Registration {
    protected String Email;
    protected String Password;
    protected String ConfirmPassword;
    protected String Name;

    protected HashMap<String, String> publisher = new HashMap<String, String>();

    public Registration(String email, String password, String confirmPassword, String name) {
        publisher.put("Email", email);
        publisher.put("Password", password);
        publisher.put("ConfirmPassword", confirmPassword);
        publisher.put("Name", name);
    }
}
