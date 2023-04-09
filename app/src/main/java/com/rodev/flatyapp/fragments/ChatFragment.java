package com.rodev.flatyapp.fragments;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.rodev.flatyapp.utils.EditTextUtils.getInputOrNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import com.rodev.flatyapp.R;
import com.rodev.flatyapp.beans.FeedbackRequest;
import com.rodev.flatyapp.dao.DataAccess;
import com.rodev.flatyapp.databinding.FragmentChatBinding;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;

    private String fullName;
    private String phoneNumber;
    private String requestText;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater);
        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        binding.buttonSendFeedback.setOnClickListener(v -> onFeedbackButtonClicked());
    }

    private void fillInput() {
        fullName = getInputOrNull(binding.credentialsTextField);
        phoneNumber = getInputOrNull(binding.phoneNumberTextField);
        requestText = getInputOrNull(binding.requestTextField);
    }

    private boolean isInputInvalid() {
        if(isAtLeastOneNullOrEmpty(fullName, phoneNumber, requestText)) {
            makeSnackBar(R.string.fill_blanks);
            return true;
        }

        return false;
    }

    private void makeSnackBar(int text) {
        Snackbar.make(binding.getRoot(), text, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    private void makeSnackBar(String text) {
        Snackbar.make(binding.getRoot(), text, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    private boolean isAtLeastOneNullOrEmpty(String... input) {
        for (String s : input) {
            if(isNullOrEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    private void resetTextFor(TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setText(null);
        }
    }

    private void onFeedbackButtonClicked() {
        fillInput();

        if(isInputInvalid()) return;

        FeedbackRequest feedbackRequest = new FeedbackRequest(fullName, phoneNumber, requestText);

        DataAccess.getDataService()
                .getFeedbackRequestData()
                .addRequest(feedbackRequest)
                .addOnCompleteListener(this::onRequestSent)
                .addOnFailureListener(this::onRequestSendFailed);
    }

    private void onRequestSent() {
        makeSnackBar(R.string.request_sent);
        resetTextFor(
                binding.credentialsTextField,
                binding.phoneNumberTextField,
                binding.requestTextField
        );
    }

    private void onRequestSendFailed(Throwable throwable) {
        makeSnackBar(throwable.getLocalizedMessage());
    }
}