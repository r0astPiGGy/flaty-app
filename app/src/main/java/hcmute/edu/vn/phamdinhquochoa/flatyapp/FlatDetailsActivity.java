package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import static hcmute.edu.vn.phamdinhquochoa.flatyapp.FlatEditActivity.FLAT_INTENT_TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.ActivityFlatDetailsBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.*;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;

public class FlatDetailsActivity extends AppCompatActivity {
    private ActivityFlatDetailsBinding binding;
    private TextView tvPrice, tvQuantity;

    private static final int FLAT_EDIT_REQUEST = 123;

    private static int quantity;
    public static FlatSize FlatSize;

    private Flat flat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFlatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quantity = 1;

        referenceComponent();
        loadData();
    }

    private String getRoundPrice(Double price){
        return "стоимость: "+Math.round(price) + " руб";
    }

    private String getTotalPrice() { return "стоимость: "+Math.round(FlatSize.getPrice() * quantity) + " руб."; }

    private void referenceComponent(){
        binding.btnBack.setOnClickListener(view -> onBackButtonClicked());

        tvPrice = findViewById(R.id.tvPrice);

        tvQuantity = findViewById(R.id.tvFlatQuantity_Flat);

        Button btnSavedFlat = findViewById(R.id.btnSavedFlat);
        btnSavedFlat.setOnClickListener(view -> onFavoriteButtonClicked());

        Button btnAddQuantity = findViewById(R.id.btnAddQuantity_Flat);
        btnAddQuantity.setOnClickListener(view -> {
            quantity++;
            tvQuantity.setText(String.format("%s", quantity));
            tvPrice.setText(getTotalPrice());
        });

        Button btnSubQuantity = findViewById(R.id.btnSubQuantity_Flat);
        btnSubQuantity.setOnClickListener(view -> {
            if(quantity > 1){
                quantity--;
                tvQuantity.setText(String.format("%s", quantity));
                tvPrice.setText(getTotalPrice());
            }
        });

        if(!DataAccess.getUser().isAdmin()) return;

        binding.adminEditorBar.setVisibility(View.VISIBLE);

        binding.buttonEditFlat.setOnClickListener(v -> onEditButtonClicked());
        binding.buttonDeleteFlat.setOnClickListener(v -> onDeleteButtonClicked());
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
        if(resultCode != RESULT_OK) return;

        if(requestCode == FLAT_EDIT_REQUEST && data != null) {
            flat = (Flat) data.getSerializableExtra(FLAT_INTENT_TAG);
            DataAccess.getDataService().getFlatData().updateFlat(flat);
            updateFlatInfo();
        }
    }

    private void onBackButtonClicked() {
        finishActivityAndUpdateFlats();
    }

    private void onDeleteButtonClicked() {
        DataAccess.getDataService()
                .getFlatData()
                .deleteFlatById(flat.getId())
                .addOnCompleteListener(this::onFlatDeleted);
    }

    private void onFlatDeleted() {
        sync(this::finishActivityAndUpdateFlats);
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

        updateFlatInfo();
    }

    private void updateFlatInfo() {
        binding.tvFlatName.setText(flat.getName());
        binding.tvDescription.setText(flat.getDescription());
        binding.image.setImageBitmap(ImageUtils.convertByteArrayToBitmap(flat.getImage()));

        Region region = flat.getRegionReference();
        binding.tvRegionName.setText(String.format("Название региона \n%s", region.getName()));
        binding.tvRegionAddress.setText(String.format("Адрес \n%s", region.getAddress()));

        tvPrice.setText(getRoundPrice(FlatSize.getPrice()));
    }
}