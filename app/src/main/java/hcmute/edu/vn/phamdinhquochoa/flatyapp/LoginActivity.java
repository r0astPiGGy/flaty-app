package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.AuthData;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataService;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.UserData;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase.FirebaseDataService;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.view.AsyncButton;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFERENCES = "store_info" ;
    private SharedPreferences sharedPreferences;
    private EditText txtUsername, txtPassword;
    private CheckBox isAdminCheckbox;

    private String email;
    private String password;
    private boolean isAdmin;

    static {
        DataAccess.setDataService(new FirebaseDataService());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        referenceComponent();
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        txtUsername.setText(sharedPreferences.getString("username", ""));
        txtPassword.setText(sharedPreferences.getString("password", ""));
        isAdminCheckbox.setChecked(sharedPreferences.getBoolean("admin", false));
    }

    private void referenceComponent(){
        txtPassword = findViewById(R.id.editText_password_login);
        txtUsername = findViewById(R.id.editText_username_login);
        isAdminCheckbox = findViewById(R.id.checkbox_is_admin);

        AsyncButton btnLogin = findViewById(R.id.button_login_login);
        btnLogin.setOnClickListener(view -> onLoginButtonClicked(btnLogin));

        Button btnSignup = findViewById(R.id.button_signup_login);
        btnSignup.setOnClickListener(view -> onRegisterButtonClicked());
    }

    private boolean inputInvalid() {
        fillInput();

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните форму!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    private void fillInput() {
        email = txtUsername.getText().toString().trim();
        password = txtPassword.getText().toString().trim();
        isAdmin = isAdminCheckbox.isChecked();
    }

    private void onLoginButtonClicked(AsyncButton button) {
        if (inputInvalid()) return;

        button.enableWaitMode();
        DataAccess.getDataService()
                .getAuthData()
                .signIn(email, password)
                .addOnCompleteListener(() -> loginSuccess(button))
                .addOnFailureListener(e -> {
                    button.disableWaitMode();
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void loginSuccess(AsyncButton button) {
        sharedPreferences.edit()
                .putString("username", email)
                .putString("password", password)
                .putBoolean("admin", isAdmin)
                .apply();

        User.Role role = isAdmin ? User.Role.ADMIN : User.Role.USER;

        DataService dataService = DataAccess.getDataService();
        AuthData authData = dataService.getAuthData();
        UserData userData = dataService.getUserData();

        userData.getUserById(authData.getUserId()).thenAccept(user -> {
            user.setRole(role);
            button.disableWaitMode();

            userData.updateUser(user);

            authData.setUser(user);

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }

    private void onRegisterButtonClicked() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            txtUsername.setText(data.getStringExtra("username"));
            txtPassword.setText(data.getStringExtra("password"));
        }
    }
}