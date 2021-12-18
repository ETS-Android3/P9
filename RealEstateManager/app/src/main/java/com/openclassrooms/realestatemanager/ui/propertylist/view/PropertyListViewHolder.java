package com.openclassrooms.realestatemanager.ui.propertylist.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;

public class PropertyListViewHolder extends RecyclerView.ViewHolder {

    final CardView cardView;
    final ImageView image;
    final TextView textViewType;
    final TextView textViewAddress;
    final TextView textViewPrice;

    public PropertyListViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.row_property_list_card_view);
        image = itemView.findViewById(R.id.row_property_list_picture);
        textViewType = itemView.findViewById(R.id.row_property_list_type);
        textViewAddress = itemView.findViewById(R.id.row_property_list_address);
        textViewPrice = itemView.findViewById(R.id.row_property_list_price) ;
    }
}
