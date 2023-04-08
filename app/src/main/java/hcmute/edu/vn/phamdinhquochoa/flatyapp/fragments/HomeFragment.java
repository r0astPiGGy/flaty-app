package hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments;

import static android.app.Activity.RESULT_OK;

import static hcmute.edu.vn.phamdinhquochoa.flatyapp.RegionEditActivity.REGION_INTENT_TAG;

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

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.FragmentHomeBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.CategoryActivity;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.RegionEditActivity;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.adapter.RegionViewAdapter;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RegionViewAdapter adapter;
    private FragmentHomeBinding binding;
    private Intent intent;
    private Button addRegionButton;

    private final static int ADD_REGION_RC = 0;

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
            intent = new Intent(getActivity(), CategoryActivity.class);
            intent.putExtra("Region", region);
            startActivity(intent);
        });
        adapter.setOnRegionSaveButtonClickedListener(region -> {
            DataAccess.getDataService()
                    .getFavoriteRegionData()
                    .addFavorite(region.getId())
                    .addOnCompleteListener(() -> {
                        Toast.makeText(getContext(), "Сохранение информации о регионе прошло успешно!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Произошла ошибка при сохранении информации!", Toast.LENGTH_SHORT).show();
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
                String textSearch = searchBar.getQuery().toString();
                intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("nameFlat", textSearch);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
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