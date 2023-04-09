package com.rodev.flatyapp.utils;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditTextUtils {

    @Nullable
    public static String getInputOrNull(@NonNull TextView textView) {
        if(textView.getText() == null) return null;

        return textView.getText().toString().trim();
    }

}
