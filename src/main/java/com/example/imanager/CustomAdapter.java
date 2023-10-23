package com.example.imanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList itemId, itemName, itemQuantity;

    CustomAdapter(Context context, ArrayList itemId,
                  ArrayList itemName, ArrayList itemQuantity){

        this.context = context;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;

    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        // Get the longest item name
        int maxLength = getMaxLength();

        // Pad string to the max length to make rows look uniform
        String paddedName = padStringToLength(String.valueOf(itemName.get(position)), maxLength);
        holder.itemName.setText(paddedName);
        holder.quantity.setText(String.valueOf((itemQuantity.get(position))));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass values to update activity
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(itemId.get(position)));
                intent.putExtra("itemName", String.valueOf(itemName.get(position)));
                intent.putExtra("quantity", String.valueOf(itemQuantity.get(position)));

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemId.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, quantity;
        ConstraintLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            quantity = itemView.findViewById(R.id.quantity);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

    public void updateData(ArrayList newItemId, ArrayList newItemName, ArrayList newItemQuantity) {
        this.itemId.clear();
        this.itemId.addAll(newItemId);

        this.itemName.clear();
        this.itemName.addAll(newItemName);

        this.itemQuantity.clear();
        this.itemQuantity.addAll(newItemQuantity);

        notifyDataSetChanged();
    }
    private int getMaxLength() {
        int maxLength = 0;
        for (Object name : itemName) {
            int len = String.valueOf(name).length();
            if (len > maxLength) {
                maxLength = len;
            }
        }
        return maxLength;
    }
    public static String padStringToLength(String original, int length) {
        return String.format("%-" + length + "s", original);
    }

}
