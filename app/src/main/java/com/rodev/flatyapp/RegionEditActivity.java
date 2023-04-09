package com.rodev.flatyapp;

import static com.rodev.flatyapp.utils.EditTextUtils.getInputOrNull;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rodev.flatyapp.beans.Region;
import com.rodev.flatyapp.databinding.ActivityRegionEditBinding;
import com.rodev.flatyapp.utils.ImageUtils;

public class RegionEditActivity extends AppCompatActivity {

    private static final int EDIT_MODE = 0;
    private static final int ADD_MODE = 1;

    private static final int IMAGE_CHOSEN_RESULT = 0;

    public static final String REGION_INTENT_TAG = "region";

    private ActivityRegionEditBinding binding;
    private Region region;

    private byte[] image;
    private String name;
    private String address;
    private String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegionEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
    }

    private void initViews() {
        Bundle extras = getIntent().getExtras();

        region = (Region) extras.getSerializable("region");
        int mode = extras.getInt("mode");

        if(mode == ADD_MODE) {
            binding.buttonAddRegion.setText(getResources().getText(R.string.activity_region_edit_button_add));
            binding.regionEditTitle.setText(getResources().getText(R.string.activity_region_edit_text_add));
        }

        name = region.getName();
        address = region.getAddress();
        phone = region.getPhone();
        image = region.getImage();

        binding.editTextRegionName.setText(name);
        binding.editTextRegionAddress.setText(address);
        binding.editTextRegionPhoneNumber.setText(phone);
        if(image != null) {
            binding.image.setImageBitmap(ImageUtils.convertByteArrayToBitmap(image));
        }

        binding.buttonAddRegion.setOnClickListener(v -> onSaveButtonClicked());
        binding.buttonCancelRegion.setOnClickListener(v -> onCancelButtonClicked());
        binding.buttonChangeImage.setOnClickListener(v -> onImageChangeClicked());
    }

    private void onImageChangeClicked() {
        Intent intent = new Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), IMAGE_CHOSEN_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == IMAGE_CHOSEN_RESULT && data != null) {
            changeImageTo(data.getData());
        }
    }

    private void changeImageTo(Uri uri) {
        Bitmap bitmap = ImageUtils.getBitmapFromUri(getContentResolver(), uri);

        if(bitmap == null) return;

        binding.image.setImageBitmap(bitmap);
    }

    private void collectUserInput() {
        Drawable imageDrawable = binding.image.getDrawable();

        if(imageDrawable != null) {
            image = ImageUtils.resizeAndConvertDrawable(imageDrawable);
        }

        name = getInputOrNull(binding.editTextRegionName);
        address = getInputOrNull(binding.editTextRegionAddress);
        phone = getInputOrNull(binding.editTextRegionPhoneNumber);
    }

    private boolean isUserInputValid() {
        if(image == null) {
            showToast(R.string.choose_image);
            return false;
        }

        if(name == null || name.isEmpty()) {
            showToast(R.string.fill_region_name);
            return false;
        }

        if(address == null || address.isEmpty()) {
            showToast(R.string.fill_region_address);
            return false;
        }

        if(phone == null || phone.isEmpty()) {
            showToast(R.string.fill_region_phone);
            return false;
        }

        return true;
    }

    private void showToast(int string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    private void onSaveButtonClicked() {
        collectUserInput();

        if(!isUserInputValid()) {
            return;
        }

        region.setName(name);
        region.setAddress(address);
        region.setPhone(phone);
        region.setImage(image);

        Intent result = new Intent();
        result.putExtra(REGION_INTENT_TAG, region);

        setResult(RESULT_OK, result);
        finish();
    }

    private void onCancelButtonClicked() {
        finish();
    }

    public static void applyIntentForEdit(Intent intent, Region region) {
        intent.putExtra("mode", EDIT_MODE);
        intent.putExtra("region", region);
    }

    public static void applyIntentForAdd(Intent intent, Region region) {
        intent.putExtra("mode", ADD_MODE);
        intent.putExtra("region", region);
    }
}
