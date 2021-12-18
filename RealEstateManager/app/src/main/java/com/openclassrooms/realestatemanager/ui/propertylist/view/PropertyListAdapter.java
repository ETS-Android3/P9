package com.openclassrooms.realestatemanager.ui.propertylist.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ui.propertylist.listener.OnRowPropertyClickListener;
import com.openclassrooms.realestatemanager.ui.propertylist.viewstate.RowPropertyViewState;

import java.util.ArrayList;
import java.util.List;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListViewHolder>{

    private final List<RowPropertyViewState> data;
    private final OnRowPropertyClickListener onRowPropertyClickListener;

    public PropertyListAdapter(OnRowPropertyClickListener onRowPropertyClickListener) {
        this.onRowPropertyClickListener = onRowPropertyClickListener;
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public PropertyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_property_list, parent, false);
        PropertyListViewHolder propertyListViewHolder = new PropertyListViewHolder(view);

        propertyListViewHolder.cardView.setOnClickListener(v -> {
            int position = propertyListViewHolder.getAbsoluteAdapterPosition();
            long id = data.get(position).getId();
            onRowPropertyClickListener.onClickRowProperty(id);
        });

        return propertyListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyListViewHolder holder, int position) {
        RowPropertyViewState rowPropertyViewState = data.get(position);
        holder.textViewType.setText(rowPropertyViewState.getType());
        holder.textViewAddress.setText(rowPropertyViewState.getAddress());
        holder.textViewPrice.setText(rowPropertyViewState.getPrice());

        if ((rowPropertyViewState.getUrl() == null) || (rowPropertyViewState.getUrl().trim().isEmpty())) {
            // Clear picture
            Glide.with(holder.image.getContext())
                    .load("")
                    .placeholder(R.drawable.ic_house)
                    .into(holder.image);
        } else {
            //load picture
            Glide.with(holder.image.getContext())
                    .load(rowPropertyViewState.getUrl())
                    .error(R.drawable.ic_house)
                    //.apply(RequestOptions.fitCenterTransform())
                    .centerCrop()
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<RowPropertyViewState> rowPropertyViewStates) {
        data.clear();
        data.addAll(rowPropertyViewStates);
        this.notifyDataSetChanged();
    }
}
