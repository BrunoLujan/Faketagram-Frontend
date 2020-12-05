package com.example.faketagram_app;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constant {
    public static final String URL = "http://192.168.0.2/";
    public static String HOME = URL+"api/";
    //public static String LOGIN = HOME+"/login";
    //public static String SIGNUP = HOME+"singup";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit==null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(HOME)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void Message(Context context, String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
