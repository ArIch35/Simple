package com.example.proto1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Objects;

public class LoginScreen extends AbstractScreen {
    private CheckBox getSavePasswordButton() {
        return findViewById(R.id.save_password);
    }

    private String getPasswordFromScreen() {
        return getPasswordBar().getText().toString();
    }

    private EditText getPasswordBar() {
        return findViewById(R.id.user_password);
    }

    private EditText getUsernameBar() {
        return findViewById(R.id.user_email_address);
    }

    private String getUsernameFromScreen() {
        return getUsernameBar().getText().toString();
    }

    private ImageView getLoginButton() {
        return findViewById(R.id.b_click_me);
    }

    @Override
    protected void screenInterface() {
        setContentView(R.layout.activity_login_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        loginSetup();
        loginButtonControl();
        motionControl();
    }

    public void motionControl(){
        getUsernameBar().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                limitCharacter(s,40,"Username limit Reached!",getUsernameBar());

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        getPasswordBar().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                limitCharacter(s,20,"Password limit Reached!",getPasswordBar());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void limitCharacter(CharSequence s,int limiter,String message,EditText bar) {
        if (s.toString().length() > limiter) {
            generateToast(message);
            bar.setFilters(new InputFilter[] { new InputFilter.LengthFilter(limiter) });
            bar.setError(getStringValueFromRes(R.string.limit_reaced));
        }


    }

    private void loginSetup() {
        getPreviousSession();
    }

    private void getPreviousSession(){
        boolean checkboxIsChecked = spMaker.getCheckboxStatus();

        if (checkboxIsChecked)
            setLoginInfo();
    }

    private void setLoginInfo() {
        getUsernameBar().setText(spMaker.getUserName());
        getPasswordBar().setText(spMaker.getPassword());
        getSavePasswordButton().setChecked(true);
    }

    private void loginButtonControl() {
        getLoginButton().setOnClickListener(view -> {

            if(checkUserCredentials()) {
                processLogin();
            }
        });
    }

    private boolean checkUserCredentials() {
        if (checkConnection()) return false;

        if (checkUserEmail()) return false;

        generateToast(getStringValueFromRes(R.string.success));
        return true;
    }

    private boolean checkConnection() {
        if (!thisDatabase.checkIfConnectionSuccess()){
            generateToast(getStringValueFromRes(R.string.conn_failed));
            return true;
        }
        return false;
    }

    private boolean checkUserEmail() {
        Email this_user_email = thisDatabase.getEmailFromDatabase(getUsernameFromScreen());

        if (checkUsername(this_user_email)) return true;

        return checkPassword(this_user_email);
    }

    private boolean checkUsername(Email this_user_email) {
        if(this_user_email == null){
            generateToast(getStringValueFromRes(R.string.username_false));
            return true;
        }
        return false;
    }

    private boolean checkPassword(Email this_user_email) {
        boolean password_is_equal_email = this_user_email.getEmailPassword().equals(getPasswordFromScreen());
        if(!password_is_equal_email){
            generateToast(getStringValueFromRes(R.string.pass_false));
            return true;
        }
        return false;
    }

    private void processLogin() {
        if(getSavePasswordButton().isChecked()){
            saveLoginInfo();
        }
        else{
            deleteLoginInfo();
        }
        saveUser();
        switchToFirstScreen();
    }

    private void saveLoginInfo() {
        SharedPreferences.Editor editor = spMaker.getSPEditor();
        editor.putString("username",getUsernameFromScreen());
        editor.putString("pass",getPasswordFromScreen());
        editor.putBoolean("status",true);
        editor.apply();
    }

    private void deleteLoginInfo() {
        SharedPreferences.Editor editor = spMaker.getSPEditor();
        editor.remove("username");
        editor.remove("pass");
        editor.remove("status");
        editor.apply();
    }

    private void saveUser(){
        SharedPreferences.Editor editor = spMaker.getSPEditor();
        editor.putString("user_now",getUsernameFromScreen());
        editor.apply();
    }

    private void switchToFirstScreen() {
        startActivity(intentMaker.getFirstScreenIntent());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("username",getUsernameFromScreen());
        outState.putString("password",getPasswordFromScreen());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        getUsernameBar().setText(savedInstanceState.getString("username"));
        getPasswordBar().setText(savedInstanceState.getString("password"));
        super.onRestoreInstanceState(savedInstanceState);
    }
}