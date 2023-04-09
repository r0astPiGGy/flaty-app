package hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments;

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
import java.util.List;
import java.util.stream.Collectors;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.FragmentFavoriteRegionsBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.RegionActivity;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.adapter.FavoriteRegionViewAdapter;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FavoriteRegion;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;

public class FavoriteRegionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FavoriteRegionViewAdapter adapter;
    private FragmentFavoriteRegionsBinding binding;
    private SwipeRefreshLayout refreshLayout;

    public FavoriteRegionsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteRegionsBinding.inflate(inflater, container, false);
        refreshLayout = binding.getRoot();
        adapter = new FavoriteRegionViewAdapter(getViewLifecycleOwner());

        initViews();

        return refreshLayout;
    }

    private void initViews() {
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnRegionClickedListener(region -> {
            Intent intent = new Intent(getActivity(), RegionActivity.class);
            intent.putExtra("Region", region);
            startActivity(intent);
        });
        adapter.setOnRegionDeletedListener((region, callback) -> {
            DataAccess.getDataService()
                    .getFavoriteRegionData()
                    .removeFavorite(region.getId())
                    .addOnCompleteListener(() -> {
                        Toast.makeText(getContext(), "Район успешо удалён из избранных", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Произошла ошибка удалении района!", Toast.LENGTH_SHORT).show();
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
        loadFavoriteRegions();
    }

    private void loadFavoriteRegions() {
        DataAccess.getDataService()
                .getFavoriteRegionData()
                .getFavoriteRegions()
                .observe(getViewLifecycleOwner(), this::loadRegionsAsync);
    }

    private void loadRegionsAsync(List<FavoriteRegion> favoriteRegions) {
        List<String> regionIds = favoriteRegions.stream().map(FavoriteRegion::getRegionId).collect(Collectors.toList());

        if(regionIds.isEmpty()) {
            adapter.setRegions(Collections.emptyList());
            refreshLayout.setRefreshing(false);
            return;
        }

        DataAccess.getDataService()
                .getRegionData()
                .getRegionsByIds(regionIds)
                .observe(this, this::onRegionsLoaded);
    }

    private void onRegionsLoaded(List<Region> regions) {
        refreshLayout.setRefreshing(false);

        adapter.setRegions(regions);
    }
}
