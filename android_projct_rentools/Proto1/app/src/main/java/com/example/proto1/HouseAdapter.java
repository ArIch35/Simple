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

public class HouseAdapter extends ArrayAdapter<House> {
    private final Context this_context;
    private final int resource_id;

    static class HouseListHolder {
        TextView city;
        TextView price;
        TextView address;
        TextView size;
        ImageView image;
    }

    public HouseAdapter(Context context, int resource, List<House> objects) {
        super(context, resource, objects);
        this_context = context;
        resource_id=resource;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HouseListHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this_context);

            convertView = inflater.inflate(resource_id, parent, false);
            holder = initializeHolderAttributes(convertView);

            convertView.setTag(holder);
        }

        else {
            holder = (HouseListHolder) convertView.getTag();
        }

        assignHolderAttributes(holder,position);
        return convertView;
    }

    private void assignHolderAttributes(HouseListHolder holder, int position) {
        House temp_item = getItem(position);
        String price_text = temp_item.getHousePrice() + " €/" + this_context.getResources().getText(R.string.month) ;
        String size_text = temp_item.getHouseSize() + " m²";

        holder.city.setText(temp_item.getCity());
        holder.price.setText(price_text);
        holder.address.setText(temp_item.getHouseAddress());
        holder.size.setText(size_text);

        Bitmap bitmap = BitmapFactory.decodeByteArray(temp_item.getHouseImage().get(0), 0 , temp_item.getHouseImage().get(0).length);

        if (bitmap != null)
            holder.image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));
    }

    private HouseListHolder initializeHolderAttributes(View convertView) {

        HouseListHolder item_holder = new HouseListHolder();

        item_holder.city = convertView.findViewById(R.id.city_house_adapter);
        item_holder.price = convertView.findViewById(R.id.price_house_adapter);
        item_holder.address = convertView.findViewById(R.id.address_house_adapte);
        item_holder.size = convertView.findViewById(R.id.size_house_adapter);
        item_holder.image = convertView.findViewById(R.id.image_house_view_adapter);

        return item_holder;
    }


}
