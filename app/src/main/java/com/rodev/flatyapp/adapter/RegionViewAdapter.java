package com.rodev.flatyapp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.rodev.flatyapp.beans.Region;
import com.rodev.flatyapp.dao.DataAccess;
import com.rodev.flatyapp.utils.ImageUtils;

public class RegionViewAdapter extends RecyclerView.Adapter<RegionViewAdapter.RegionViewHolder> {

    private final ArrayList<RegionWrapper> regions;
    private final LifecycleOwner lifecycleOwner;

    private Consumer<Region> regionClickedListener = r -> {};
    private Consumer<Region> saveButtonClickedListener = r -> {};

    private Predicate<Region> showRegionsPredicate = r -> true;

    private boolean matchPredicateRequested = false;

    public RegionViewAdapter(LifecycleOwner lifecycleOwner) {
        this(new ArrayList<>(), lifecycleOwner);
    }

    public RegionViewAdapter(ArrayList<RegionWrapper> regions, LifecycleOwner lifecycleOwner) {
        this.regions = regions;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.region_card, parent, false);

        return new RegionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        RegionWrapper regionWrapper = regions.get(position);

        Region region = regionWrapper.region;
        regionWrapper.view = holder.itemView;

        holder.setName(region.getName());
        holder.setDescription(region.getAddress());
        holder.initImage(lifecycleOwner, region);

        holder.itemView.setOnClickListener(v -> {
            regionClickedListener.accept(region);
        });
        holder.getButton().setOnClickListener(v -> {
            saveButtonClickedListener.accept(region);
        });

        if(matchPredicateRequested) {
            regionWrapper.onSearch(showRegionsPredicate);
        }

        if(position == regions.size() - 1) {
            matchPredicateRequested = false;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRegions(Collection<Region> newRegions) {
        regions.clear();
        regions.addAll(transform(newRegions));

        notifyDataSetChanged();

        requestMatchPredicate();
    }

    private void requestMatchPredicate() {
        matchPredicateRequested = true;
    }

    private Collection<RegionWrapper> transform(Collection<Region> regions) {
        return regions.stream()
                .map(RegionWrapper::new)
                .collect(Collectors.toList());
    }

    public void setShowRegionsPredicate(Predicate<Region> showRegionPredicate) {
        this.showRegionsPredicate = showRegionPredicate;
        showRegionsMatchingPredicate();
    }

    private void showRegionsMatchingPredicate() {
        regions.forEach(r -> r.onSearch(showRegionsPredicate));
    }

    @Override
    public int getItemCount() {
        return regions.size();
    }

    public void setOnRegionClickedListener(Consumer<Region> regionClickedListener) {
        this.regionClickedListener = regionClickedListener;
    }

    public void setOnRegionSaveButtonClickedListener(Consumer<Region> onSaveButtonClicked) {
        this.saveButtonClickedListener = onSaveButtonClicked;
    }

    public static class RegionWrapper {

        private final Region region;
        private View view;

        public RegionWrapper(Region region) {
            this.region = region;
        }

        public Region getRegion() {
            return region;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public void onSearch(Predicate<Region> showPredicate) {
            if(showPredicate.test(region)) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public static class RegionViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView regionName;
        private final TextView description;
        private final Button saveButton;

        public RegionViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.region_card_image);
            regionName = itemView.findViewById(R.id.region_title);
            description = itemView.findViewById(R.id.region_description);
            saveButton = itemView.findViewById(R.id.save_region_button);
        }

        public void setImage(byte[] bytes) {
            Bitmap bitmap = ImageUtils.convertByteArrayToBitmap(bytes);
            imageView.setImageBitmap(bitmap);
        }

        public void setName(String phoneNumber) {
            regionName.setText(phoneNumber);
        }

        public void setDescription(String requestText) {
            description.setText(requestText);
        }

        public Button getButton() {
            return saveButton;
        }

        private void initImage(LifecycleOwner lifecycleOwner, Region region) {
            byte[] image = region.getImage();

            if(image == null) {
                DataAccess.getDataService()
                        .getImageStorage()
                        .getImageByUri(region.getId())
                        .observe(lifecycleOwner, this::setImage);
                return;
            }

            setImage(image);
        }
    }

}
