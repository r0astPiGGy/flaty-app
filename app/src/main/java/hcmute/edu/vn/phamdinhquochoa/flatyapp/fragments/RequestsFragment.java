package hcmute.edu.vn.phamdinhquochoa.flatyapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.FragmentRequestsBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.adapter.RequestViewAdapter;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FeedbackRequest;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;

public class RequestsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final RequestViewAdapter adapter = new RequestViewAdapter();
    private FragmentRequestsBinding binding;

    public RequestsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRequestsBinding.inflate(inflater, container, false);
        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        binding.requestRecyclerView.setAdapter(adapter);

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
                .getFeedbackRequestData()
                .getAllRequests()
                .observe(getViewLifecycleOwner(), this::onRefreshEnded);
    }

    private void onRefreshEnded(List<FeedbackRequest> feedbackRequests) {
        binding.swipeRefreshLayout.setRefreshing(false);
        adapter.setFeedbackRequests(feedbackRequests);
    }
}
