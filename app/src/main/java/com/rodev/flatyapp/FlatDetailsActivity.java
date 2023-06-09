package com.rodev.flatyapp;

import static com.rodev.flatyapp.FlatEditActivity.FLAT_INTENT_TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rodev.flatyapp.beans.*;
import com.rodev.flatyapp.dao.DataAccess;
import com.rodev.flatyapp.databinding.ActivityFlatDetailsBinding;
import com.rodev.flatyapp.utils.ImageUtils;

public class FlatDetailsActivity extends AppCompatActivity {
    private ActivityFlatDetailsBinding binding;
    private TextView tvPrice;

    private static final int FLAT_EDIT_REQUEST = 123;

    private Flat flat;
    private Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFlatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        referenceComponent();
        loadData();
    }

    private String getRoundPrice(Double price){
        if(price == null) return null;

        return "Стоимость: " + Math.round(price) + " руб";
    }

    private void referenceComponent(){
        binding.btnBack.setOnClickListener(view -> onBackButtonClicked());

        tvPrice = findViewById(R.id.tvPrice);

        Button btnSavedFlat = findViewById(R.id.btnSavedFlat);
        btnSavedFlat.setOnClickListener(view -> onFavoriteButtonClicked());

        if(!DataAccess.getUser().isAdmin()) return;

        binding.adminEditorBar.setVisibility(View.VISIBLE);

        binding.buttonEditFlat.setOnClickListener(v -> onEditButtonClicked());
        binding.buttonDeleteFlat.setOnClickListener(v -> onDeleteButtonClicked());
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void onFavoriteButtonClicked() {
        DataAccess.getDataService()
                .getFavoriteFlatData()
                .addFavorite(flat.getId())
                .addOnCompleteListener(() -> {
                    Toast.makeText(this, "Информация о квартире сохранена!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(this, "При сохранении квартиры произошла ошибка", Toast.LENGTH_SHORT).show();
                });
    }

    private void onEditButtonClicked() {
        Intent intent = new Intent(this, FlatEditActivity.class);
        FlatEditActivity.applyIntentForEdit(intent, flat);
        try {
            startActivityForResult(intent, FLAT_EDIT_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Невозможно отобразить информацию!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == FLAT_EDIT_REQUEST && data != null) {
            flat = (Flat) data.getSerializableExtra(FLAT_INTENT_TAG);
            DataAccess.getDataService().getFlatData().updateFlat(flat);
            updateFlatInfo();
        }
    }

    private void onBackButtonClicked() {
        finishActivityAndUpdateFlats();
    }

    private void onDeleteButtonClicked() {
        binding.buttonDeleteFlat.enableWaitMode();
        DataAccess.getDataService()
                .getFlatData()
                .deleteFlatById(flat.getId())
                .addOnCompleteListener(this::onFlatDeleted)
                .addOnFailureListener(this::onFlatDeleteFailure);
    }

    private void onFlatDeleted() {
        sync(this::finishActivityAndUpdateFlats);
    }

    private void onFlatDeleteFailure(Throwable throwable) {
        binding.buttonDeleteFlat.disableWaitMode();
        Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    private void sync(Runnable task) {
        new Handler().post(task);
    }

    private void finishActivityAndUpdateFlats() {
        setResult(RESULT_OK);
        finish();
    }

    private void loadData(){
        Intent intent = getIntent();

        if (intent == null) {
            return;
        }

        flat = (Flat) intent.getSerializableExtra("Flat");
        region = (Region) intent.getSerializableExtra("Region");

        updateFlatInfo();
    }

    private void updateFlatInfo() {
        binding.tvFlatName.setText(flat.getName());
        binding.tvDescription.setText(flat.getDescription());
        updateFlatImage();

        binding.tvRegionName.setText(String.format("Название района \n%s", region.getName()));
        binding.tvRegionAddress.setText(String.format("Адрес \n%s", region.getAddress()));

        tvPrice.setText(getRoundPrice(flat.getPrice()));
    }

    private void updateFlatImage() {
        byte[] image = flat.getImage();

        if(image == null) {
            DataAccess.getDataService()
                    .getImageStorage()
                    .getImageByUri(flat.getId())
                    .observe(this, this::updateFlatImage);
            return;
        }
        updateFlatImage(image);
    }

    private void updateFlatImage(byte[] image) {
        flat.setImage(image);

        Bitmap bitmap = ImageUtils.convertByteArrayToBitmap(image);
        binding.image.setImageBitmap(bitmap);
    }
}