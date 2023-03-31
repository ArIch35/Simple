package com.example.proto1;

import android.content.Intent;
import java.util.ArrayList;

public class ContactScreen extends GenericScreen {
    private ArrayList<Chat> getChatList(){
        return thisDatabase.getChatPartners(thisUser);
    }

    @Override
    protected void setupScreen() {
        setupList();
        setScreenName(getStringValueFromRes(R.string.contacts));
    }

    @Override
    protected void controlScreen() {
        listControl();
    }

    private void setupList(){
        ContactAdapter adapter = new ContactAdapter(this,R.layout.adapter_contact,getChatList());
        getMainList().setClickable(true);
        getMainList().setAdapter(adapter);
    }

    private void listControl() {
        getMainList().setOnItemClickListener((adapterView, view, i, l) -> changeToChatScreen(i));
    }

    private void changeToChatScreen(int index) {
        Intent to_chat_screen = intentMaker.getChatScreenIntent();
        User chat_partner = getChatList().get(index).getChatPartner();

        putValueToIntent(to_chat_screen, chat_partner);
        startActivity(to_chat_screen);
    }

    private void putValueToIntent(Intent to_chat_screen, User chat_partner) {
        to_chat_screen.putExtra("buyer",thisUser.getUserID());
        to_chat_screen.putExtra("seller", chat_partner.getUserID());
        to_chat_screen.putExtra("previous_screen_chat_screen","contact_screen");
    }
}