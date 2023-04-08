package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import static hcmute.edu.vn.phamdinhquochoa.flatyapp.FlatEditActivity.FLAT_INTENT_TAG;

import android.content.Intent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.List;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.ActivityCategoryBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FlatSize;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.view.FlatCard;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;

public class CategoryActivity extends AppCompatActivity {

    public static final int REQUEST_SYNC = 10;
    public static final int ADD_FLAT = 11;

    private ActivityCategoryBinding binding;
    private LinearLayout FlatCartContainer;
    private Intent intent_get_data;
    private Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent_get_data = getIntent();

        referencesComponents();
    }

    private void referencesComponents(){
        binding.imageSync.setOnClickListener(view -> updateFlatData());
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String nameFlatOfThisRegion = binding.searchBar.getQuery().toString();
                // TODO
                //initFlatDataByRegion(nameFlatOfThisRegion);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        FlatCartContainer = binding.FlatCartContainer;

        if(DataAccess.getUser().isAdmin()) {
            binding.adminEditorBar.setVisibility(View.VISIBLE);
        }

        // Region data
        region = (Region) intent_get_data.getSerializableExtra("Region");
        updateRegionInfo();
        updateFlatData();
        binding.buttonAddFlat.setOnClickListener(v -> onFlatAddClicked());
    }

    private void updateRegionInfo() {
        updateRegionImage();
        binding.tvRegionNameCategory.setText(region.getName());
        binding.tvRegionPhoneCategory.setText(String.format("Адрес: %s", region.getAddress()));
        binding.tvRegionAddressCategory.setText(String.format("Телефон: %s", region.getPhone()));
    }

    private void updateRegionImage() {
        byte[] image = region.getImage();

        if(image == null) {
            DataAccess.getDataService().getImageStorage()
                    .getImageByUri(region.getId())
                    .observe(this, this::updateRegionImage);
            return;
        }

        updateRegionImage(image);
    }

    private void updateRegionImage(byte[] image) {
        region.setImage(image);
        Bitmap bitmap = ImageUtils.convertByteArrayToBitmap(image);
        binding.imageRegionCategory.setImageBitmap(bitmap);
    }

    private void onFlatAddClicked() {
        Intent intent = new Intent(this, FlatEditActivity.class);
        Flat flat = new Flat();
        flat.setRegionId(flat.getRegionId());
        flat.setType("default");

        FlatEditActivity.applyIntentForAdd(intent, flat);

        try {
            startActivityForResult(intent, ADD_FLAT);
        } catch (Exception e){
            Toast.makeText(this, "Невозможно отобразить информацию!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFlatData() {
//        String regionId = intent_get_data.getStringExtra("RegionId");
//
//        if (regionId == null){
//            String FlatKeyword = intent_get_data.getStringExtra("nameFlat");
//            DataAccess.getDataService()
//                    .getFlatData()
//                    .getFlatsByKeyWord(FlatKeyword)
//                    .observe(this, this::onFlatsLoaded);
//            return;
//        }

        DataAccess.getDataService()
                .getFlatData()
                .getFlatsByRegionId(region.getId())
                .observe(this, this::onFlatsLoaded);
    }

    private void onFlatsLoaded(List<Flat> flats) {
        updateFlatData(flats);
    }

    private void updateFlatData(List<Flat> flats){
        FlatCartContainer.removeAllViews();

        flats.forEach(this::addFlat);
    }

    private void addFlat(Flat flat) {
        // TODO
        FlatSize flatSize = new FlatSize(15, 20, 3000d);
        FlatCard flatCard = new FlatCard(this, flat, flatSize.getPrice(), region.getName(), this);

        flatCard.setOnClickListener(view -> {
            FlatDetailsActivity.FlatSize = flatSize;
            Intent intent = new Intent(this, FlatDetailsActivity.class);
            intent.putExtra("Flat", flat);
            intent.putExtra("Region", region.copyAndApply(r -> r.setImage(null)));
            try {
                startActivityForResult(intent, REQUEST_SYNC);
            } catch (Exception e){
                Toast.makeText(this, "Невозможно отобразить информацию!", Toast.LENGTH_SHORT).show();
            }
        });
        FlatCartContainer.addView(flatCard);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_SYNC) {
            updateFlatData();
        }

        if (requestCode == ADD_FLAT && data != null) {
            Flat addedFlat = (Flat) data.getSerializableExtra(FLAT_INTENT_TAG);
            DataAccess.getDataService()
                    .getFlatData()
                    .addFlat(addedFlat)
                    .addOnCompleteListener(this::updateFlatData);
        }
    }
}