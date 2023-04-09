package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.ActivityLoginBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.AuthData;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataService;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.UserData;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase.FirebaseDataService;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.view.AsyncButton;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFERENCES = "store_info" ;

    private ActivityLoginBinding binding;
    private SharedPreferences sharedPreferences;
    private EditText txtUsername, txtPassword;

    private String email;
    private String password;

    static {
        DataAccess.setDataService(new FirebaseDataService());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        initViews();
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        txtUsername.setText(sharedPreferences.getString("username", ""));
        txtPassword.setText(sharedPreferences.getString("password", ""));
    }

    private void initViews(){
        txtPassword = findViewById(R.id.editText_password_login);
        txtUsername = findViewById(R.id.editText_username_login);

        AsyncButton buttonLoginAsAdmin = binding.buttonLoginAsAdmin;
        buttonLoginAsAdmin.setOnClickListener(view -> onLoginButtonClicked(User.Role.ADMIN));

        AsyncButton buttonLoginAsUser = binding.buttonLoginAsUser;
        buttonLoginAsUser.setOnClickListener(view -> onLoginButtonClicked(User.Role.USER));

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
    }

    private void onLoginButtonClicked(User.Role role) {
        if (inputInvalid()) return;

        enableWaitMode();
        DataAccess.getDataService()
                .getAuthData()
                .signIn(email, password)
                .addOnCompleteListener(() -> loginSuccess(role))
                .addOnFailureListener(e -> {
                    disableWaitMode();
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void enableWaitMode() {
        binding.buttonLoginAsAdmin.enableWaitMode();
        binding.buttonLoginAsUser.enableWaitMode();
    }

    private void disableWaitMode() {
        binding.buttonLoginAsAdmin.disableWaitMode();
        binding.buttonLoginAsUser.disableWaitMode();
    }

    private void loginSuccess(User.Role role) {
        sharedPreferences.edit()
                .putString("username", email)
                .putString("password", password)
                .apply();

        DataService dataService = DataAccess.getDataService();
        AuthData authData = dataService.getAuthData();
        UserData userData = dataService.getUserData();

        userData.getUserById(authData.getUserId()).thenAccept(user -> {
            user.setRole(role);

            userData.updateUser(user);
            authData.setUser(user);

            disableWaitMode();
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