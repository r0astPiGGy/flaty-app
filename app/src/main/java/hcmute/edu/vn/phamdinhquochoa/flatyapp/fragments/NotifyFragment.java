package hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.HomeActivity;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Notify;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.components.NotifyCard;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;

public class NotifyFragment extends Fragment {

    private LinearLayout notifyContainer;

    public NotifyFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_notify, container, false);
        
        notifyContainer = mainView.findViewById(R.id.layout_notify);

        LoadNotify();

        return mainView;
    }
    
    private void LoadNotify(){
        DataAccess.getDataService()
                .getNotificationData()
                .getAllNotifications()
                .observe(this, this::onNotificationLoaded);
    }

    // TODO: create recycler
    private void onNotificationLoaded(List<Notify> notifies) {
        notifyContainer.removeAllViews();
        for (Notify notify : notifies){
            notifyContainer.addView(new NotifyCard(getActivity(), notify));
        }
    }
}
