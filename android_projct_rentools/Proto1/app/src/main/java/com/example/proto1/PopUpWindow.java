package com.example.proto1;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopUpWindow {
    private final View view;
    private final PopupWindow window;
    private boolean isShowing = false;

    public PopUpWindow(int rID, Context context, boolean isFullscreen){
        view = inflateView(rID,context);
        window = setupPopupWindow(view,isFullscreen);
    }

    public ImageView getImageView(int imgID){
        return view.findViewById(imgID);
    }
    public Button getButton(int butID){
        return view.findViewById(butID);
    }
    public TextView getTextView(int txtID){
        return view.findViewById(txtID);
    }

    private View inflateView(int rID, Context context){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(rID, null);
    }

    private PopupWindow setupPopupWindow(View view, boolean isFullscreen) {
        int width,height;
        if (isFullscreen) {
            width = LinearLayout.LayoutParams.MATCH_PARENT;
            height = LinearLayout.LayoutParams.MATCH_PARENT;
        }
        else {
            width = LinearLayout.LayoutParams.WRAP_CONTENT;
            height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        boolean focusable = true;

        return new PopupWindow(view, width, height, focusable);
    }

   public void show(){
        isShowing = true;
        window.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void close(){
        isShowing = false;
        window.dismiss();
    }

    public boolean isShowing() {
        return isShowing;
    }
}
