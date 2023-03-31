package com.example.proto1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;

public class FirstLeftScreen extends Fragment {
    private View view;
    private User thisUser = new User();
    private Database thisDatabase;
    private SharedPreferenceMaker spMaker;
    private IntentMaker intentMaker;


    private NavigationView getNavigationView() {
        return view.findViewById(R.id.nav_view_menu_first_screen);
    }

    private MenuItem getMenuItem() {
        return getNavigationView().getMenu().findItem(R.id.nav_owner_houses);
    }

    private Bitmap getUserPhotoBitmap() {
        if(thisUser.getImage()!= null) {
            return BitmapFactory.decodeByteArray(thisUser.getImage(), 0, thisUser.getImage().length);
        }
            return BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.unknown);
    }

    private View getHeaderLayout() {
        return getNavigationView().getHeaderView(0);
    }

    private ImageView getUserPhoto() {
        return getHeaderLayout().findViewById(R.id.user_photo);
    }

    private TextView getUserName() {
        return getHeaderLayout().findViewById(R.id.user_name);
    }

    private void setUser() {
        Bundle bundle = this.getArguments();
        thisUser = thisDatabase.getUserFromDatabase(bundle.getString("user_name"));
    }

    private void setDatabase() {
        thisDatabase = new Database(getActivity());
    }

    private void setView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_first_left_screen, container,false);
    }

    private FirstScreen getFirstScreen() {return (FirstScreen) getActivity();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initiateAttributes(inflater, container);
        leftScreenControl();
        return view;
    }

    private void leftScreenControl() {
        navigationMenuControl();
        headerLayoutControl();
    }

    private void initiateAttributes(LayoutInflater inflater, ViewGroup container) {
        setView(inflater, container);
        setDatabase();
        setUser();
        setMaker();
        ownerPropertiesSetup();
        assignHeaderAttributes();
    }

    private void setMaker() {
        intentMaker = new IntentMaker(getActivity());
        spMaker = new SharedPreferenceMaker(getActivity());
    }

    @SuppressLint("SetTextI18n")
    private void assignHeaderAttributes() {
        assignUserNameHeaderAttribute();
        assignUserImage();
    }

    private void assignUserImage() {
        getUserPhoto().setImageBitmap(getUserPhotoBitmap());
    }

    private void assignUserNameHeaderAttribute() {
        String user_identity = thisUser.getFirstName() + " " + thisUser.getSecondName();
        getUserName().setText(user_identity);
    }

    private void ownerPropertiesSetup() {
        if (thisUser.getUserRole() == User.Role.CUSTOMER) {
            getMenuItem().setVisible(false);
        } else if (thisUser.getUserRole() == User.Role.OWNER) {
            getMenuItem().setVisible(true);
        }
    }

    protected void navigationMenuControl() {
        getNavigationView().setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case (R.id.nav_wishlist):
                    switchScreen(intentMaker.getWishlistIntent());
                    break;
                case (R.id.nav_logout):
                    clearHistory();
                    switchScreen(intentMaker.getLoginScreenIntent());
                    break;
                case (R.id.nav_filter): {
                    putValueToFilter();
                    break;
                }
                case (R.id.nav_contacts):
                    switchScreen(intentMaker.getContactScreenIntent());
                    break;
                case (R.id.nav_owner_houses):
                    switchScreen(intentMaker.getOwnerScreenIntent());
                    break;
                case (R.id.nav_language):
                    decideLanguage();
                    break;
            }
            return true;
        });
    }

    private void putValueToFilter() {
        Intent to_filter = intentMaker.getFilterScreenIntent();
        sendPrice(to_filter);
        to_filter.putExtra("houseDate",getFirstScreen().getDate());
        to_filter.putExtra("houseSize",getFirstScreen().getSize());
        to_filter.putExtra("houseDuration",getFirstScreen().getDuration());
        to_filter.putExtra("houseSort",getFirstScreen().getSort());
        to_filter.putExtra("order",getFirstScreen().getOrder());
        switchScreen(to_filter);
    }

    private void sendPrice(Intent to_filter) {
        Pair<Integer,Integer> max_min = getFirstScreen().getPrices();
        to_filter.putExtra("priceMin",max_min.first);
        to_filter.putExtra("priceMax",max_min.second);
    }

    private void switchScreen(Intent intent) {
        startActivity(intent);
    }

    private void clearHistory() {
        SharedPreferences.Editor sp_editor = spMaker.getSPEditor();
        sp_editor.remove("cityNow");
        sp_editor.apply();
    }

    private void headerLayoutControl() {
        getHeaderLayout().setOnClickListener(view -> switchScreen(intentMaker.getPersonalScreenIntent()));
    }

    private void decideLanguage() {
        if (spMaker.getLanguage().equals("en")){
            changeLanguage("de");
            return;
        }
        changeLanguage("en");
    }

    private void changeSPLanguage(String language){
        SharedPreferences.Editor sp_editor = spMaker.getSPEditor();
        sp_editor.putString("lang",language);
        sp_editor.apply();
    }

    private void changeLanguage(String language){
        changeSPLanguage(language);
        getFirstScreen().languageSetup();
        getFirstScreen().setupMainListView();
        getFirstScreen().loadFragment(new FirstLeftScreen(),R.id.nav_view_menu_first_screen,getFirstScreen().getUserNameBundle());
    }
}