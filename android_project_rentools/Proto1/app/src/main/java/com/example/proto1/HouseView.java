package com.example.proto1;

import androidx.annotation.NonNull;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class HouseView extends AbstractScreen implements ClickListener{
    private User thisUser = new User();
    private House thisHouse = new House();
    private TextView[] dotText;
    private PopUpWindow houseWindow;
    private byte[] showedImage;

    class HouseHolder{
        TextView city = findViewById(R.id.city_house_view_screen);
        TextView address = findViewById(R.id.address_house_view_screen);
        TextView date = findViewById(R.id.available_house_view_screen);
        TextView size = findViewById(R.id.size_house_view_screen);
        TextView price = findViewById(R.id.price_house_view_screen);
        TextView duration = findViewById(R.id.duration_house_view_screen);
        TextView utilities = findViewById(R.id.utilities_house_view_screen);
        TextView furnished = findViewById(R.id.furnished_house_view_screen);
        TextView description = findViewById(R.id.desc_house_view_screen);

        ImageView ownerImage = findViewById(R.id.owner_image_house_view_screen);
        TextView ownerEmail = findViewById(R.id.owner_email_house_view_screen);
    }

    private void assignItemAttributes() {
        HouseHolder holder = new HouseHolder();
        String city = "\n "+ getStringValueFromRes(R.string.city)  + thisHouse.getCity();
        String address = " " + getStringValueFromRes(R.string.address) + thisHouse.getHouseAddress();
        String date = " " + getStringValueFromRes(R.string.date) + thisHouse.getAvailableDate();
        String size = " " + getStringValueFromRes(R.string.size) + thisHouse.getHouseSize() + " m²";
        String price = " " + getStringValueFromRes(R.string.price) + thisHouse.getHousePrice() + " €";
        String duration = " " + getStringValueFromRes(R.string.duration) + thisHouse.getRentDuration() + " " + getStringValueFromRes(R.string.month);
        String covered = " " + getStringValueFromRes(R.string.total_rent) + convertBoolToValue(thisHouse.hasUtilities());
        String furnished = " " + getStringValueFromRes(R.string.furnished) + convertBoolToValue(thisHouse.isFurnished());
        String description = "\n\n" + getStringValueFromRes(R.string.description) + thisHouse.getDescription()+ "\n";

         /*city = "\n " + getStringValueFromRes(R.string.city) + "                : " + thisHouse.getCity() +
                "\n " + getStringValueFromRes(R.string.address) + "           : " + thisHouse.getHouseAddress() +
                "\n " + getStringValueFromRes(R.string.date) + ": " + thisHouse.getAvailableDate()
                ;*/

        holder.city.setText(city);
        holder.address.setText(address);
        holder.date.setText(date);
        holder.size.setText(size);
        holder.price.setText(price);
        holder.duration.setText(duration);
        holder.utilities.setText(covered);
        holder.furnished.setText(furnished);
        holder.description.setText(description);

        holder.ownerEmail.setText(thisHouse.getItemOwner().getUserEmail().getEmailAddress());

        setupImageBitmap(holder.ownerImage, thisHouse.getItemOwner().getImage());
    }

    private String convertBoolToValue(boolean status){
        if (status){
            return getStringValueFromRes(R.string.yes);
        }
        return getStringValueFromRes(R.string.no);
    }

    private void setupImageBitmap(ImageView image, byte[] image_link) {
        Bitmap bitmap;
        if (image_link != null) {
            bitmap = BitmapFactory.decodeByteArray(image_link, 0, image_link.length);
        }
        else {
            bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.unknown);
        }
        image.setImageBitmap(bitmap);
    }

    private RecyclerView getRecyclerView() {
        return findViewById(R.id.item_images_house_view_screen);
    }

    @NonNull
    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
    }

    private ImageView getBackButton() {
        return findViewById(R.id.back_arrow_all_screen);
    }

    private ImageView getWishStar() {
        return findViewById(R.id.wish_star_house_view_screen);
    }

    private FloatingActionButton getChatButton() {
        return findViewById(R.id.chat_button_house_view_screen);
    }

    private void setHouse() {
        thisHouse = thisDatabase.getHouseFromDatabase (getIntent().getIntExtra("item",0));
    }

    private void setUser() {
        thisUser = thisDatabase.getUserFromDatabase(spMaker.getUserEmail());
    }

    @Override
    protected void screenInterface() {
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_house_view_screen);

        initializeHouseViewScreen();
        houseScreenControl();
    }

    private void initializeHouseViewScreen() {
        houseWindow = new PopUpWindow(R.layout.popup_image,this,true);
        getValueFromFirstScreen();
        assignItemAttributes();
        setupImageList();
        generateDot();
        chatButtonSetup();
    }

    private void getValueFromFirstScreen() {
        setUser();
        setHouse();
    }

    private void setupImageList(){
        getRecyclerView().measure(0, 0);
        ImageAdapter imageAdapter = new ImageAdapter((ArrayList<byte[]>) thisHouse.getHouseImage(), this);
        getRecyclerView().setLayoutManager(getLinearLayoutManager());
        getRecyclerView().setAdapter(imageAdapter);
    }

    @NonNull
    private ImageView generateArrow(int rotation) {
        ImageView arrow = new ImageView(getApplicationContext());
        arrow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        arrow.setImageResource(R.drawable.ic_baseline_arrow_back_ios_24);
        arrow.setRotationY(rotation);
        return arrow;
    }

    private LinearLayout getRightArrowLayout() {
        return findViewById(R.id.right_arrow_holder);
    }

    private LinearLayout getLeftArrowLayout() {
        return findViewById(R.id.left_arrow_holder);
    }

    private void addNewArrow(LinearLayout layout,int rotation){
        layout.addView(generateArrow(rotation));
    }

    private void imagesHolderControl() {
        addNewArrow(getLeftArrowLayout(),0);
        addNewArrow(getRightArrowLayout(),180);
        getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int count = Objects.requireNonNull(getRecyclerView().getAdapter()).getItemCount();
                int position = getScrollPosition();
                dotControl(count);
                arrowSetup(position,count);
                arrowControl();
            }

            private void arrowControl() {
                getLeftArrowLayout().setOnClickListener(v -> getRecyclerView().smoothScrollToPosition(getScrollPosition()-1));
                getRightArrowLayout().setOnClickListener(v -> getRecyclerView().smoothScrollToPosition(getScrollPosition()+1));
            }

            private void arrowSetup(int pos,int count) {
                LinearLayoutManager myLayoutManager = (LinearLayoutManager) getRecyclerView().getLayoutManager();

                if (pos != 0 && pos != count-1){
                    getRightArrowLayout().setVisibility(View.VISIBLE);
                    getLeftArrowLayout().setVisibility(View.VISIBLE);
                }
                if (pos == count-1){
                    getRightArrowLayout().setVisibility(View.GONE);
                    getLeftArrowLayout().setVisibility(View.VISIBLE);
                }
                if (pos == 0){
                    getLeftArrowLayout().setVisibility(View.GONE);
                    getRightArrowLayout().setVisibility(View.VISIBLE);
                }
                if (Objects.requireNonNull(myLayoutManager).findFirstCompletelyVisibleItemPosition() == -1){
                    getRightArrowLayout().setVisibility(View.GONE);
                    getLeftArrowLayout().setVisibility(View.GONE);
                }
            }

            private void dotControl(int count) {
                for (int i = 0; i < count; i++) {
                    dotText[i].setTextColor(Color.GRAY);
                }

                dotText[getScrollPosition()].setTextColor(Color.WHITE);
            }

            private int getScrollPosition() {
                LinearLayoutManager myLayoutManager = (LinearLayoutManager) getRecyclerView().getLayoutManager();
                int scrollPosition = Objects.requireNonNull(myLayoutManager).findFirstCompletelyVisibleItemPosition();
                if (scrollPosition < 0)
                    scrollPosition = Objects.requireNonNull(myLayoutManager).findFirstVisibleItemPosition();
                return scrollPosition;
            }
        });
    }

    private void generateDot() {
        LinearLayout mDotsLayout = findViewById(R.id.image_count);
        int count = Objects.requireNonNull(getRecyclerView().getAdapter()).getItemCount();
        dotText = new TextView[count];

        for (int i = 0; i < count; i++) {
            dotText[i] = new TextView(this);
            dotText[i].setText(".");
            dotText[i].setTextSize(45);
            dotText[i].setTypeface(null, Typeface.BOLD);
            dotText[i].setTextColor(Color.GRAY);
            mDotsLayout.addView(dotText[i]);
        }
    }

    private void houseScreenControl() {
        backArrowSetting();
        imagesHolderControl();
        toolbarControl();
        chatButtonControl();
    }

    @Override
    public void imageControl(byte[] image) {
        showedImage = image;
        showImage();
    }

    private void showImage() {
        houseWindow.show();
        setupImageBitmap(houseWindow.getImageView(R.id.popup_image), showedImage);
        houseWindow.getImageView(R.id.back_button_popup_image).setOnClickListener(view -> houseWindow.close());
    }

    private void backArrowSetting() {
        getBackButton().setOnClickListener(view -> switchScreen());
    }

    private void switchScreen() {
        String target_screen_string = getIntent().getStringExtra("previous_screen_house_view");
        Intent switch_screen;

        switch_screen = decideNextScreen(target_screen_string);
        switch_screen.putExtra("position", getIntent().getIntExtra("position",0));
        startActivity(switch_screen);
    }

    private Intent decideNextScreen(String target_screen_string) {
        switch (target_screen_string){
            case "first_screen":
                return intentMaker.getFirstScreenIntent();
            case "wish_list_screen":
                return intentMaker.getWishlistIntent();
        }
        return null;
    }

    private void toolbarControl(){
        setupWishStar();
        wishStarControl();
    }

    private void setupWishStar() {
        if (ownerHouse())
            getWishStar().setVisibility(View.INVISIBLE);
    }

    private boolean ownerHouse(){
        return thisDatabase.isOwnerHouse(thisUser.getUserID(),thisHouse.getHouseID());
    }

    private void wishStarControl() {
        if (thisDatabase.hasItem(thisUser,thisHouse)){
            getWishStar().setImageResource(R.drawable.star_filled);
        }

        getWishStar().setOnClickListener(view -> wishStarResult());
    }

    private void wishStarResult() {
        boolean is_on_wishlist = thisDatabase.hasItem(thisUser,thisHouse);
        if(checkIfUserAddedTheirOwnHouseToWishlist()) {
            generateToast("You can't do that");
            return;
        }
        if(is_on_wishlist)
            removeFromWishList();
        else
            addToWishList();
    }

    private boolean checkIfUserAddedTheirOwnHouseToWishlist(){
        return thisUser.getUserID() == thisHouse.getItemOwner().getUserID();
    }

    private void addToWishList() {
        getWishStar().setImageResource(R.drawable.star_filled);
        thisDatabase.addToWishListQuery(thisUser,thisHouse);
        generateToast(getStringValueFromRes(R.string.added_wishlist));
    }

    private void removeFromWishList(){
        getWishStar().setImageResource(R.drawable.star_unfilled);
        thisDatabase.removeFromWishlistQuery(thisUser,thisHouse);
        generateToast(getStringValueFromRes(R.string.remove_wishlist));
    }

    private void chatButtonSetup() {
        if (ownerHouse())
            getChatButton().setVisibility(View.INVISIBLE);
    }

    private void chatButtonControl() {
        getChatButton().setOnClickListener(view -> changeToChatScreen());
    }

    private void changeToChatScreen() {
        Intent to_chat_screen = intentMaker.getChatScreenIntent();
        User item_owner = thisHouse.getItemOwner();

        if(!thisDatabase.addToChatQuery(thisUser,item_owner)){
            generateToast("You can't do that");
            return;
        }

        setupChatScreenIntent(to_chat_screen, item_owner);
        startActivity(to_chat_screen);
    }

    private void setupChatScreenIntent(Intent to_chat_screen, User item_owner) {
        to_chat_screen.putExtra("buyer",thisUser.getUserID());
        to_chat_screen.putExtra("seller", item_owner.getUserID());
        to_chat_screen.putExtra("item", thisHouse.getHouseID());
        to_chat_screen.putExtra("previous_screen_house_view",getIntent().getStringExtra("previous_screen_house_view"));
        to_chat_screen.putExtra("previous_screen_chat_screen","house_view_screen");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (houseWindow.isShowing()) {
            outState.putBoolean("imageOpen", true);
            outState.putByteArray("image",showedImage);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean("imageOpen")){
            showedImage = savedInstanceState.getByteArray("image");
            findViewById(R.id.image_house_view_adapter).post(this::showImage);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}