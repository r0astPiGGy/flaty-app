package com.rodev.flatyapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rodev.flatyapp.HomeActivity;
import com.rodev.flatyapp.R;
import com.rodev.flatyapp.UserInformationActivity;
import com.rodev.flatyapp.dao.DataAccess;

public class ProfileFragment extends Fragment {
    private View mainView;
    private Intent intent;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_profile, container, false);
        referenceComponent();
        return mainView;
    }

    private void referenceComponent(){
        LinearLayout user_information = mainView.findViewById(R.id.layout_user_information);
        user_information.setOnClickListener(view -> startActivity(new Intent(getActivity(), UserInformationActivity.class)));

        LinearLayout hint = mainView.findViewById(R.id.account_btn_layout_hint);
        hint.setOnClickListener(view -> {
            intent = new Intent(getActivity(), HomeActivity.class);
            intent.putExtra("request", "hint");
            startActivity(intent);
        });

        LinearLayout logout = mainView.findViewById(R.id.account_btn_layout_logout);
        logout.setOnClickListener(view -> {
            Toast.makeText(this.getActivity(),
                    "Выход из системы!",
                    Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        });

        TextView txtUser_name = mainView.findViewById(R.id.account_user_name);
        txtUser_name.setText(DataAccess.getUser().getName());
    }
}