package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Notify;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.NotifyToUser;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DAO;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtUsername, txtPassword, txtPasswordConfirm;
    private String username, password, confirm;
    public DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        referenceComponent();
        dao = new DAO(this);
    }

    private void referenceComponent(){
        txtUsername = findViewById(R.id.editText_username_signup);
        txtPassword = findViewById(R.id.editText_password_signup);
        txtPasswordConfirm = findViewById(R.id.editText_password_signup_confirm);

        Button btnSignup = findViewById(R.id.button_signup_signup);
        btnSignup.setOnClickListener(view -> {
            username = txtUsername.getText().toString().trim();
            password = txtPassword.getText().toString().trim();
            confirm = txtPasswordConfirm.getText().toString().trim();

            if(username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните форму!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!confirm.equals(password)){
                Toast.makeText(this, "Неверный пароль подтверждения!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(dao.UserExited(username)){
                Toast.makeText(this, "Пользователь уже существует!", Toast.LENGTH_SHORT).show();
            } else {
                dao.addUser(new User(null, "", "Male", "1/1/2000", "", username, password));
                Toast.makeText(this, "Учетная запись успешно создана!", Toast.LENGTH_SHORT).show();

                // Make notify
                dao.addNotify(new Notify(1, "Добро пожаловать !", "Благодарим вас за использование приложения! \n" +
                        "Пожалуйста, измените свою личную информацию, нажав на значок пользователя в разделе профиля!",
                        dao.getDate()));
                dao.addNotifyToUser(new NotifyToUser(dao.getNewestNotifyId(), dao.getNewestUserId()));

                // Return Login
                Intent intent = new Intent();
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                setResult(0, intent);
                finish();
            }
        });

        Button btnLogin = findViewById(R.id.button_login_signup);
        btnLogin.setOnClickListener(view -> finish());
    }
}