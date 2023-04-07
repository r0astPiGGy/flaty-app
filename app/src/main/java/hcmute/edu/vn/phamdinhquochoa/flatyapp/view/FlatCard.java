package hcmute.edu.vn.phamdinhquochoa.flatyapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;

public class FlatCard extends LinearLayout {
    private Flat flat;
    private Double price;
    private String regionName;

    private ImageView imageView;
    private LifecycleOwner lifecycleOwner;

    public FlatCard(Context context) {
        super(context);
    }

    public FlatCard(Context context, Flat flat, Double price, String RegionName, LifecycleOwner lifecycleOwner){
        super(context);
        this.flat = flat;
        this.price = price;
        this.regionName = RegionName;
        this.lifecycleOwner = lifecycleOwner;
        initControl(context);
    }

    private void initControl(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.flat_card, this);

        imageView = findViewById(R.id.imageFlat);
        TextView tvName = findViewById(R.id.tvNameFlat);
        TextView tvPrice = findViewById(R.id.tvPriceFlat);
        TextView tvRegionName = findViewById(R.id.tvFlatRegionName);

        // Set information for Flat cart
        initImage();
        tvName.setText(flat.getName());
        tvPrice.setText(String.format("%s руб", Math.round(price)));
        tvRegionName.setText(regionName);
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
        flat.setImage(image);
        Bitmap bitmap = ImageUtils.convertByteArrayToBitmap(image);

        imageView.setImageBitmap(bitmap);
    }
}
