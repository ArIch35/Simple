package com.example.proto1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.viewHolder> {
    private final List<byte[]> imageHouse;
    private ImageView imageView;
    private final ClickListener listener;

    static class viewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        viewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.item_swipe_image_adapter);
        }
    }

    public ImageAdapter(ArrayList<byte[]> urls,ClickListener listener) {
        this.imageHouse = urls;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_swipe_images, parent, false);
        viewHolder temp = new viewHolder(itemView);
        imageView = temp.image;
        return temp;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        byte[] image_url = imageHouse.get(position);

        Bitmap bitmap = BitmapFactory.decodeByteArray(image_url, 0 , image_url.length);

        imageView.setImageBitmap(bitmap);

        imageView.setOnClickListener(view -> listener.imageControl(imageHouse.get(holder.getAdapterPosition())));
    }



    @Override
    public int getItemCount() {
        return imageHouse.size();
    }
}
