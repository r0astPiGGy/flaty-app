package hcmute.edu.vn.phamdinhquochoa.flatyapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.FavoriteRegionData;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;

public class RegionCard extends LinearLayout {
    private Region region;
    private boolean isSaved;
    private ImageView imageView;
    private LifecycleOwner lifecycleOwner;

    public RegionCard(Context context){
        super(context);
    }

    public RegionCard(Context context, Region Region, boolean isSaved, LifecycleOwner lifecycleOwner) {
        super(context);
        this.lifecycleOwner = lifecycleOwner;
        this.region = Region;
        this.isSaved = isSaved;
        initControl(context);
    }

    private void initControl(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.region_card, this);

        imageView = findViewById(R.id.imageRegion);
        TextView tvRegionName = findViewById(R.id.tvRegionName_res_cart);
        TextView tvRegionAddress = findViewById(R.id.tvRegionAddress_res_cart);

        Button btnSaved = findViewById(R.id.btnSavedRegion);
        if(isSaved){ btnSaved.setText("Удалить"); } // Thẻ được lưu
        btnSaved.setOnClickListener(view ->{
            String regionId = region.getId();
            FavoriteRegionData favoriteRegionData = DataAccess.getDataService().getFavoriteRegionData();
            if(isSaved){
                favoriteRegionData.removeFavorite(regionId).addOnCompleteListener(() -> {
                    Toast.makeText(context, "Сохранение информации о регионе прошло успешно!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(context, "Произошла ошибка при сохранении информации!", Toast.LENGTH_SHORT).show();
                });
            } else {
                favoriteRegionData.addFavorite(regionId).addOnCompleteListener(() -> {
                    Toast.makeText(context, "Сохранение информации о регионе прошло успешно!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(context, "Произошла ошибка при сохранении информации!", Toast.LENGTH_SHORT).show();
                });
            }
            isSaved = !isSaved;
        });

        // set information
        initImage();
        tvRegionName.setText(region.getName());
        tvRegionAddress.setText(region.getAddress());
    }

    private void initImage() {
        byte[] image = region.getImage();

        if(image == null) {
            DataAccess.getDataService()
                    .getImageStorage()
                    .getImageByUri(region.getId())
                    .observe(lifecycleOwner, this::initImage);

            return;
        }

        initImage(image);
    }

    private void initImage(byte[] image) {
        region.setImage(image);
        Bitmap bitmap = ImageUtils.convertByteArrayToBitmap(image);
        imageView.setImageBitmap(bitmap);
    }
}
