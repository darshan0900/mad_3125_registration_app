package com.darshan09200.registrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

enum FormState {
    LOGIN,
    SIGNUP,
    LOGGED_IN
}

public class MainActivity extends AppCompatActivity {

    private EditText username, password, firstName, lastName;
    private TextView loggedInDetails;
    private Button formBtn, switchBtn;
    private FormState currFormState = FormState.LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI(findViewById(R.id.main_parent));

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastNme);

        loggedInDetails = findViewById(R.id.loggedInDetails);

        formBtn = findViewById(R.id.formBtn);
        switchBtn = findViewById(R.id.switchBtn);

        switchToLogin();

        formBtn.setOnClickListener(view -> onFormSubmit());

        switchBtn.setOnClickListener(view -> {
            if (currFormState == FormState.LOGIN) switchToSignup();
            else switchToLogin();
        });
    }

    public void switchToLogin() {
        clearFields();

        currFormState = FormState.LOGIN;

        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);

        formBtn.setText("Login");
        switchBtn.setText("Don't have an account? Sign Up");
    }

    public void switchToSignup() {
        clearFields();

        currFormState = FormState.SIGNUP;

        firstName.setVisibility(View.VISIBLE);
        lastName.setVisibility(View.VISIBLE);

        formBtn.setText("Sign Up");
        switchBtn.setText("Already have an account? Login");
    }

    public void showLoggedInDetails() {
        User loggedInUser = Database.getInstance().getLoggedInUser();
        if (loggedInUser != null) {
            currFormState = FormState.LOGGED_IN;
            username.setVisibility(View.GONE);
            password.setVisibility(View.GONE);

            firstName.setVisibility(View.GONE);
            lastName.setVisibility(View.GONE);
            switchBtn.setVisibility(View.GONE);

            loggedInDetails.setVisibility(View.VISIBLE);
            String details = "First Name: " + loggedInUser.getFirstName() + "\nLast Name: " + loggedInUser.getLastName();
            loggedInDetails.setText(details);

            formBtn.setText("Logout");
        }
    }

    public void logout() {
        Database.getInstance().logoutUser();

        username.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        switchBtn.setVisibility(View.VISIBLE);

        loggedInDetails.setVisibility(View.GONE);

        switchToLogin();
    }

    public void onFormSubmit() {
        if (currFormState == FormState.LOGGED_IN) {
            logout();
            return;
        }
        String usernameInput = username.getText().toString().trim();
        String passwordInput = password.getText().toString();
        String firstNameInput = firstName.getText().toString().trim();
        String lastNameInput = lastName.getText().toString().trim();

        boolean error = false;

        if (usernameInput.isEmpty()) {
            showToast("Please enter username");
            error = true;
        } else if (passwordInput.isEmpty()) {
            showToast("Please enter password");
            error = true;
        } else if (currFormState == FormState.SIGNUP) {
            if (firstNameInput.isEmpty()) {
                showToast("Please enter first name");
                error = true;
            } else if (lastNameInput.isEmpty()) {
                showToast("Please enter last name");
                error = true;
            } else {
                Status userAddStatus = Database.getInstance().addNewUser(new User(firstNameInput, lastNameInput, usernameInput, passwordInput));
                if (userAddStatus == Status.USER_EXISTS) {
                    showToast("User already exists");
                    error = true;
                } else {
                    showToast("User Added");
                }
            }
        } else {
            Status userLoginStatus = Database.getInstance().isValidLogin(usernameInput, passwordInput);
            if (userLoginStatus == Status.INVALID_PASSWORD) {
                showToast("Invalid username/password");
                error = true;
            } else {
                showToast("Logged In");
            }
        }
        if (!error) {
            if (currFormState == FormState.LOGIN)
                showLoggedInDetails();
            else
                switchToLogin();
        }
    }

    private void clearFields() {
        username.getText().clear();
        password.getText().clear();
        firstName.getText().clear();
        lastName.getText().clear();
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideKeyboard(MainActivity.this.getCurrentFocus());
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void hideKeyboard(View view) {
        try {
            username.clearFocus();
            password.clearFocus();
            firstName.clearFocus();
            lastName.clearFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}