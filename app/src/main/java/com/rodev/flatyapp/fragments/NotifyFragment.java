package com.rodev.flatyapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import com.rodev.flatyapp.adapter.NotifyViewAdapter;
import com.rodev.flatyapp.beans.Notify;
import com.rodev.flatyapp.dao.DataAccess;
import com.rodev.flatyapp.databinding.FragmentNotifyBinding;

public class NotifyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final NotifyViewAdapter adapter = new NotifyViewAdapter();
    private FragmentNotifyBinding binding;

    public NotifyFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotifyBinding.inflate(inflater, container, false);
        initViews();

        return binding.getRoot();
    }
    
    private void initViews(){
        binding.recyclerView.setAdapter(adapter);

        initRefreshLayout(binding.swipeRefreshLayout);
    }

    private void initRefreshLayout(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.post(() -> {
            refreshLayout.setRefreshing(true);
            onRefresh();
        });
    }

    @Override
    public void onRefresh() {
        DataAccess.getDataService()
                .getNotificationData()
                .getAllNotifications()
                .observe(getViewLifecycleOwner(), this::onNotificationLoaded);
    }

    private void onNotificationLoaded(List<Notify> notifies) {
        binding.swipeRefreshLayout.setRefreshing(false);
        adapter.setNotifications(notifies);
    }
}
