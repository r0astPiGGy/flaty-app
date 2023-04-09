package hcmute.edu.vn.phamdinhquochoa.flatyapp.adapter;

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

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;

public class FlatViewAdapter extends RecyclerView.Adapter<FlatViewAdapter.FlatViewHolder> {

    private final ArrayList<Flat> flats;
    private final LifecycleOwner lifecycleOwner;

    private Consumer<Flat> flatClickedListener = r -> {};

    public FlatViewAdapter(LifecycleOwner lifecycleOwner) {
        this(new ArrayList<>(), lifecycleOwner);
    }

    public FlatViewAdapter(ArrayList<Flat> flats, LifecycleOwner lifecycleOwner) {
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
        Flat flat = flats.get(position);

        holder.setName(flat.getName());
        holder.setPrice(flat.getPrice());
        holder.setRegion(flat.getRegionReference() != null ? flat.getRegionReference().getName() : null);
        holder.initImage(lifecycleOwner, flat);

        holder.itemView.setOnClickListener(v -> {
            flatClickedListener.accept(flat);
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
