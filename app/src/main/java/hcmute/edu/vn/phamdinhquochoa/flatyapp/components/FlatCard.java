package hcmute.edu.vn.phamdinhquochoa.flatyapp.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dbcontext.DatabaseHandler;

public class FlatCard extends LinearLayout {
    private Flat Flat;
    private Double defaultPrice;
    private String RegionName;

    public FlatCard(Context context, Flat Flat, Double defaultPrice, String RegionName){
        super(context);
        this.Flat = Flat;
        this.defaultPrice = defaultPrice;
        this.RegionName = RegionName;
        initControl(context);
    }

    public FlatCard(Context context) {
        super(context);
        initControl(context);
    }

    private void initControl(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.flat_card, this);

        ImageView image = findViewById(R.id.imageFlat);
        TextView tvName = findViewById(R.id.tvNameFlat);
        TextView tvPrice = findViewById(R.id.tvPriceFlat);
        TextView tvRegionName = findViewById(R.id.tvFlatRegionName);

        // Set information for Flat cart
        image.setImageBitmap(DatabaseHandler.convertByteArrayToBitmap(Flat.getImage()));
        tvName.setText(Flat.getName());
        tvPrice.setText(String.format("%s руб", Math.round(defaultPrice)));
        tvRegionName.setText(RegionName);
    }
}
