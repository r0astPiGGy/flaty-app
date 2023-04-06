package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DAO;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments.*;

public class HomeActivity extends AppCompatActivity {

    private static int clickToLogout;
    private static int stackLayout = 0;
    public static DAO dao;
    public static User user;
    private Fragment homeFragment, savedFragment, chatFragment, notifyFragment, profileFragment;
    private BottomNavigationView navigation;

    private void referenceFragment(){
        homeFragment = new HomeFragment();
        savedFragment = new SavedFragment();
//        chatFragment = new ChatFragment();
        notifyFragment = new NotifyFragment();
        profileFragment = new ProfileFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dao = new DAO(this);

        Integer userID = user.getId();

        FlatDetailsActivity.userID = userID;
//        ViewOrderActivity.userID = userID;

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
//                case "cart":
//                    loadFragment(chatFragment, 2);
//                    break;
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
                    case R.id.navigation_chat:
                        loadFragment(chatFragment, 4);
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        transaction.commit();
    }
}