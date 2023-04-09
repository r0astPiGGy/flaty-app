package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import static hcmute.edu.vn.phamdinhquochoa.flatyapp.FlatEditActivity.FLAT_INTENT_TAG;
import static hcmute.edu.vn.phamdinhquochoa.flatyapp.RegionEditActivity.REGION_INTENT_TAG;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.ActivityRegionBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.adapter.FlatViewAdapter;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.FlatData;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.RegionData;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.view.AsyncButton;

public class RegionActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final int REQUEST_SYNC = 10;
    public static final int ADD_FLAT = 11;

    private static final int REGION_EDIT_REQUEST = 123;

    private FlatViewAdapter flatViewAdapter;

    private ActivityRegionBinding binding;
    private Intent intent_get_data;
    private Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        flatViewAdapter = new FlatViewAdapter(this);

        intent_get_data = getIntent();

        initViews();
    }

    private void initViews(){
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

        if(DataAccess.isUserAdmin()) {
            binding.adminEditorBar.setVisibility(View.VISIBLE);
        }

        region = (Region) intent_get_data.getSerializableExtra("Region");
        updateRegionInfo();

        initRecyclerView();
        initRefreshLayout(binding.swipeRefreshLayout);

        initButtons();
    }

    private void initRefreshLayout(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.post(() -> {
            refreshLayout.setRefreshing(true);
            onRefresh();
        });
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

    private void initRecyclerView() {
        binding.recyclerView.setAdapter(flatViewAdapter);
        flatViewAdapter.setOnFlatClickedListener(flat -> {
            Intent intent = new Intent(this, FlatDetailsActivity.class);
            intent.putExtra("Flat", flat);
            intent.putExtra("Region", region.copyAndApply(r -> r.setImage(null)));
            try {
                startActivityForResult(intent, REQUEST_SYNC);
            } catch (Exception e){
                Toast.makeText(this, "Невозможно отобразить информацию!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initButtons() {
        binding.buttonAddFlat.setOnClickListener(v -> onFlatAddClicked());
        binding.buttonEditRegion.setOnClickListener(v -> onEditButtonClicked());
        binding.buttonDeleteRegion.setOnClickListener(v -> onDeleteButtonClicked());
    }

    private void updateRegionImage(byte[] image) {
        region.setImage(image);
        Bitmap bitmap = ImageUtils.convertByteArrayToBitmap(image);
        binding.imageRegionCategory.setImageBitmap(bitmap);
    }

    private void onFlatAddClicked() {
        Intent intent = new Intent(this, FlatEditActivity.class);
        Flat flat = new Flat();
        flat.setRegionId(region.getId());

        FlatEditActivity.applyIntentForAdd(intent, flat);

        try {
            startActivityForResult(intent, ADD_FLAT);
        } catch (Exception e){
            Toast.makeText(this, "Невозможно отобразить информацию!", Toast.LENGTH_SHORT).show();
        }
    }

    private void onEditButtonClicked() {
        Intent intent = new Intent(this, RegionEditActivity.class);
        RegionEditActivity.applyIntentForEdit(intent, region);
        try {
            startActivityForResult(intent, REGION_EDIT_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Невозможно отобразить информацию!", Toast.LENGTH_SHORT).show();
        }
    }

    private void onDeleteButtonClicked() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.alert_delete_region_title)
                .setMessage(R.string.alert_delete_region_msg)
                .setPositiveButton(R.string.alert_delete_region_yes, (d, which) -> {
                    onDeleteRegionAlertDialogAccepted(d);
                })
                .setNegativeButton(R.string.alert_delete_region_no, (d, which) -> {
                    d.dismiss();
                })
                .create()
                .show();
    }

    private void onDeleteRegionAlertDialogAccepted(DialogInterface dialog) {
        binding.buttonDeleteRegion.enableWaitMode();

        RegionData regionData = DataAccess.getDataService().getRegionData();
        FlatData flatData = DataAccess.getDataService().getFlatData();

        flatData.deleteFlatsByRegionId(region.getId())
                .addOnCompleteListener(() -> {
                    regionData.deleteRegion(region)
                            .addOnCompleteListener(this::onRegionDeleted)
                            .addOnFailureListener(this::onRegionDeleteFailed);
                })
                .addOnFailureListener(this::onRegionDeleteFailed);
    }

    private void onRegionDeleted() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void onRegionDeleteFailed(Throwable exception) {
        binding.buttonDeleteRegion.disableWaitMode();
    }

    @Override
    public void onRefresh() {
        updateFlatData();
    }

    private void updateFlatData() {
        DataAccess.getDataService()
                .getFlatData()
                .getFlatsByRegionId(region.getId())
                .observe(this, this::onFlatsLoaded);
    }

    private void onFlatsLoaded(List<Flat> flats) {
        Region copy = region.copyAndApply(r -> r.setImage(null));
        flats.forEach(f -> f.setRegionReference(copy));

        binding.swipeRefreshLayout.setRefreshing(false);
        flatViewAdapter.setFlats(flats);
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

        if (requestCode == REGION_EDIT_REQUEST && data != null) {
            region = (Region) data.getSerializableExtra(REGION_INTENT_TAG);

            DataAccess.getDataService()
                    .getRegionData()
                    .updateRegion(region);

            updateRegionInfo();
        }
    }
}