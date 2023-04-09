package com.rodev.flatyapp.data.firebase;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseUser {

    public static String getUniqueId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
