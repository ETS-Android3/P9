package com.openclassrooms.realestatemanager.ui.photoList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.tag.Tag;

import java.util.ArrayList;
import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListViewHolder> {

    private final List<Photo> data;
    private final OnRowPhotoListener onRowPhotoListener;

    public PhotoListAdapter(OnRowPhotoListener onRowPhotoListener) {
        this.onRowPhotoListener = onRowPhotoListener;
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public PhotoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_photo_list, parent, false);
        PhotoListViewHolder photoListViewHolder = new PhotoListViewHolder(view);

        if (onRowPhotoListener != null) {
            photoListViewHolder.image.setOnClickListener(v -> {
                int position = photoListViewHolder.getAbsoluteAdapterPosition();
                Photo photo = data.get(position);
                onRowPhotoListener.onClickRowPhoto(photo);
            });
            photoListViewHolder.image.setOnLongClickListener(v -> {
                Log.d(Tag.TAG, "PhotoListViewHolder.onLongClick() called with: v = [" + v + "]");
                int position = photoListViewHolder.getAbsoluteAdapterPosition();
                Photo photo = data.get(position);
                onRowPhotoListener.onLongClickRowPhoto(v, photo);
                // return true do not display menu
                // return false will display menu with onCreateContextMenu
                return false;
            });
        }

        return photoListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoListViewHolder holder, int position) {
        Photo photo = data.get(position);
        if (photo != null) {
            if (photo.getLegend().isEmpty()) {
                holder.textView.setText(R.string.caption_required);
                holder.textView.setTextColor(Color.RED);
            } else {
                holder.textView.setText(photo.getLegend());
                holder.textView.setTextColor(holder.textView.getResources().getColor(R.color.primaryColor));
            }
        }

        if ((photo.getUrl() == null) || (photo.getUrl().trim().isEmpty())) {
            // Clear picture
            Glide.with(holder.image.getContext())
                    .load("")
                    .placeholder(R.drawable.ic_house)
                     .into(holder.image);
        } else {
            //load restaurant picture
            Glide.with(holder.image.getContext())
                    .load(photo.getUrl())
                    .centerCrop()
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Photo> photos){
        data.clear();
        data.addAll(photos);
        this.notifyDataSetChanged();
    }
}
