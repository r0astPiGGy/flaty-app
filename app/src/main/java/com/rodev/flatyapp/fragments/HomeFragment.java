package com.rodev.flatyapp.fragments;

import static android.app.Activity.RESULT_OK;

import static com.rodev.flatyapp.RegionEditActivity.REGION_INTENT_TAG;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import com.rodev.flatyapp.RegionActivity;
import com.rodev.flatyapp.RegionEditActivity;
import com.rodev.flatyapp.adapter.RegionViewAdapter;
import com.rodev.flatyapp.beans.Region;
import com.rodev.flatyapp.dao.DataAccess;
import com.rodev.flatyapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RegionViewAdapter adapter;
    private FragmentHomeBinding binding;
    private Intent intent;
    private Button addRegionButton;

    private final static int ADD_REGION_RC = 0;
    private final static int SYNC_RC = 1;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = new RegionViewAdapter(getViewLifecycleOwner());
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        addRegionButton = binding.buttonAddRegion;

        initViews();
        updateButton();

        return binding.getRoot();
    }

    private void initViews() {
        binding.regionRecyclerView.setAdapter(adapter);

        adapter.setOnRegionClickedListener(region -> {
            intent = new Intent(getActivity(), RegionActivity.class);
            intent.putExtra("Region", region);
            startActivityForResult(intent, SYNC_RC);
        });
        adapter.setOnRegionSaveButtonClickedListener(region -> {
            DataAccess.getDataService()
                    .getFavoriteRegionData()
                    .addFavorite(region.getId())
                    .addOnCompleteListener(() -> {
                        Toast.makeText(getContext(), "Район успешно добавлен в избранные!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "При добавлении района в избранные произошла ошибка.", Toast.LENGTH_SHORT).show();
                    });
        });

        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.swipeRefreshLayout.post(() -> {
            binding.swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        });

        initSearchBar();

        addRegionButton.setOnClickListener(v -> onAddRegionButtonClicked());
    }

    private void initSearchBar() {
        SearchView searchBar = binding.searchBar;
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String query = s.toLowerCase();
                adapter.setShowRegionsPredicate(r -> r.getName().toLowerCase().contains(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String query = s.toLowerCase();
                adapter.setShowRegionsPredicate(r -> r.getName().toLowerCase().contains(query));
                return true;
            }
        });
    }

    @Override
    public void onRefresh() {
        updateRegions();
    }

    private void updateRegions() {
        DataAccess.getDataService()
                .getRegionData()
                .getAllRegions()
                .observe(getViewLifecycleOwner(), this::onRegionsLoaded);
    }

    private void onRegionsLoaded(List<Region> regionList) {
        binding.swipeRefreshLayout.setRefreshing(false);
        adapter.setRegions(regionList);
    }

    private void onAddRegionButtonClicked() {
        Intent intent = new Intent(getActivity(), RegionEditActivity.class);
        RegionEditActivity.applyIntentForAdd(intent, new Region());

        startActivityForResult(intent, ADD_REGION_RC);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) return;

        if(requestCode == ADD_REGION_RC && data != null) {
            Region region = (Region) data.getSerializableExtra(REGION_INTENT_TAG);
            DataAccess.getDataService()
                    .getRegionData()
                    .addRegion(region)
                    .addOnCompleteListener(this::updateRegions);
        }

        if(requestCode == SYNC_RC) {
            binding.swipeRefreshLayout.setRefreshing(true);
            updateRegions();
        }
    }

    private void updateButton() {
        boolean isAdmin = DataAccess.getUser().isAdmin();

        if(isAdmin) {
            addRegionButton.setVisibility(View.VISIBLE);
        } else {
            addRegionButton.setVisibility(View.GONE);
        }
    }
}