package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.ActivityHomeBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.adapter.ViewPagerAdapter;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments.*;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private static int clickToLogout;
    private static int stackLayout = 0;

    private final int[] navbar_ids = {
            R.id.navigation_home,
            R.id.navigation_saved,
            R.id.navigation_notify,
            R.id.navigation_chat,
            R.id.navigation_profile
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        stackLayout++;
        clickToLogout = 0;

        ViewPagerAdapter adapter = ViewPagerAdapter.of(this,
                new HomeFragment(),
                new SavedFragment(),
                new NotifyFragment(),
                new ChatFragment(),
                new ProfileFragment()
        );

        binding.mainPager.setAdapter(adapter);

        binding.mainPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                int id = getNavigationItemIdByPosition(position);
                binding.navigation.setSelectedItemId(id);
            }
        });

        binding.navigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            int position = getNavigationPositionById(id);
            binding.mainPager.setCurrentItem(position);
            return true;
        });
    }

    private int getNavigationItemIdByPosition(int position) {
        return navbar_ids[position];
    }

    @SuppressLint("NonConstantResourceId")
    private int getNavigationPositionById(int id) {
        switch (id) {
            case R.id.navigation_home: return 0;
            case R.id.navigation_saved: return 1;
            case R.id.navigation_notify: return 2;
            case R.id.navigation_chat: return 3;
            case R.id.navigation_profile: return 4;
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        if(stackLayout < 2){
            clickToLogout++;

            if(clickToLogout > 1){
                finish();
                stackLayout--;
            } else {
                Toast.makeText(this, "Нажмите еще раз, чтобы выйти!", Toast.LENGTH_SHORT).show();
            }

            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long l) {}

                @Override
                public void onFinish() {
                    clickToLogout = 0;
                }
            }.start();
        } else {
            stackLayout--;
            super.onBackPressed();
        }
    }
}