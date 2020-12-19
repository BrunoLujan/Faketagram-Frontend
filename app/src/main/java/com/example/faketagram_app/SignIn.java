package com.example.faketagram_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.TokenWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.faketagram_app.model.Users;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignIn extends AppCompatActivity {

    Button btnSignIn;
    Button btnRegister;
    EditText txtEmail, txtPassword;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Constant.getUserService();

        txtEmail = (EditText) findViewById(R.id.txtEmailSignIn);
        txtPassword = (EditText) findViewById(R.id.txtPasswordSignIn);

        preferences = getSharedPreferences("preferences", MODE_PRIVATE);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (validate()){
                    login();
                } else
                    Constant.Message(getApplicationContext(),"Complete all fields");
            }
        });

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private boolean validate() {
        if(txtEmail.getText().toString().isEmpty()) {
            return false;
        }

        if(txtPassword.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(txtEmail.getText().toString());
        loginRequest.setPassword(txtPassword.getText().toString());

        Call<LoginResponse> loginResponseCall = Constant.CONNECTION.login(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", response.body().getToken_type() + " " + response.body().getAccess_token());
                    editor.commit();
                    Constant.AUTHTOKEN = response.body().getToken_type() + " " + response.body().getAccess_token();
                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    startActivity(intent);
                }else
                    Constant.Message(getApplicationContext(),"User/Password is incorrect");
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Constant.Message(getApplicationContext(),"Error, try again");
            }
        });
    }
}