package com.example.proto1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import kotlin.Pair;

public class PersonalScreen extends AbstractScreen{
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private User thisUser = new User();
    private attributeHolder holder;

    private View getMainLayout() {
        return findViewById(R.id.layout_personal);
    }

    static class attributeHolder{
        TextView screenName;
        PopUpWindow changePassWindow;
        PopUpWindow imageWindow;
        PopUpWindow chooseWindow;
        EditText name;
        EditText age;
        AutoCompleteTextView gender;
        EditText telephone;
        ImageView userImage;
        ImageView cameraIcon;
        Button save;
        ImageView backButton;
        Button changePass;
    }

    private attributeHolder initializeHolder(){
        attributeHolder holder = new attributeHolder();
        holder.screenName = findViewById(R.id.name_generic_screen);
        holder.name = findViewById(R.id.user_name_personal);
        holder.age = findViewById(R.id.age_personal);
        holder.gender = findViewById(R.id.gender_personal);
        holder.telephone = findViewById(R.id.telephone_personal);
        holder.userImage = findViewById(R.id.user_image_personal);
        holder.cameraIcon = findViewById(R.id.icon_camera_personal);
        holder.save = findViewById(R.id.button_save_personal);
        holder.backButton = findViewById(R.id.back_arrow_all_screen);
        holder.changePass = findViewById(R.id.button_change_pass_personal);
        holder.changePassWindow = new PopUpWindow(R.layout.popup_change_password,this,false);
        holder.imageWindow = new PopUpWindow(R.layout.popup_image,this,true);
        holder.chooseWindow = new PopUpWindow(R.layout.popup_choose_img_source,this,false);
        return holder;
    }

    private void setUser() {
        thisUser = thisDatabase.getUserFromDatabase(spMaker.getUserEmail());
    }

    @Override
    protected void screenInterface() {
        setContentView(R.layout.activity_personal_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        personalScreenSetup();
        personalScreenControl();
    }

    private void personalScreenSetup() {
        setUser();
        holderSetup();
        setupGenderDropDown();

    }

    private void holderSetup(){
        holder = initializeHolder();
        holder.screenName.setText(getStringValueFromRes(R.string.profile));
        userImageSetup();
        userInfoSetup();
    }

    private void userImageSetup() {
        holder.userImage.setImageBitmap(getUserBitmap());
        holder.cameraIcon.setImageResource(R.drawable.dslr_camera);
    }

    private Bitmap getUserBitmap() {
        if (thisUser.getImage() != null) {
            return BitmapFactory.decodeByteArray(thisUser.getImage(), 0, thisUser.getImage().length);
        }
        return BitmapFactory.decodeResource(this.getResources(),R.drawable.unknown);
    }

    private void userInfoSetup() {
        setupUserName();
        setupAge();
        setupGender();
        setupTelephone();
    }

    public void setupGender(){
        if (thisUser.getUserGender() == null){
            return;
        }
        holder.gender.setText(thisUser.getUserGender());
    }

    private void setupUserName() {
        String user_name = thisUser.getFirstName() + " " + thisUser.getSecondName();
        holder.name.setText(user_name);
    }

    private void setupAge() {
        if (thisUser.getUserAge() == 0)
            return;
        holder.age.setText(String.valueOf(thisUser.getUserAge()));
    }

    private void setupTelephone() {
        if (thisUser.getUserTelephone() == 0)
            return;
        holder.telephone.setText(String.valueOf(thisUser.getUserTelephone()));
    }

    private void personalScreenControl(){
        userImageClickedControl();
        cameraIconClickedControl();
        backButtonControl();
        changePasswordButton();
        saveUserChangeControl();
    }

    private void setupGenderDropDown() {
        holder.gender.setOnFocusChangeListener((v, hasFocus) -> {
            ArrayList<String> gender = new ArrayList<>(Arrays.asList("Male","Female","Other"));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.drop_down_material,gender);
            holder.gender.setAdapter(adapter);
        });
    }

    private void userImageClickedControl(){
        holder.userImage.setOnClickListener(v -> controlPopUpImage());
    }

    private void controlPopUpImage(){
        holder.imageWindow.show();
        setUserImagePopUp();
        holder.imageWindow.getImageView(R.id.back_button_popup_image).setOnClickListener(view -> holder.imageWindow.close());
    }

    private void setUserImagePopUp(){
        Bitmap bitmap = BitmapFactory.decodeByteArray(thisUser.getImage(), 0 , thisUser.getImage().length);
        holder.imageWindow.getImageView(R.id.popup_image).setImageBitmap(bitmap);
    }

    private void cameraIconClickedControl(){
        holder.cameraIcon.setOnClickListener(v -> controlPopUpChoose());
    }

    private void controlPopUpChoose(){
        holder.chooseWindow.show();
        galleryButtonCLicked();
        cameraButtonCLicked();
    }

