package com.example.faketagram_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.faketagram_app.interfaces.ApiService;
import com.example.faketagram_app.model.Users;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private ApiService service = Constant.getRetrofit().create(ApiService.class);
    Button btnGoToSignIn, btnSignUp;
    EditText txtName, txtLastName, txtUserName, txtEmail, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtName = (EditText) findViewById(R.id.txtNameSignUp);
        txtLastName = (EditText) findViewById(R.id.txtLastNameSignUp);
        txtUserName = (EditText) findViewById(R.id.txtUserNameSignUp);
        txtEmail = (EditText) findViewById(R.id.txtEmailSignUp);
        txtPassword = (EditText) findViewById(R.id.txtPasswordSignUp);

        btnGoToSignIn = (Button) findViewById(R.id.btnGoToSignIn);
        btnGoToSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (validate()){
                    signUp();
                } else {
                    Constant.Message(getApplicationContext(),"Complete all fields");
                }
            }
        });
    }

    

    private boolean validate() {
        if(txtName.getText().toString().isEmpty()) {
            return false;
        }

        if(txtLastName.getText().toString().isEmpty()) {
            return false;
        }

        if(txtEmail.getText().toString().isEmpty()) {
            return false;
        }

        if(txtPassword.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private void signUp() {
        Call<Users> call = service.signUpUser(
                txtName.getText().toString(),
                txtLastName.getText().toString(),
                txtUserName.getText().toString(),
                txtEmail.getText().toString(),
                txtPassword.getText().toString()
        );
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if(response.isSuccessful()) {
                    Constant.Message(getApplicationContext(), "User has been added");
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    startActivity(intent);
                }else
                    Constant.Message(getApplicationContext(),"Error, try again");
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }
}