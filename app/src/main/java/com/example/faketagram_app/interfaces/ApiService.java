package com.example.faketagram_app.interfaces;

import com.example.faketagram_app.Constant;
import com.example.faketagram_app.LoginResponse;
import com.example.faketagram_app.model.Users;
import com.example.faketagram_app.LoginRequest;

import java.util.List;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

 @POST("sign_up")
 @FormUrlEncoded
 Call<Users> signUpUser(
         @Field("name") String name,
         @Field("lastname") String lastname,
         @Field("username") String username,
         @Field("email") String email,
         @Field("password") String password
 );

 @POST("login")
 Call<LoginResponse> login(@Body LoginRequest user);

 @GET("user")
 Call<Users> getLoggedUser(@Header("Authorization") String authToken);

 @GET("{name}/get_name")
 Call<List<Users>> getSearchUsers(@Header("Authorization") String authToken, @Path("name") String name);
}

