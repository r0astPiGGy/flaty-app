package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments.*;

public class HomeActivity extends AppCompatActivity {

    private static int clickToLogout;
    private static int stackLayout = 0;
    private Fragment homeFragment, savedFragment, notifyFragment, profileFragment;
    private BottomNavigationView navigation;

    private void referenceFragment(){
        homeFragment = new HomeFragment();
        savedFragment = new SavedFragment();
        notifyFragment = new NotifyFragment();
        profileFragment = new ProfileFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        stackLayout++;
        clickToLogout = 0;
        referenceFragment();

        Intent intent = getIntent();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String request = intent.getStringExtra("request");
        if(request != null) {
            switch (request) {
                case "history":
                case "check":
                case "payment":
                case "hint":
                    loadFragment(notifyFragment, 3);
                    break;
                default:
                    loadFragment(homeFragment, 0);
                    break;
            }
        } else {
            loadFragment(homeFragment, 0);
        }
    }

    @Override
    public void onBackPressed() {
        System.out.println(stackLayout);
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
                public void onTick(long l) {

                }

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

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        loadFragment(homeFragment, 0);
                        return true;
                    case R.id.navigation_saved:
                        loadFragment(savedFragment, 1);
                        return true;
                    case R.id.navigation_notify:
                        loadFragment(notifyFragment, 3);
                        return true;
                    case R.id.navigation_profile:
                        loadFragment(profileFragment, 2);
                        return true;
                }
                return false;
            };

    private void loadFragment(Fragment fragment, int indexItem) {
        navigation.getMenu().getItem(indexItem).setChecked(true);

        // load fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
    }
}