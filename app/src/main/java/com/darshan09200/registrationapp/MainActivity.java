package com.darshan09200.registrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

enum FormState {
    LOGIN,
    SIGNUP
}

public class MainActivity extends AppCompatActivity {

    private EditText username, password, firstName, lastName;
    private Button formBtn, switchBtn;

    private FormState currFormState = FormState.LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastNme);

        formBtn = findViewById(R.id.formBtn);
        switchBtn = findViewById(R.id.switchBtn);

        switchToLogin();

        formBtn.setOnClickListener(view -> {
            onFormSubmit();
        });

        switchBtn.setOnClickListener(view -> {
            if (currFormState == FormState.LOGIN) switchToSignup();
            else switchToLogin();
        });
    }

    public void switchToLogin() {
        currFormState = FormState.LOGIN;

        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);

        formBtn.setText("Login");
        switchBtn.setText("Don't have an account? Sign Up");
    }

    public void switchToSignup() {
        currFormState = FormState.SIGNUP;

        firstName.setVisibility(View.VISIBLE);
        lastName.setVisibility(View.VISIBLE);

        formBtn.setText("Sign Up");
        switchBtn.setText("Already have an account? Login");
    }

    public void onFormSubmit() {

    }
}