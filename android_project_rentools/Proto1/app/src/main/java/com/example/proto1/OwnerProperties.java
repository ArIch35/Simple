package com.example.proto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;
import java.util.Objects;

public class OwnerProperties extends AppCompatActivity {
    private final Database thisDatabase = new Database(this);
    private User thisUser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();


        getUser();
        setupList(thisDatabase.getOwnerHouseListFromDatabase(thisUser));
        controlMainListView();
        backArrowSetting();
    }

    private void setupList(List<House> item_list){
        ListView myList = findViewById(R.id.list_generic_screen);
        HouseAdapter adapter = new HouseAdapter(this,R.layout.adapter_house_view,item_list);
        myList.setClickable(true);
        myList.setAdapter(adapter);
    }

    private void getUser() {
        SharedPreferences new_session = this.getSharedPreferences("Login",MODE_PRIVATE);
        String user_email = new_session.getString("user_now",null);
        thisUser = thisDatabase.getUserFromDatabase(user_email);
    }

    private void controlMainListView() {
        ListView main_list = findViewById(R.id.list_generic_screen);
        listControl(main_list);
    }

    private void listControl(ListView myList) {
        myList.setOnItemClickListener((adapterView, view, i, l) -> switchScreenToItemView(i));
    }

    private void switchScreenToItemView(int i) {
        Intent to_item_view = new Intent(OwnerProperties.this, HouseView.class);

        putValueToItemViewIntent(i, to_item_view);

        startActivity(to_item_view);
    }

    private void putValueToItemViewIntent(int i, Intent to_item_view) {
        House this_item = getItemFromList(i);

        to_item_view.putExtra("item", this_item.getHouseID());
        to_item_view.putExtra("previous_screen_house_view","owner_properties");
    }

    private House getItemFromList(int index){
        return thisDatabase.getOwnerHouseListFromDatabase(thisUser).get(index);
    }

    private void backArrowSetting() {
        ImageView back_button = findViewById(R.id.back_arrow_all_screen);

        back_button.setOnClickListener(view -> switchScreen());
    }

    private void switchScreen() {
        Intent switch_screen = new Intent(OwnerProperties.this,FirstScreen.class);

        startActivity(switch_screen);
    }
}