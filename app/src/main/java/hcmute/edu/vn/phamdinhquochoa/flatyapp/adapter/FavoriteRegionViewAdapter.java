package hcmute.edu.vn.phamdinhquochoa.flatyapp.adapter;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;

public class FavoriteRegionViewAdapter extends RecyclerView.Adapter<FavoriteRegionViewAdapter.RegionViewHolder> {

    private final ArrayList<Region> regions;
    private final LifecycleOwner lifecycleOwner;

    private Consumer<Region> regionClickedListener = r -> {};
    private OnRegionDeletedListener deleteButtonClickedListener = (r, c) -> {};

    public FavoriteRegionViewAdapter(LifecycleOwner lifecycleOwner) {
        this(new ArrayList<>(), lifecycleOwner);
    }

    public FavoriteRegionViewAdapter(ArrayList<Region> regions, LifecycleOwner lifecycleOwner) {
        this.regions = regions;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.region_saved_card, parent, false);

        return new RegionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        Region region = regions.get(position);

        holder.setName(region.getName());
        holder.setDescription(region.getAddress());
        holder.initImage(lifecycleOwner, region);

        holder.itemView.setOnClickListener(v -> {
            regionClickedListener.accept(region);
        });
        holder.getButton().setOnClickListener(v -> {
            deleteButtonClickedListener.onDeleteButtonClicked(region, () -> deleteRegion(region));
        });
    }

    public void deleteRegion(Region region) {
        int index = regions.indexOf(region);

        if(index == -1) return;

        regions.remove(index);
        notifyItemRemoved(index);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRegions(Collection<Region> requests) {
        regions.clear();
        regions.addAll(requests);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return regions.size();
    }

    public void setOnRegionClickedListener(Consumer<Region> regionClickedListener) {
        this.regionClickedListener = regionClickedListener;
    }

    public void setOnRegionDeletedListener(OnRegionDeletedListener onRegionDeletedListener) {
        this.deleteButtonClickedListener = onRegionDeletedListener;
    }

    public interface OnRegionDeletedListener {

        void onDeleteButtonClicked(Region region, DeleteCallback callback);

    }

    public interface DeleteCallback {

        void delete();

    }

    public static class RegionViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView regionName;
        private final TextView description;
        private final FloatingActionButton deleteButton;

        public RegionViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.region_image);
            regionName = itemView.findViewById(R.id.region_title);
            description = itemView.findViewById(R.id.region_description);
            deleteButton = itemView.findViewById(R.id.fav_region_delete_button);
        }

        public void setImage(byte[] bytes) {
            Bitmap bitmap = ImageUtils.convertByteArrayToBitmap(bytes);
            imageView.setImageBitmap(bitmap);
        }

        public void setName(String name) {
            regionName.setText(name);
        }

        public void setDescription(String requestText) {
            description.setText(requestText);
        }

        public FloatingActionButton getButton() {
            return deleteButton;
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
