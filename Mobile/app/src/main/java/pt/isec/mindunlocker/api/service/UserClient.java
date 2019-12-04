package pt.isec.mindunlocker.api.service;

import pt.isec.mindunlocker.api.model.Token;
import pt.isec.mindunlocker.api.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface UserClient {

//    @FormUrlEncoded
//    @GET("/token")
//    Call<Registration> loginUser(@QueryMap Map<String,String> params);

    @FormUrlEncoded
    @Headers({"Content-Type:application/x-www-form-urlencoded"})
    @GET("/token")
    Call<Token> login(@Body User user);
}
