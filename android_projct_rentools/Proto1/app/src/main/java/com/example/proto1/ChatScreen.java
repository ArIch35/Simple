package com.example.proto1;



import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatScreen extends AbstractScreen  {
    private User thisUser = new User();
    private User chatPartner = new User();
    private List<Message> listAllMessages = new ArrayList<>();

    private Integer getChatID(String type){
        return getIntent().getIntExtra(type,0);
    }

    private ListView getChatList() {
        return findViewById(R.id.chat_list_chat);
    }

    private TextView getToolbarName() {
        return findViewById(R.id.toolbar_name);
    }

    private TextView getChatMessage() {
        return findViewById(R.id.chat_input_message_chat);
    }

    private ImageView getBackButton() {
        return findViewById(R.id.back_button);
    }

    @Override
    protected void screenInterface() {
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_chat_screen);

        chatScreenSetup();
        chatScreenControl();
    }

    private void chatScreenSetup() {
        getChatMembers();
        addAllMessageToMessageList();
        setupChatList();
        setupToolbar();
    }

    private void getChatMembers() {
        chatPartner = thisDatabase.getUserFromDatabase(getChatID("seller"));
        thisUser = thisDatabase.getUserFromDatabase(getChatID("buyer"));
    }

    private void addAllMessageToMessageList() {
        listAllMessages = thisDatabase.getChatMessagesFromDatabase(thisUser,chatPartner);
    }

    private void setupChatList(){
        ChatAdapter adapter = new ChatAdapter(getApplicationContext(), R.layout.adapter_chat_sender, listAllMessages,chatPartner);
        getChatList().post(() -> getChatList().setSelection(adapter.getCount() - 1));
        getChatList().setAdapter(adapter);
    }

    private void setupToolbar(){
        String user_name = chatPartner.getFirstName() + " " + chatPartner.getSecondName();
        getToolbarName().setText(user_name);
    }

    private void chatScreenControl() {
        chatControl();
        backButtonControl();
    }

    public void chatControl(){
        getSendButton().setOnClickListener(view -> updateChat());
    }

    private Button getSendButton() {
        return findViewById(R.id.send_button_chat);
    }

    private void updateChat() {
        addChatToChatList();
        setupChatList();
        getChatMessage().setText("");
    }

    private void addChatToChatList() {
        Message temp_message = new Message(getChatMessage().getText().toString());
        int chat_id = thisDatabase.getChatIDFromDatabase(thisUser,chatPartner);

        editMessage(temp_message);
        updateMessage(temp_message, chat_id);
    }

    private void editMessage(Message temp_message) {
        temp_message.setSenderID(thisUser.getUserID());
        temp_message.setTag("sender");
    }

    private void updateMessage(Message temp_message, int chat_id) {
        thisDatabase.addToMessageListQuery(chat_id, temp_message);
        listAllMessages.add(temp_message);
    }

    private void backButtonControl() {
        getBackButton().setOnClickListener(view -> decidePreviousScreen());
    }

    private void decidePreviousScreen(){
        switch (getIntent().getStringExtra("previous_screen_chat_screen")){
            case "contact_screen":
                startActivity(intentMaker.getContactScreenIntent());
                break;
            case "house_view_screen":
                Intent intent = intentMaker.getHouseViewIntent();
                intent.putExtra("item", getIntent().getIntExtra("item",0));
                switch (getIntent().getStringExtra("previous_screen_house_view")){
                    case "first_screen":
                        intent.putExtra("previous_screen_house_view", "first_screen");
                        break;
                    case "wish_list_screen":
                        intent.putExtra("previous_screen_house_view", "wish_list_screen");
                        break;
                }
                startActivity(intent);
                break;
        }
    }
}