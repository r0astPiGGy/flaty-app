package hcmute.edu.vn.phamdinhquochoa.flatyapp.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.FavoriteRegionData;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.utils.ImageUtils;

public class RegionCard extends LinearLayout {
    private Region Region;
    private boolean isSaved;
    
    public RegionCard(Context context, Region Region, boolean isSaved) {
        super(context);
        this.Region = Region;
        this.isSaved = isSaved;
        initControl(context);
    }

    public RegionCard(Context context){
        super(context);
    }
    
    private void initControl(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.region_card, this);

        ImageView image = findViewById(R.id.imageRegion);
        TextView tvRegionName = findViewById(R.id.tvRegionName_res_cart);
        TextView tvRegionAddress = findViewById(R.id.tvRegionAddress_res_cart);

        Button btnSaved = findViewById(R.id.btnSavedRegion);
        if(isSaved){ btnSaved.setText("Удалить"); } // Thẻ được lưu
        btnSaved.setOnClickListener(view ->{
            String regionId = Region.getId();
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
        });

        // set information
        image.setImageBitmap(ImageUtils.convertByteArrayToBitmap(Region.getImage()));
        tvRegionName.setText(Region.getName());
        tvRegionAddress.setText(Region.getAddress());
    }
}
