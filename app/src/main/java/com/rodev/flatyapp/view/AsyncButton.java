package com.rodev.flatyapp.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec;
import com.google.android.material.progressindicator.IndeterminateDrawable;
import com.rodev.flatyapp.R;

public class AsyncButton extends MaterialButton {

    private Drawable loadingIcon;

    public AsyncButton(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AsyncButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AsyncButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        CircularProgressIndicatorSpec spec =
                new CircularProgressIndicatorSpec(context, null, 0, R.style.ThemeOverlay_App_CircularProgressIndicator);
        loadingIcon = IndeterminateDrawable.createCircularDrawable(context, spec);
    }

    public void enableWaitMode() {
        setEnabled(false);
        setIcon(loadingIcon);
    }

    public void disableWaitMode() {
        setEnabled(true);
        setIcon(null);
    }
}
