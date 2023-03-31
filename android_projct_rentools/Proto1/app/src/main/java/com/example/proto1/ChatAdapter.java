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

public class ChatAdapter extends ArrayAdapter<Message> {
    private final Context this_context;
    private int resource_id;
    private final User chatPartner;

    private static class ChatReceiverHolder {
        TextView chatHolderReceiver;
        TextView timeHolderReceiver;
        ImageView partnerImage;
    }

    private static class ChatSenderHolder {
        TextView chatHolderSender;
        TextView timeHolderSender;
    }

    public ChatAdapter(Context context, int resource, List<Message> objects, User partner) {
        super(context, resource, objects);
        resource_id = resource;
        this_context = context;
        chatPartner = partner;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this_context);

        Message this_message = getItem(position);
        if(this_message.getTag().equals("sender")) {
            convertView = convertViewSender(parent, inflater, this_message);
        }
        else {
            convertView = convertViewReceiver(parent, inflater, this_message);
        }
        return convertView;
    }

    @NonNull
    private View convertViewReceiver(@NonNull ViewGroup parent, LayoutInflater inflater, Message this_message) {
        @Nullable View convertView;
        resource_id = R.layout.adapter_chat_reciever;
        convertView = inflater.inflate(resource_id, parent, false);

        assignReceiverHolderAttributes(initializeReceiverHolderAttributes(convertView),this_message);
        return convertView;
    }

    private ChatReceiverHolder initializeReceiverHolderAttributes(View convertView) {
        ChatReceiverHolder holder = new ChatReceiverHolder();

        holder.chatHolderReceiver = convertView.findViewById(R.id.message_receiver);
        holder.timeHolderReceiver = convertView.findViewById(R.id.time_holder_receiver);
        holder.partnerImage = convertView.findViewById(R.id.user_image_receiver);

        return holder;
    }

    private void assignReceiverHolderAttributes(ChatReceiverHolder holder, Message message) {

        holder.chatHolderReceiver.setText(message.getMessage());
        holder.timeHolderReceiver.setText(message.getCurrentTime());

        if (chatPartner.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(chatPartner.getImage(), 0, chatPartner.getImage().length);
            holder.partnerImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));
        }
        else {
            Bitmap bitmap = BitmapFactory.decodeResource(this_context.getResources(),R.drawable.unknown);
            holder.partnerImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));
        }
    }

    @NonNull
    private View convertViewSender(@NonNull ViewGroup parent, LayoutInflater inflater, Message this_message) {
        @Nullable View convertView;
        resource_id = R.layout.adapter_chat_sender;
        convertView = inflater.inflate(resource_id, parent, false);

        assignSenderHolderAttributes(initializeSenderHolderAttributes(convertView),this_message);
        return convertView;
    }

    private ChatSenderHolder initializeSenderHolderAttributes(View convertView) {
        ChatSenderHolder holder = new ChatSenderHolder();

        holder.chatHolderSender = convertView.findViewById(R.id.message_sender);
        holder.timeHolderSender = convertView.findViewById(R.id.time_holder_sender);

        return holder;
    }

    private void assignSenderHolderAttributes(ChatSenderHolder holder, Message message) {
        holder.chatHolderSender.setText(message.getMessage());
        holder.timeHolderSender.setText(message.getCurrentTime());
    }
}
