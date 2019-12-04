package pt.isec.mindunlocker.api.model;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("token_type")
    private String token_type;
    @SerializedName("expires_in")
    private int expires_in;
    @SerializedName("userName")
    private String userName;
    @SerializedName(".issued")
    private String issued;
    @SerializedName(".expires")
    private String expires;

    public String getToken() {
        return access_token;
    }

    public void setToken(String token) {
        this.access_token = token;
    }
}
