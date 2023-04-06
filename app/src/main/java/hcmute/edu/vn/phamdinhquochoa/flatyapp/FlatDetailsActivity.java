package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import static hcmute.edu.vn.phamdinhquochoa.flatyapp.FlatEditActivity.FLAT_INTENT_TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.ActivityFlatDetailsBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.*;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DAO;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dbcontext.DatabaseHandler;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;

public class FlatDetailsActivity extends AppCompatActivity {
    private ActivityFlatDetailsBinding binding;
    private TextView tvPrice, tvQuantity;

    private static final int FLAT_EDIT_REQUEST = 123;

    public static Integer userID;
    private static int quantity;
    public static FlatSize FlatSize;

    private Flat flat;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFlatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quantity = 1;

        referenceComponent();
        dao = new DAO(this);
        LoadData();
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
        btnSavedFlat.setOnClickListener(view -> {
            boolean addFlatSaved = dao.addFlatSaved(new FlatSaved(FlatSize.getFlatId(), FlatSize.getSize(), userID));
            if(addFlatSaved){
                Toast.makeText(this, "Информация о квартире сохранена!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "При сохранении квартиры произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });

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

        if(!HomeActivity.user.isAdmin()) return;

        binding.adminEditorBar.setVisibility(View.VISIBLE);

        binding.buttonEditFlat.setOnClickListener(v -> onEditButtonClicked());
        binding.buttonDeleteFlat.setOnClickListener(v -> onDeleteButtonClicked());
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
            updateFlatInfo();
            dao.updateFlat(flat);
        }
    }

    private void onBackButtonClicked() {
        finishActivityAndUpdateFlats();
    }

    private void onDeleteButtonClicked() {
        dao.deleteFlatById(flat.getId());

        finishActivityAndUpdateFlats();
    }

    private void finishActivityAndUpdateFlats() {
        setResult(RESULT_OK);
        finish();
    }

    private void SetPriceDefault(Double price){
        tvPrice.setText(getRoundPrice(price));
        quantity = 1;
        tvQuantity.setText("1");
    }

    private void LoadData(){
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

        Region Region = dao.getRegionInformation(flat.getRegionId());
        binding.tvRegionName.setText(String.format("Название региона \n%s", Region.getName()));
        binding.tvRegionAddress.setText(String.format("Адрес \n%s", Region.getAddress()));

        tvPrice.setText(getRoundPrice(FlatSize.getPrice()));
    }
}