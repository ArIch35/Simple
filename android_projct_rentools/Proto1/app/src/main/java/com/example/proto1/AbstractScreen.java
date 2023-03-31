package com.example.proto1;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class AbstractScreen extends AppCompatActivity {
    protected final Database thisDatabase = new Database(this);
    protected final IntentMaker intentMaker = new IntentMaker(this);
    protected final SharedPreferenceMaker spMaker = new SharedPreferenceMaker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageSetup();
        screenInterface();
    }

    protected void languageSetup() {
        if (spMaker.getLanguage().equals("en")){
            setLanguage("en");
            return;
        }
        setLanguage("de");
    }

    protected void screenInterface(){}

    private void setLanguage(String language){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(language);
        resources.updateConfiguration(configuration,metrics);
        onConfigurationChanged(configuration);
    }

    protected void generateToast(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    protected String getStringValueFromRes(int id){
        return (String) getResources().getText(id);
    }
}
