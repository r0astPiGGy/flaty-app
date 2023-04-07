package hcmute.edu.vn.phamdinhquochoa.flatyapp.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;

import java.util.function.Consumer;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FlatSize;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;

@SuppressLint("ViewConstructor")
public class FlatSavedCard extends LinearLayout {
    private final Flat flat;
    private final String regionName;
    private final FlatSize flatSize;
    private final LifecycleOwner lifecycleOwner;
    private Consumer<FlatSavedCard> onCardDeleteListener = flat -> {};
    private ImageView imageView;

    public FlatSavedCard(Context context, Flat Flat, String RegionName, FlatSize FlatSize, LifecycleOwner lifecycleOwner) {
        super(context);
        this.flat = Flat;
        this.regionName = RegionName;
        this.flatSize = FlatSize;
        this.lifecycleOwner = lifecycleOwner;
        initControl(context);
    }

    public void setOnDeleteListener(Consumer<FlatSavedCard> onCardDelete) {
        this.onCardDeleteListener = onCardDelete;
    }

    @SuppressLint("SetTextI18n")
    private void initControl(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.flat_saved_card, this);

        imageView = findViewById(R.id.imageSavedFlat);
        TextView tvName = findViewById(R.id.tvFlatNameSaved);
        TextView tvSize = findViewById(R.id.tvFlatSavedSize);
        TextView tvRegionName = findViewById(R.id.tvFlatSavedRegionName);
        TextView tvPrice = findViewById(R.id.tvFlatSavedPrice);

        Button btnDelete = findViewById(R.id.btnDeleteSaveCardItem);
        btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Вы хотите удалить элемент " + flat.getName() + " ?");
            dialog.setPositiveButton("Да", (dialogInterface, i) -> {
                DataAccess.getDataService()
                        .getFavoriteFlatData()
                        .removeFavorite(flat.getId())
                        .addOnCompleteListener(this::onCardDeleted);
            });
            dialog.setNegativeButton("Нет", (dialogInterface, i) -> {});
            dialog.show();
        });

        // Set information for cart card
        initImage();
        tvName.setText(flat.getName());
        switch (flatSize.getSize()){
            case 1:
                tvSize.setText("");
                break;
            case 2:
                tvSize.setText("Size M");
                break;
            case 3:
                tvSize.setText("Size L");
                break;
        }
        tvRegionName.setText(regionName);
        tvPrice.setText(getRoundPrice(flatSize.getPrice()));
    }

    private void initImage() {
        byte[] image = flat.getImage();

        if(image == null) {
            DataAccess.getDataService()
                    .getImageStorage()
                    .getImageByUri(flat.getId())
                    .observe(lifecycleOwner, this::initImage);
            return;
        }

        initImage(image);
    }

    private void initImage(byte[] image) {
        Bitmap bitmap = ImageUtils.convertByteArrayToBitmap(image);
        imageView.setImageBitmap(bitmap);
    }

    private void onCardDeleted() {
        onCardDeleteListener.accept(this);
    }

    private String getRoundPrice(Double price){
        return "Стоимость: "+Math.round(price) + " руб";
    }
}
