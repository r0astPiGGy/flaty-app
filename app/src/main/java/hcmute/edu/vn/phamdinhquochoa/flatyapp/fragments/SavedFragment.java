package hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.CategoryActivity;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.FlatDetailsActivity;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.HomeActivity;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FlatSaved;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FlatSize;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.RegionSaved;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.components.FlatSavedCard;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.components.RegionCard;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static LinearLayout saved_container;
    private LinearLayout btn_saved_Flat, btn_saved_Region;
    private TextView tv_saved_Flat, tv_saved_Region;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public SavedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedFragment newInstance(String param1, String param2) {
        SavedFragment fragment = new SavedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

            LoadSavedCard("Flat");
        });

        btn_saved_Region.setOnClickListener(view -> {
            btn_saved_Flat.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.bg_white));
            tv_saved_Flat.setTextColor(Color.BLACK);
            btn_saved_Region.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.color.teal_700));
            tv_saved_Region.setTextColor(Color.WHITE);

            LoadSavedCard("Region");
        });

        LoadSavedCard("Flat");

        return mainView;
    }

    private void LoadSavedCard(String type){
        saved_container.removeAllViews();

        if(type.equals("Flat")){
            ArrayList<FlatSaved> FlatSavedArrayList = HomeActivity.dao.getFlatSaveList(HomeActivity.user.getId());

            if(FlatSavedArrayList.size() > 0){
                for(FlatSaved FlatSaved : FlatSavedArrayList){
                    Flat Flat = HomeActivity.dao.getFlatById(FlatSaved.getFlatId());
                    Region Region = HomeActivity.dao.getRegionInformation(Flat.getRegionId());
                    FlatSize FlatSize = HomeActivity.dao.getFlatSize(FlatSaved.getFlatId(), FlatSaved.getSize());

                    FlatSavedCard savedCard = new FlatSavedCard(getContext(), Flat, Region.getName(), FlatSize);
                    savedCard.setOnClickListener(view -> {
                        FlatDetailsActivity.FlatSize = FlatSize;
                        Intent intent = new Intent(getContext(), FlatDetailsActivity.class);
                        intent.putExtra("Flat", Flat);
                        try {
                            startActivity(intent);
                        } catch (Exception e){
                            Toast.makeText(getContext(), "Выход из системы!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    saved_container.addView(savedCard);
                }
            }
        } else {
            ArrayList<RegionSaved> RegionSavedArrayList = HomeActivity.dao.getRegionSavedList(HomeActivity.user.getId());
            for(RegionSaved RegionSaved : RegionSavedArrayList){
                Region Region = HomeActivity.dao.getRegionInformation(RegionSaved.getRegionId());
                RegionCard card = new RegionCard(getContext(), Region, true);
                card.setOnClickListener(view -> {
                    Intent intent = new Intent(getActivity(), CategoryActivity.class);
                    intent.putExtra("RegionId", Region.getId());
                    startActivity(intent);
                });
                saved_container.addView(card);
            }
        }
    }
}