package com.example.faketagram_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

    Button btnGoToSignIn;
    EditText txtName, txtLastName, txtEmail, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnGoToSignIn = (Button) findViewById(R.id.btnGoToSignIn);
        btnGoToSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
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
}