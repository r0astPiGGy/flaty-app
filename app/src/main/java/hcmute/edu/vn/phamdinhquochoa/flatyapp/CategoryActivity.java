package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import static hcmute.edu.vn.phamdinhquochoa.flatyapp.FlatEditActivity.FLAT_INTENT_TAG;

import android.content.Intent;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.ActivityCategoryBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FlatSize;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.components.FlatCard;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DAO;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dbcontext.DatabaseHandler;

public class CategoryActivity extends AppCompatActivity {

    private static final int REGION_NOT_FOUND = -1;

    public static final int REQUEST_SYNC = 10;
    public static final int ADD_FLAT = 11;

    private ActivityCategoryBinding binding;
    private LinearLayout FlatCartContainer;
    private DAO dao;
    private Intent intent_get_data;
    private Integer RegionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent_get_data = getIntent();

        dao = new DAO(this);
        referencesComponents();
        initFlatData();
    }

    private void referencesComponents(){
        binding.imageSync.setOnClickListener(view -> initFlatData());
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String nameFlatOfThisRegion = binding.searchBar.getQuery().toString();
                initFlatDataByRegion(nameFlatOfThisRegion);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        FlatCartContainer = binding.FlatCartContainer;

        if(HomeActivity.user.isAdmin()) {
            binding.adminEditorBar.setVisibility(View.VISIBLE);
        }

        // Region data
        RegionId = intent_get_data.getIntExtra("RegionId", REGION_NOT_FOUND);

        if(RegionId == REGION_NOT_FOUND) {
            binding.layoutRegionInformation.setVisibility(View.GONE);
            return;
        }

        Region region = dao.getRegionInformation(RegionId);

        binding.imageRegionCategory.setImageBitmap(DatabaseHandler.convertByteArrayToBitmap(region.getImage()));
        binding.tvRegionNameCategory.setText(region.getName());
        binding.tvRegionPhoneCategory.setText(String.format("Адрес: %s", region.getAddress()));
        binding.tvRegionAddressCategory.setText(String.format("Телефон: %s", region.getPhone()));
        binding.buttonAddFlat.setOnClickListener(v -> onFlatAddClicked());
    }

    private void onFlatAddClicked() {
        Intent intent = new Intent(this, FlatEditActivity.class);
        Flat flat = new Flat();
        flat.setRegionId(RegionId);
        flat.setType("default");

        FlatEditActivity.applyIntentForAdd(intent, flat);

        try {
            startActivityForResult(intent, ADD_FLAT);
        } catch (Exception e){
            Toast.makeText(this, "Невозможно отобразить информацию!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initFlatDataByRegion(String name) {
        initFlatData(dao.getFlatByKeyWord(name, RegionId));
    }

    private ArrayList<Flat> getFlatData() {
        int getRegionId = intent_get_data.getIntExtra("RegionId", -1);

        if (getRegionId == -1){
            String FlatKeyword = intent_get_data.getStringExtra("nameFlat");
            return dao.getFlatByKeyWord(FlatKeyword, null);
        }

        return dao.getFlatByRegion(getRegionId);
    }

    private void initFlatData() {
        initFlatData(getFlatData());
    }

    private void initFlatData(ArrayList<Flat> flats){
        FlatCartContainer.removeAllViews();

        flats.forEach(this::addFlat);
    }

    private void addFlat(Flat flat) {
        Region Region = dao.getRegionInformation(flat.getRegionId());
        FlatSize FlatSize = dao.getFlatDefaultSize(flat.getId());

        FlatCard FlatCard = new FlatCard(this, flat, FlatSize.getPrice(), Region.getName());

        FlatCard.setOnClickListener(view -> {
            FlatDetailsActivity.FlatSize = FlatSize;
            Intent intent = new Intent(this, FlatDetailsActivity.class);
            intent.putExtra("Flat", flat);
            try {
                startActivityForResult(intent, REQUEST_SYNC);
            } catch (Exception e){
                Toast.makeText(this, "Невозможно отобразить информацию!", Toast.LENGTH_SHORT).show();
            }
        });
        FlatCartContainer.addView(FlatCard);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_OK) return;

        if(requestCode == REQUEST_SYNC) {
            initFlatData();
        }

        if(requestCode == ADD_FLAT && data != null) {
            Flat addedFlat = (Flat) data.getSerializableExtra(FLAT_INTENT_TAG);
            dao.addFlat(addedFlat);

            initFlatData();
        }
    }
}