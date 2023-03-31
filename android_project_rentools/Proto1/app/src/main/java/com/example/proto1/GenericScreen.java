package com.example.proto1;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public abstract class GenericScreen extends AbstractScreen {
    protected User thisUser = new User();

    protected ListView getMainList() {
        return findViewById(R.id.list_generic_screen);
    }

    protected ImageView getBackButton() {
        return findViewById(R.id.back_arrow_all_screen);
    }

    private void setUser() {
        thisUser = thisDatabase.getUserFromDatabase(spMaker.getUserEmail());
    }

    protected void setScreenName(String name){
        ((TextView)findViewById(R.id.name_generic_screen)).setText(name);
    }

    private void buttonControl(){
        getBackButton().setOnClickListener(view -> startActivity(intentMaker.getFirstScreenIntent()));
    }

    protected void setupScreen(){}

    protected void controlScreen(){}

    @Override
    protected void screenInterface() {
        setContentView(R.layout.activity_generic_screen);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        setUser();
        setupScreen();
        controlScreen();
        buttonControl();
    }
}