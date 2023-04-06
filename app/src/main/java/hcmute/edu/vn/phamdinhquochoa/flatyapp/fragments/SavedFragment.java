package hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Objects;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.CategoryActivity;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.FlatDetailsActivity;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FlatSize;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.components.FlatSavedCard;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.components.RegionCard;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;

public class SavedFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static LinearLayout saved_container;
    private LinearLayout btn_saved_Flat, btn_saved_Region;
    private TextView tv_saved_Flat, tv_saved_Region;

    public SavedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_saved, container, false);

        saved_container = mainView.findViewById(R.id.layout_saved);

        btn_saved_Flat = mainView.findViewById(R.id.btn_saved_Flat);
        tv_saved_Flat = mainView.findViewById(R.id.tv_saved_Flat);
        btn_saved_Region = mainView.findViewById(R.id.btn_saved_Region);
        tv_saved_Region = mainView.findViewById(R.id.tv_saved_Region);

        btn_saved_Flat.setOnClickListener(view -> {
            btn_saved_Flat.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.color.teal_700));
            tv_saved_Flat.setTextColor(Color.WHITE);
            btn_saved_Region.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.bg_white));
            tv_saved_Region.setTextColor(Color.BLACK);

            loadFavoriteFlats();
        });

        btn_saved_Region.setOnClickListener(view -> {
            btn_saved_Flat.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.bg_white));
            tv_saved_Flat.setTextColor(Color.BLACK);
            btn_saved_Region.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.color.teal_700));
            tv_saved_Region.setTextColor(Color.WHITE);

            loadFavoriteRegions();
        });

        loadFavoriteFlats();

        return mainView;
    }

    private void loadFavoriteFlats() {
        DataAccess.getDataService().getFavoriteFlatData().getFavoriteFlats().observe(this, this::onFavoriteFlatLoaded);
    }

    private void onFavoriteFlatLoaded(List<Flat> flats) {
        flats.forEach(this::addFlatCard);
    }

    private void addFlatCard(Flat flat) {
        // TODO
        FlatSize flatSize = new FlatSize(100, 2, 3d);

        FlatSavedCard savedCard = new FlatSavedCard(getContext(), flat, flat.getRegionReference().getName(), flatSize);
        savedCard.setOnClickListener(view -> {
            FlatDetailsActivity.FlatSize = flatSize;
            Intent intent = new Intent(getContext(), FlatDetailsActivity.class);
            intent.putExtra("Flat", flat);
            try {
                startActivity(intent);
            } catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        saved_container.addView(savedCard);
    }

    private List<Region> cachedSavedRegions;

    private void loadFavoriteRegions() {
        DataAccess.getDataService()
                .getFavoriteRegionData()
                .getFavoriteRegions()
                .observe(this, this::onRegionsLoaded);
    }

    private void onRegionsLoaded(List<Region> regions) {
        regions.forEach(this::addRegionCard);
    }

    private void addRegionCard(Region region) {
        RegionCard card = new RegionCard(getContext(), region, true);
        card.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CategoryActivity.class);
            // TODO : put Serializable
            intent.putExtra("RegionId", region.getId());
            startActivity(intent);
        });
        saved_container.addView(card);
    }
}