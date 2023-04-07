package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.ActivityFlatEditBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;

public class FlatEditActivity extends AppCompatActivity {

    private static final int EDIT_MODE = 0;
    private static final int ADD_MODE = 1;

    private static final int IMAGE_CHOSEN_RESULT = 0;

    public static final String FLAT_INTENT_TAG = "flat";

    private ActivityFlatEditBinding binding;
    private Flat flat;

    private byte[] image;
    private String name;
    private String description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFlatEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
    }

    private void initViews() {
        Bundle extras = getIntent().getExtras();

        flat = (Flat) extras.getSerializable("flat");
        int mode = extras.getInt("mode");

        if(mode == ADD_MODE) {
            binding.buttonAddFlat.setText(getResources().getText(R.string.activity_flat_edit_button_add));
            binding.flatEditTitle.setText(getResources().getText(R.string.activity_flat_edit_text_add));
        }

        name = flat.getName();
        description = flat.getDescription();
        image = flat.getImage();

        binding.editTextFlatName.setText(name);
        binding.editTextFlatDescription.setText(description);
        if(image != null) {
            binding.image.setImageBitmap(ImageUtils.convertByteArrayToBitmap(image));
        }

        binding.buttonAddFlat.setOnClickListener(v -> onSaveButtonClicked());
        binding.buttonCancelFlat.setOnClickListener(v -> onCancelButtonClicked());
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

        name = binding.editTextFlatName.getText().toString().trim();
        description = binding.editTextFlatDescription.getText().toString().trim();
    }

    private boolean isUserInputValid() {
        if(image == null) {
            showToast(R.string.choose_image);
            return false;
        }

        if(name == null || name.isEmpty()) {
            showToast(R.string.fill_name);
            return false;
        }

        if(description == null || description.isEmpty()) {
            showToast(R.string.fill_description);
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

        flat.setName(name);
        flat.setDescription(description);
        flat.setImage(image);

        Intent result = new Intent();
        result.putExtra(FLAT_INTENT_TAG, flat);

        setResult(RESULT_OK, result);
        finish();
    }

    private void onCancelButtonClicked() {
        finish();
    }

    public static void applyIntentForEdit(Intent intent, Flat flat) {
        intent.putExtra("mode", EDIT_MODE);
        intent.putExtra("flat", flat);
    }

    public static void applyIntentForAdd(Intent intent, Flat flat) {
        intent.putExtra("mode", ADD_MODE);
        intent.putExtra("flat", flat);
    }
}
