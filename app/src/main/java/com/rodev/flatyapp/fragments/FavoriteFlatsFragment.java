package com.rodev.flatyapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rodev.flatyapp.FlatDetailsActivity;
import com.rodev.flatyapp.adapter.FavoriteFlatViewAdapter;
import com.rodev.flatyapp.beans.FavoriteFlat;
import com.rodev.flatyapp.beans.Flat;
import com.rodev.flatyapp.beans.Region;
import com.rodev.flatyapp.dao.DataAccess;
import com.rodev.flatyapp.databinding.FragmentFavoriteFlatsBinding;

public class FavoriteFlatsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FavoriteFlatViewAdapter adapter;
    private FragmentFavoriteFlatsBinding binding;
    private SwipeRefreshLayout refreshLayout;

    public FavoriteFlatsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteFlatsBinding.inflate(inflater, container, false);
        refreshLayout = binding.getRoot();
        adapter = new FavoriteFlatViewAdapter(getViewLifecycleOwner());

        initViews();

        return refreshLayout;
    }

    private void initViews() {
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnFlatClickedListener(flat -> {
            Region region = flat.getRegionReference();
            flat.setRegionReference(null);

            Intent intent = new Intent(getContext(), FlatDetailsActivity.class);
            intent.putExtra("Flat", flat);
            intent.putExtra("Region", region);
            try {
                startActivity(intent);
            } catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnFlatDeletedListener((flat, callback) -> {
            DataAccess.getDataService()
                    .getFavoriteFlatData()
                    .removeFavorite(flat.getId())
                    .addOnCompleteListener(() -> {
                        Toast.makeText(getContext(), "Квартира успешо удалена из избранных", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Произошла ошибка удалении квартиры!", Toast.LENGTH_SHORT).show();
                    });

            callback.delete();
        });

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.post(() -> {
            refreshLayout.setRefreshing(true);
            onRefresh();
        });
    }

    @Override
    public void onRefresh() {
        loadFavoriteFlats();
    }

    private void loadFavoriteFlats() {
        DataAccess.getDataService()
                .getFavoriteFlatData()
                .getFavoriteFlats()
                .observe(getViewLifecycleOwner(), this::loadFlatsAsync);
    }

    private void loadFlatsAsync(List<FavoriteFlat> favoriteFlats) {
        List<String> flatIds = favoriteFlats.stream().map(FavoriteFlat::getFlatId).collect(Collectors.toList());

        if(flatIds.isEmpty()) {
            adapter.setFlats(Collections.emptyList());
            refreshLayout.setRefreshing(false);
            return;
        }

        DataAccess.getDataService()
                .getFlatData()
                .getFlatsByIds(flatIds)
                .observe(this, this::onFlatsLoaded);
    }

    private void onFlatsLoaded(List<Flat> flats) {
        List<String> regionIds = flats.stream()
                .map(Flat::getRegionId)
                .distinct()
                .collect(Collectors.toList());

        DataAccess.getDataService()
                .getRegionData()
                .getRegionsByIds(regionIds)
                .observe(this, regions -> onFlatsAndRegionsLoaded(flats, regions));
    }

    private void onFlatsAndRegionsLoaded(List<Flat> flats, List<Region> regions) {
        Map<String,Region> regionsById = regions.stream().collect(HashMap::new, (m, r) -> m.put(r.getId(), r), HashMap::putAll);

        flats.forEach(f -> {
            String regionId = f.getRegionId();
            Region region = regionsById.get(regionId);

            f.setRegionReference(region);
        });

        refreshLayout.setRefreshing(false);
        adapter.setFlats(flats);
    }
}
