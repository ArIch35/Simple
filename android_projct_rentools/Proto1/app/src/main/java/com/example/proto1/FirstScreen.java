package com.example.proto1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FirstScreen extends AbstractScreen {
    protected User thisUser = new User();
    private List<House> houseList = new ArrayList<>();

    protected Pair<Integer,Integer> getPrices() {
        int maximum = getIntent().getIntExtra("priceMax",0);
        int minimum = getIntent().getIntExtra("priceMin",0);
        return new Pair<>(minimum,maximum);
    }

    protected String getDuration(){
        return getIntent().getStringExtra("houseDuration");
    }

    protected String getDate(){
        return getIntent().getStringExtra("houseDate");
    }

    protected Integer getSize(){
        return getIntent().getIntExtra("houseSize",0);
    }

    protected String getSort() {
        return getIntent().getStringExtra("houseSort");
    }

    protected String getOrder(){
        return getIntent().getStringExtra("order");
    }

    private ListView getMainList() {
        return findViewById(R.id.list_view_main_first_screen);
    }

    private DrawerLayout getDrawerLayout() {
        return findViewById(R.id.layout_first_screen);
    }

    private ImageView getMenuIcon() {
        return findViewById(R.id.list_toggle_menu_first_screen);
    }

    private ImageView getSearchIcon() {
        return findViewById(R.id.list_toggle_search_first_screen);
    }

    protected Bundle getUserNameBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("user_name", spMaker.getUserEmail());
        return bundle;
    }

    private void setUser() {
        thisUser = thisDatabase.getUserFromDatabase(spMaker.getUserEmail());
    }

    protected void setHouseList(List<House> item_list) {
        houseList = item_list;
    }

    @Override
    protected void screenInterface(){
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_first_screen);

        setupFirstScreen();
        firstScreenControl();
    }

    private void setupFirstScreen() {
        //Objects.requireNonNull(this.getSupportActionBar()).hide();
        setUser();
        setupMainList();
    }

    private void setupMainList() {
        Pair<Boolean,List<House>> query_result = thisDatabase.getFilterResultFromDatabase(
                spMaker.getSearchedCity(), getPrices(), getDuration(),getDate(),
                getSize(),getSort(),getOrder(),thisUser);

        if (checkIfFiltered())
            generateFilterResultToast(query_result);

        setHouseList(query_result.second);
        setupMainListView();
    }

    private boolean checkIfFiltered(){
        return getIntent().getBooleanExtra("filtered",false);
    }

    private void generateFilterResultToast(Pair<Boolean, List<House>> query_result) {
        if (query_result.first)
            generateToast(getStringValueFromRes(R.string.filter_success) + query_result.second.size() + getStringValueFromRes(R.string.result));
        else
            generateToast(getStringValueFromRes(R.string.filter_failed));
    }

    protected void setupMainListView(){
        HouseAdapter adapter = new HouseAdapter(this,R.layout.adapter_house_view,houseList);
        getMainList().setClickable(true);
        getMainList().setAdapter(adapter);
        getMainList().setSelection(getIntent().getIntExtra("position",0));
    }

    private void firstScreenControl() {
        controlMainList();
        toolbarControl();
    }

    private void controlMainList(){
        getMainList().setOnItemClickListener((adapterView, view, i, l) -> switchScreenToHouseView(i));
    }

    private void switchScreenToHouseView(int i) {
        Intent intent = intentMaker.getHouseViewIntent();
        putValueToItemViewIntent(i,intent);
        startActivity(intent);
    }

    private void putValueToItemViewIntent(int i,Intent intent) {
        House this_item = houseList.get(i);

        intent.putExtra("item", this_item.getHouseID());
        intent.putExtra("previous_screen_house_view","first_screen");
        intent.putExtra("position",i);
    }

    protected void toolbarControl(){
        toolbarListIconControl();
        toolbarFilterIconControl();
    }

    private void toolbarListIconControl(){
        getMenuIcon().setOnClickListener(v -> {
            loadFragment(new FirstLeftScreen(),R.id.nav_view_menu_first_screen,getUserNameBundle());
            getDrawerLayout().openDrawer(GravityCompat.START);
        });

    }

    protected void loadFragment(Fragment fragment, int resID, @Nullable Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(resID, fragment);
        fragmentTransaction.commit();
    }

    private void toolbarFilterIconControl(){
        getSearchIcon().setOnClickListener(v -> {
            loadFragment(new FirstRightScreen(),R.id.nav_view_search_first_screen,new Bundle());
            getDrawerLayout().openDrawer(GravityCompat.END);
        });
    }
}