    private void galleryButtonCLicked(){
        holder.chooseWindow.getButton(R.id.gallery_button_popup_choose).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            holder.chooseWindow.close();
            startActivityForResult(intent, 0);
        });
    }

    private void cameraButtonCLicked(){
        holder.chooseWindow.getButton(R.id.camera_button_popup_choose).setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
            else
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                holder.chooseWindow.close();                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {
            try {
                assert data != null;
                Bitmap resized = getNewImageCamera((Bitmap) data.getExtras().get("data"));

                updateUserImage(resized);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return;
        }

        if (resultCode == RESULT_OK){
            assert data != null;
            Uri targetUri = data.getData();
            try {
                Bitmap resized = getNewImageGallery(targetUri);
                updateUserImage(resized);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (MY_CAMERA_PERMISSION_CODE == requestCode){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }

    }

    private void updateUserImage(Bitmap resized) {
        holder.userImage.setImageBitmap(resized);
        thisDatabase.updateUserImage(thisUser.getUserID(),getBytes(resized));
        thisUser.setUserImage(getBytes(resized));
    }

    private Bitmap getNewImageGallery(Uri targetUri) throws FileNotFoundException {
        return getNewImageCamera(BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri)));
    }

    private Bitmap getNewImageCamera(Bitmap bitmap) throws FileNotFoundException {
        Pair<Integer,Integer> ratio = getAspectRatio(bitmap);
        return Bitmap.createScaledBitmap(bitmap,ratio.getFirst(),ratio.getSecond(),false);
    }

    private Pair<Integer,Integer> getAspectRatio(Bitmap bitmap){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        while (height > 1500)
            height/=2;
        while (width > 1500)
            width/=2;
        return new Pair<>(width,height);
    }

    @NonNull
    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void backButtonControl(){
        holder.backButton.setOnClickListener(v -> startActivity(intentMaker.getFirstScreenIntent()));
    }

    private void changePasswordButton(){
        holder.changePass.setOnClickListener(v -> changeButtonControl());
    }

    private void changeButtonControl(){
        holder.changePassWindow.show();
        savePasswordButtonControl();
        notSavePasswordButtonControl();
    }

    private void savePasswordButtonControl(){
        holder.changePassWindow.getButton(R.id.button_accept_change_pass_popup).setOnClickListener(v -> savePasswordCheck());
    }

    private void savePasswordCheck(){
        TextView old_pass = holder.changePassWindow.getTextView(R.id.old_pass_change_pass_popup);
        TextView new_pass = holder.changePassWindow.getTextView(R.id.new_pass_change_pass_popup);
        TextView confirm_pass = holder.changePassWindow.getTextView(R.id.confirm_pass_change_pass_popup);
        Email user_email = thisDatabase.getEmailFromDatabase(thisUser.getUserEmail().getEmailAddress());

        if (!old_pass.getText().toString().equals(user_email.getEmailPassword())){
            generateToast(getStringValueFromRes(R.string.pass_false));
            old_pass.setText("");
            return;
        }
        if (new_pass.getText().toString().length() < 8 ){
            generateToast(getStringValueFromRes(R.string.pass_lt8));
            new_pass.setText("");
            return;
        }
        if (old_pass.getText().toString().equals(new_pass.getText().toString())){
            generateToast(getStringValueFromRes(R.string.pass_like_old));
            new_pass.setText("");
            return;
        }
        if (!new_pass.getText().toString().equals(confirm_pass.getText().toString())){
            generateToast(getStringValueFromRes(R.string.pass_not_like_confirm));
            confirm_pass.setText("");
            return;
        }
        thisDatabase.updateUserPassword(user_email.getEmailAddress(),confirm_pass.getText().toString());
        updateSharedPreference(confirm_pass.getText().toString());
        generateToast(getStringValueFromRes(R.string.success));
        holder.changePassWindow.close();
    }

    private void updateSharedPreference(String pass){
        SharedPreferences.Editor sp_editor = spMaker.getSPEditor();
        sp_editor.putString("pass",pass);
        sp_editor.apply();
    }

    private void notSavePasswordButtonControl(){
        holder.changePassWindow.getButton(R.id.button_cancel_change_pass_popup).setOnClickListener(v -> holder.changePassWindow.close());
    }

    private void saveUserChangeControl() {
        holder.save.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges(){
        saveUserName(holder.name.getText().toString());
        saveUserAge(holder.age.getText().toString());
        saveUserGender(holder.gender.getText().toString());
        saveUserTelephone(holder.telephone.getText().toString());
        thisDatabase.updateUserInfo(thisUser);
    }

    private void saveUserName(String name){
        String[] temp = name.split(" ");
        String first_name = temp[0];
        StringBuilder second_name = new StringBuilder(temp[1]);

        for (int i = 2; i<temp.length;i++){
            second_name.append(" ").append(temp[i]);
        }
        thisUser.setFirstName(first_name);
        thisUser.setSecondName(second_name.toString());
    }

    private void saveUserAge(String age_string){
        if (age_string.equals("")){
            thisUser.setUserAge(0);
            return;
        }
        try {
            Integer.valueOf(age_string);
        }
        catch (Exception e){
            generateToast(getStringValueFromRes(R.string.no_letter));
            return;
        }
        thisUser.setUserAge(Integer.parseInt(age_string));
    }

    private void saveUserGender(String gender){
        if (gender.equals("")){
            thisUser.setUserGender(null);
            return;
        }
        thisUser.setUserGender(gender);
    }

    private void saveUserTelephone(String telephone_string){
        if (telephone_string.equals("")){
            thisUser.setUserTelephone(0);
            return;
        }
        try {
            Integer.valueOf(telephone_string);
        }
        catch (Exception e){
            generateToast(getStringValueFromRes(R.string.no_letter));
            return;
        }
        thisUser.setUserTelephone(Integer.parseInt(telephone_string));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("changePassOpen", holder.changePassWindow.isShowing());
        outState.putBoolean("imageOpen", holder.imageWindow.isShowing());
        outState.putBoolean("chooseOpen", holder.chooseWindow.isShowing());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean("changePassOpen")){
            getMainLayout().post(this::changeButtonControl);
        }
        if (savedInstanceState.getBoolean("imageOpen")){
            getMainLayout().post(this::controlPopUpImage);
        }
        if (savedInstanceState.getBoolean("chooseOpen")){
            getMainLayout().post(this::controlPopUpChoose);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}