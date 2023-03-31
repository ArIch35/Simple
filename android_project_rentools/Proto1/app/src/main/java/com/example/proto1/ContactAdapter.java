package com.example.proto1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Chat> {
    private final Context this_context;
    private final int resource_id;

    static class ContactAdapterHolder{
        TextView userName;
        ImageView userImage;
        TextView lastMessage;
    }

    public ContactAdapter(Context context, int resource, List<Chat> objects) {
        super(context, resource, objects);
        this_context = context;
        resource_id=resource;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this_context);

        if(convertView == null) {
            convertView = inflater.inflate(resource_id, parent, false);

            assignReceiverHolderAttributes(initializeReceiverHolderAttributes(convertView),position);
        }

        return convertView;
    }

    private ContactAdapterHolder initializeReceiverHolderAttributes(View convertView) {
        ContactAdapterHolder holder = new ContactAdapterHolder();

        holder.userName = convertView.findViewById(R.id.username_contact_adapter);
        holder.userImage = convertView.findViewById(R.id.user_image_contact_adapter);
        holder.lastMessage = convertView.findViewById(R.id.last_message_contact_adapter);

        return holder;
    }

    private void assignReceiverHolderAttributes(ContactAdapterHolder holder,int position) {
        Chat chat = getItem(position);
        User user = chat.getChatPartner();
        String user_name_string = user.getFirstName() + " " + user.getSecondName();

        holder.userName.setText(user_name_string);
        if (chat.getLastMessage().equals("")) {
            holder.lastMessage.setText(R.string.empty);
        }
        else {
            holder.lastMessage.setText(chat.getLastMessage());
        }

        if (user.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            holder.userImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));
        }
        else {
            Bitmap bitmap = BitmapFactory.decodeResource(this_context.getResources(),R.drawable.unknown);
            holder.userImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));
        }
    }


}
