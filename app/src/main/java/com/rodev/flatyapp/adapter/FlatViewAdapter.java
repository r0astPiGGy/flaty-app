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

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.rodev.flatyapp.R;
import com.rodev.flatyapp.beans.Flat;
import com.rodev.flatyapp.dao.DataAccess;
import com.rodev.flatyapp.utils.ImageUtils;

public class FlatViewAdapter extends RecyclerView.Adapter<FlatViewAdapter.FlatViewHolder> {

    private final ArrayList<FlatWrapper> flats;
    private final LifecycleOwner lifecycleOwner;

    private Consumer<Flat> flatClickedListener = f -> {};
    private Predicate<Flat> showFlatsPredicate = f -> true;

    private boolean matchPredicateRequested = false;

    public FlatViewAdapter(LifecycleOwner lifecycleOwner) {
        this(new ArrayList<>(), lifecycleOwner);
    }

    public FlatViewAdapter(ArrayList<FlatWrapper> flats, LifecycleOwner lifecycleOwner) {
        this.flats = flats;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public FlatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.flat_card, parent, false);

        return new FlatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlatViewHolder holder, int position) {
        FlatWrapper flatWrapper = flats.get(position);

        Flat flat = flatWrapper.flat;

        flatWrapper.view = holder.itemView;

        holder.setName(flat.getName());
        holder.setPrice(flat.getPrice());
        holder.setRegion(flat.getRegionReference() != null ? flat.getRegionReference().getName() : null);
        holder.initImage(lifecycleOwner, flat);

        holder.itemView.setOnClickListener(v -> {
            flatClickedListener.accept(flat);
        });

        if(matchPredicateRequested) {
            flatWrapper.onSearch(showFlatsPredicate);
        }

        if(position == flats.size() - 1) {
            matchPredicateRequested = false;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFlats(Collection<Flat> newFlats) {
        flats.clear();
        flats.addAll(transform(newFlats));

        notifyDataSetChanged();

        requestMatchPredicate();
    }

    private void requestMatchPredicate() {
        matchPredicateRequested = true;
    }

    private Collection<FlatWrapper> transform(Collection<Flat> flats) {
        return flats.stream()
                .map(FlatWrapper::new)
                .collect(Collectors.toList());
    }

    public void setShowFlatsPredicate(Predicate<Flat> showFlatsPredicate) {
        this.showFlatsPredicate = showFlatsPredicate;
        showFlatsMatchingPredicate();
    }

    private void showFlatsMatchingPredicate() {
        flats.forEach(r -> r.onSearch(showFlatsPredicate));
    }

    @Override
    public int getItemCount() {
        return flats.size();
    }

    public void setOnFlatClickedListener(Consumer<Flat> flatClickedListener) {
        this.flatClickedListener = flatClickedListener;
    }

    public static class FlatWrapper {

        private final Flat flat;
        private View view;

        public FlatWrapper(Flat flat) {
            this.flat = flat;
        }

        public Flat getFlat() {
            return flat;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public void onSearch(Predicate<Flat> showPredicate) {
            if(showPredicate.test(flat)) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public static class FlatViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView flatName;
        private final TextView price;
        private final TextView region;

        public FlatViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.flat_image);
            flatName = itemView.findViewById(R.id.flat_title);
            price = itemView.findViewById(R.id.flat_price);
            region = itemView.findViewById(R.id.flat_region);
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
