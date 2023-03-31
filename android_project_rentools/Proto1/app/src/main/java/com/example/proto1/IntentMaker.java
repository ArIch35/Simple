package com.example.proto1;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

public class IntentMaker {
    Context context;

    public IntentMaker(Context context) {
        this.context = context;
    }

    @NonNull
    public Intent getFirstScreenIntent() {
        return new Intent(context,FirstScreen.class);
    }

    @NonNull
    public Intent getPersonalScreenIntent() {
        return new Intent(context,PersonalScreen.class);
    }

    @NonNull
    public Intent getWishlistIntent() {
        return new Intent(context, Wishlist.class);
    }

    @NonNull
    public Intent getLoginScreenIntent() {
        return new Intent(context, LoginScreen.class);
    }

    @NonNull
    public Intent getOwnerScreenIntent() {
        return new Intent(context, OwnerProperties.class);
    }

    @NonNull
    public Intent getContactScreenIntent() {
        return new Intent(context, ContactScreen.class);
    }

    @NonNull
    public Intent getFilterScreenIntent() {
        return new Intent(context, FilterScreen.class);
    }

    @NonNull
    public Intent getHouseViewIntent(){return new Intent(context,HouseView.class);}

    @NonNull
    public Intent getChatScreenIntent(){return new Intent(context,ChatScreen.class);}

}
