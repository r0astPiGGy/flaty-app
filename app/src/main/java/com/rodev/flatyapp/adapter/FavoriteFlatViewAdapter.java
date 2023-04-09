package com.rodev.flatyapp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import com.rodev.flatyapp.R;
import com.rodev.flatyapp.beans.Flat;
import com.rodev.flatyapp.dao.DataAccess;
import com.rodev.flatyapp.utils.ImageUtils;

public class FavoriteFlatViewAdapter extends RecyclerView.Adapter<FavoriteFlatViewAdapter.FlatViewHolder> {

    private final ArrayList<Flat> flats;
    private final LifecycleOwner lifecycleOwner;

    private Consumer<Flat> flatClickedListener = r -> {};
    private OnFlatDeletedListener deleteButtonClickedListener = (r, c) -> {};

    public FavoriteFlatViewAdapter(LifecycleOwner lifecycleOwner) {
        this(new ArrayList<>(), lifecycleOwner);
    }

    public FavoriteFlatViewAdapter(ArrayList<Flat> flats, LifecycleOwner lifecycleOwner) {
        this.flats = flats;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public FlatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.flat_saved_card, parent, false);

        return new FlatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlatViewHolder holder, int position) {
        Flat flat = flats.get(position);

        holder.setName(flat.getName());
        holder.setPrice(flat.getPrice());
        holder.setRegion(flat.getRegionReference() != null ? flat.getRegionReference().getName() : null);
        holder.initImage(lifecycleOwner, flat);

        holder.itemView.setOnClickListener(v -> {
            flatClickedListener.accept(flat);
        });
        holder.getButton().setOnClickListener(v -> {
            deleteButtonClickedListener.onDeleteButtonClicked(flat, () -> deleteFlat(flat));
        });
    }

    public void deleteFlat(Flat flat) {
        int index = flats.indexOf(flat);

        if(index == -1) return;

        flats.remove(index);
        notifyItemRemoved(index);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFlats(Collection<Flat> requests) {
        flats.clear();
        flats.addAll(requests);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return flats.size();
    }

    public void setOnFlatClickedListener(Consumer<Flat> regionClickedListener) {
        this.flatClickedListener = regionClickedListener;
    }

    public void setOnFlatDeletedListener(OnFlatDeletedListener onFlatDeletedListener) {
        this.deleteButtonClickedListener = onFlatDeletedListener;
    }

    public interface OnFlatDeletedListener {

        void onDeleteButtonClicked(Flat flat, DeleteCallback callback);

    }

    public interface DeleteCallback {

        void delete();

    }

    public static class FlatViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView flatName;
        private final TextView price;
        private final TextView region;
        private final FloatingActionButton deleteButton;

        public FlatViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.flat_image);
            flatName = itemView.findViewById(R.id.flat_title);
            price = itemView.findViewById(R.id.flat_price);
            region = itemView.findViewById(R.id.flat_region);
            deleteButton = itemView.findViewById(R.id.fav_flat_delete_button);
        }

        public void setImage(byte[] bytes) {
            Bitmap bitmap = ImageUtils.convertByteArrayToBitmap(bytes);
            imageView.setImageBitmap(bitmap);
        }

        public void setName(String phoneNumber) {
            flatName.setText(phoneNumber);
        }

        public void setPrice(Double price) {
            if(price == null) return;

            long rounded = Math.round(price);

            this.price.setText(String.valueOf(rounded));
        }

        public void setRegion(String region) {
            this.region.setText(region);
        }

        public FloatingActionButton getButton() {
            return deleteButton;
        }

        private void initImage(LifecycleOwner lifecycleOwner, Flat flat) {
            byte[] image = flat.getImage();

            if(image == null) {
                DataAccess.getDataService()
                        .getImageStorage()
                        .getImageByUri(flat.getId())
                        .observe(lifecycleOwner, this::setImage);
                return;
            }

            setImage(image);
        }
    }

}
