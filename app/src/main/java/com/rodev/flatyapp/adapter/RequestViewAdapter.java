package com.rodev.flatyapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import com.rodev.flatyapp.R;
import com.rodev.flatyapp.beans.FeedbackRequest;

public class RequestViewAdapter extends RecyclerView.Adapter<RequestViewAdapter.RequestViewHolder> {

    private final ArrayList<FeedbackRequest> feedbackRequests;

    public RequestViewAdapter() {
        this(new ArrayList<>());
    }

    public RequestViewAdapter(ArrayList<FeedbackRequest> feedbackRequests) {
        this.feedbackRequests = feedbackRequests;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.request_card, parent, false);

        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        FeedbackRequest request = feedbackRequests.get(position);

        holder.setFullName(request.getFullName());
        holder.setPhoneNumber(request.getPhoneNumber());
        holder.setRequestText(request.getRequest());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFeedbackRequests(Collection<FeedbackRequest> requests) {
        feedbackRequests.clear();
        feedbackRequests.addAll(requests);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return feedbackRequests.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        private final TextView fullNameTextView;
        private final TextView phoneNumberTextView;
        private final TextView requestTextView;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            fullNameTextView = itemView.findViewById(R.id.textView_full_name);
            phoneNumberTextView = itemView.findViewById(R.id.textView_phone_number);
            requestTextView = itemView.findViewById(R.id.textView_request_text);
        }

        public void setFullName(String fullName) {
            fullNameTextView.setText(fullName);
        }

        public void setPhoneNumber(String phoneNumber) {
            phoneNumberTextView.setText(phoneNumber);
        }

        public void setRequestText(String requestText) {
            requestTextView.setText(requestText);
        }
    }

}
