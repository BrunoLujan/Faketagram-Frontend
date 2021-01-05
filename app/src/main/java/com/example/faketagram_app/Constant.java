package com.example.faketagram_app;

import android.content.Context;
import android.widget.Toast;
import com.example.faketagram_app.interfaces.ApiService;
import com.example.faketagram_app.model.Users;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constant {
    public static final String URL = "http://192.168.0.12/";
    public static String HOME = URL + "api/user/";
    public static String PROFILEIMAGE = URL + "storage/user/photos/";
    private static Retrofit retrofit;
    public static ApiService CONNECTION;
    public static String AUTHTOKEN;
    public static Users LOGGEDUSER = new Users();

    public static Retrofit getRetrofit() {
        if(retrofit==null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(HOME)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void getUserService() {
        ApiService getUserService = getRetrofit().create(ApiService.class);
        CONNECTION = getUserService;
    }

    public static void Message(Context context, String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
