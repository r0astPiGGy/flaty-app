package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class FirebaseUser {

    public static String getUniqueId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
