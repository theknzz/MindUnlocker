package pt.isec.mindunlocker.api.model;

import com.google.gson.annotations.SerializedName;

public class User {

    private String grant_type="password";
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
