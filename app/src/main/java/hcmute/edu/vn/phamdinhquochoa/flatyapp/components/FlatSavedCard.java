package hcmute.edu.vn.phamdinhquochoa.flatyapp.components;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.HomeActivity;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FlatSize;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dbcontext.DatabaseHandler;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments.SavedFragment;

@SuppressLint("ViewConstructor")
public class FlatSavedCard extends LinearLayout {
    private final Flat Flat;
    private final String RegionName;
    private final FlatSize FlatSize;

    public FlatSavedCard(Context context, Flat Flat, String RegionName, FlatSize FlatSize) {
        super(context);
        this.Flat = Flat;
        this.RegionName = RegionName;
        this.FlatSize = FlatSize;
        initControl(context);
    }

    @SuppressLint("SetTextI18n")
    private void initControl(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.flat_saved_card, this);

        ImageView image = findViewById(R.id.imageSavedFlat);
        TextView tvName = findViewById(R.id.tvFlatNameSaved);
        TextView tvSize = findViewById(R.id.tvFlatSavedSize);
        TextView tvRegionName = findViewById(R.id.tvFlatSavedRegionName);
        TextView tvPrice = findViewById(R.id.tvFlatSavedPrice);

        Button btnDelete = findViewById(R.id.btnDeleteSaveCardItem);
        btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Вы хотите удалить элемент " + Flat.getName() + " ?");
            dialog.setPositiveButton("Имеется", (dialogInterface, i) -> {
                HomeActivity.dao.deleteFlatSavedByFlatIdAndSize(FlatSize.getFlatId(), FlatSize.getSize());
                SavedFragment.saved_container.removeView(this);
            });
            dialog.setNegativeButton("Нет", (dialogInterface, i) -> {});
            dialog.show();
        });

        // Set information for cart card
        image.setImageBitmap(DatabaseHandler.convertByteArrayToBitmap(Flat.getImage()));
        tvName.setText(Flat.getName());
        switch (FlatSize.getSize()){
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
        tvRegionName.setText(RegionName);
        tvPrice.setText(getRoundPrice(FlatSize.getPrice()));
    }

    private String getRoundPrice(Double price){
        return "Стоимость: "+Math.round(price) + " руб";
    }
}
