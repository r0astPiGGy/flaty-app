package hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments;

import static android.app.Activity.RESULT_OK;

import static hcmute.edu.vn.phamdinhquochoa.flatyapp.RegionEditActivity.REGION_INTENT_TAG;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.CategoryActivity;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.RegionEditActivity;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.view.RegionCard;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;

public class HomeFragment extends Fragment {
    private Intent intent;
    private View mainView;
    private Button addRegionButton;

    private final static int ADD_REGION_RC = 0;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mainView == null){
            mainView = inflater.inflate(R.layout.fragment_home, container, false);
            addRegionButton = mainView.findViewById(R.id.button_add_region);
            lazyInit();
        }

        updateButton();

        return mainView;
    }

    private void lazyInit() {
        updateRegions();

        SearchView searchBar = mainView.findViewById(R.id.search_bar);
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
        addRegionButton.setOnClickListener(v -> onAddRegionButtonClicked());
    }

    private void updateRegions() {
        DataAccess.getDataService()
                .getRegionData()
                .getAllRegions()
                .observe(this, this::onRegionsLoaded);
    }

    private void onRegionsLoaded(List<Region> regionList) {
        LinearLayout layout_container = mainView.findViewById(R.id.layout_container_Region);
        layout_container.removeAllViews();
        for(Region region : regionList){
            RegionCard card = new RegionCard(getContext(), region, false, this);
            card.setOnClickListener(view -> {
                intent = new Intent(getActivity(), CategoryActivity.class);
                // TODO: put serializable
                intent.putExtra("RegionId", region.getId());
                startActivity(intent);
            });

            layout_container.addView(card);
        }
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