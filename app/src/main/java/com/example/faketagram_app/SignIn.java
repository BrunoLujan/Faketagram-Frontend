package com.example.faketagram_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.faketagram_app.model.Users;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignIn extends AppCompatActivity {

    Button btnSignIn;
    Button btnRegister;
    EditText txtEmail, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtEmail = (EditText) findViewById(R.id.txtEmailSignIn);
        txtPassword = (EditText) findViewById(R.id.txtPasswordSignIn);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(SignIn.this, MainActivity.class);
                //startActivity(intent);
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

        Call<Users> loginResponseCall = Constant.getUSerService().login(loginRequest);
        loginResponseCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if(response.isSuccessful()) {
                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    startActivity(intent);
                }else
                    Constant.Message(getApplicationContext(),"User/Password is incorrect");
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Constant.Message(getApplicationContext(),"Error, try again");
            }
        });
    }
}