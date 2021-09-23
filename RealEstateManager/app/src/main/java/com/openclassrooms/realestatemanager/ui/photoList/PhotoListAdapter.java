package com.openclassrooms.realestatemanager.ui.photoList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.tag.Tag;

import java.util.ArrayList;
import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListViewHolder> {

    private Context context;
    private List<Photo> data;
    private OnRowPhotoListener onRowPhotoListener;

    public PhotoListAdapter(Context context, OnRowPhotoListener onRowPhotoListener) {
        this.context = context;
        this.onRowPhotoListener = onRowPhotoListener;
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public PhotoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_photo_list, parent, false);
        PhotoListViewHolder photoListViewHolder = new PhotoListViewHolder(view);

        photoListViewHolder.image.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position = photoListViewHolder.getAdapterPosition();
                long id = data.get(position).getId();
                onRowPhotoListener.onClickRowPhoto(id);
            }
        });

        return photoListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoListViewHolder holder, int position) {
        Photo photo = data.get(position);
        holder.textView.setText(photo.getLegend());

        if ((photo.getUrl() == null) || (photo.getUrl().trim().isEmpty())) {
            // Clear picture
            Glide.with(holder.image.getContext())
                    .load("")
                    .placeholder(R.drawable.ic_house)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(holder.image);
        } else {
            //load restaurant picture
            Glide.with(holder.image.getContext())
                    .load(photo.getUrl())
                    .apply(RequestOptions.fitCenterTransform())
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<Photo> photos){
        data.clear();
        data.addAll(photos);
        this.notifyDataSetChanged();
    }
}
