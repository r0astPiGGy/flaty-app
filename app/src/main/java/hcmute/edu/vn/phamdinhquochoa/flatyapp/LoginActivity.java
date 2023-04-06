package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DAO;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFERENCES = "store_info" ;
//    public static final Integer DEFAULT_USER_ID = 1;
    private SharedPreferences sharedPreferences;
    private EditText txtUsername, txtPassword;
    private CheckBox isAdminCheckbox;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        referenceComponent();
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        txtUsername.setText(sharedPreferences.getString("username", ""));
        txtPassword.setText(sharedPreferences.getString("password", ""));
        isAdminCheckbox.setChecked(sharedPreferences.getBoolean("admin", false));
        dao = new DAO(this);
    }

    private void referenceComponent(){
        txtPassword = findViewById(R.id.editText_password_login);
        txtUsername = findViewById(R.id.editText_username_login);
        isAdminCheckbox = findViewById(R.id.checkbox_is_admin);

        Button btnLogin = findViewById(R.id.button_login_login);
        btnLogin.setOnClickListener(view -> {
            String username = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            boolean isAdmin = isAdminCheckbox.isChecked();

            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните форму!", Toast.LENGTH_SHORT).show();
                return;
            }

            User targetUser = dao.getUserByUsernameAndPassword(username, password);

            boolean isRightAuthentication = false;
            if(targetUser != null){
                isRightAuthentication = dao.signIn(targetUser);
            }
            if(isRightAuthentication){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", targetUser.getUsername());
                editor.putString("password", targetUser.getPassword());
                editor.putBoolean("admin", isAdmin);
                editor.apply();

                User.Role role = isAdmin ? User.Role.ADMIN : User.Role.USER;

                targetUser.setRole(role);
                dao.updateUser(targetUser);

//                Integer userId = sharedPreferences.getInt("UserID", DEFAULT_USER_ID);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                HomeActivity.user = targetUser;
                startActivity(intent);
            } else{
                Toast.makeText(this, "Неверные данные для входа", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnSignup = findViewById(R.id.button_signup_login);
        btnSignup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, 0);
        });
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