package com.example.proto1;


import android.content.Intent;


import java.util.ArrayList;

public class Wishlist extends GenericScreen {
    private ArrayList<House> getWishlist() {
        ArrayList<House> houseList = new ArrayList<>();
        for (String houseID: thisDatabase.getWishlistQuery(thisUser))
            houseList.add(thisDatabase.getWishlistFromDatabase(houseID));
        return houseList;
    }

    @Override
    protected void setupScreen() {
        setupList();
        setScreenName(getStringValueFromRes(R.string.wishlist));
    }

    @Override
    protected void controlScreen() {
        houseControl();
    }

    private void setupList(){
        HouseAdapter adapter = new HouseAdapter(this,R.layout.adapter_house_view,getWishlist());
        getMainList().setClickable(true);
        getMainList().setAdapter(adapter);
    }

    private void houseControl() {
        getMainList().setOnItemClickListener((adapterView, view, i, l) -> switchScreenToItemView(i));
    }

    private void switchScreenToItemView(int i) {
        Intent to_item_view = intentMaker.getHouseViewIntent();

        putValueToIntentItemView(i, to_item_view);
        startActivity(to_item_view);
    }

    private void putValueToIntentItemView(int i, Intent to_item_view) {
        House this_item = getWishlist().get(i);

        to_item_view.putExtra("item", this_item.getHouseID());
        to_item_view.putExtra("previous_screen_house_view","wish_list_screen");
    }
}