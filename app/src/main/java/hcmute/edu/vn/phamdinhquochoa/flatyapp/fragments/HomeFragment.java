package hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import java.util.ArrayList;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.CategoryActivity;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.HomeActivity;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.components.RegionCard;

public class HomeFragment extends Fragment {
    private Intent intent;
    private View mainView;
    private Button addRegionButton;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mainView == null){
            mainView = inflater.inflate(R.layout.fragment_home, container, false);
//            addRegionButton = mainView.findViewById(R.id.button_add_region);
            lazyInit();
        }

//        updateButton();

        return mainView;
    }

    private void lazyInit() {
        LinearLayout layout_container = mainView.findViewById(R.id.layout_container_Region);

        ArrayList<Region> RegionArrayList = HomeActivity.dao.getRegionList();

        layout_container.removeAllViews();
        for(Region Region : RegionArrayList){
            RegionCard card = new RegionCard(getContext(), Region, false);
            card.setOnClickListener(view->{
                intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("RegionId", Region.getId());
                startActivity(intent);
            });

            layout_container.addView(card);
        }

        ImageView imageCart = mainView.findViewById(R.id.imageCart);
        imageCart.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.putExtra("request", "cart");
            startActivity(intent);
        });

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
    }

    private void updateButton() {
        boolean isAdmin = HomeActivity.user.isAdmin();

        if(isAdmin) {
            addRegionButton.setVisibility(View.VISIBLE);
        } else {
            addRegionButton.setVisibility(View.GONE);
        }
    }
}