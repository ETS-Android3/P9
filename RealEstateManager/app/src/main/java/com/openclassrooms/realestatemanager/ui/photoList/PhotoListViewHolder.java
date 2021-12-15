package com.openclassrooms.realestatemanager.ui.photoList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;

public class PhotoListViewHolder extends RecyclerView.ViewHolder{

    ImageView image;
    TextView textView;

    public PhotoListViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.row_photo_image_view);
        textView = itemView.findViewById(R.id.row_photo_text_view);
    }
}
