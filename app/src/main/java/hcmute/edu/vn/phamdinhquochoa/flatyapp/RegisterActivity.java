package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import android.content.Intent;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Notify;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataService;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.view.AsyncButton;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtUsername, txtPassword, txtPasswordConfirm;
    private String username, password, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        referenceComponent();
    }

    private void referenceComponent(){
        txtUsername = findViewById(R.id.editText_username_signup);
        txtPassword = findViewById(R.id.editText_password_signup);
        txtPasswordConfirm = findViewById(R.id.editText_password_signup_confirm);

        AsyncButton btnSignup = findViewById(R.id.button_signup_signup);
        btnSignup.setOnClickListener(view -> onRegisterButtonClicked(btnSignup));

        Button btnLogin = findViewById(R.id.button_login_signup);
        btnLogin.setOnClickListener(view -> finish());
    }

    private boolean inputInvalid() {
        fillInput();

        if(username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните форму!", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(!confirm.equals(password)){
            Toast.makeText(this, "Неверный пароль подтверждения!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    private void fillInput() {
        username = txtUsername.getText().toString().trim();
        password = txtPassword.getText().toString().trim();
        confirm = txtPasswordConfirm.getText().toString().trim();
    }

    private void onRegisterButtonClicked(AsyncButton button) {
        if (inputInvalid()) return;

        button.enableWaitMode();

        DataAccess.getDataService()
                .getAuthData()
                .register(username, password)
                .addOnCompleteListener(() -> {
                    button.disableWaitMode();
                    onUserRegistered();
                })
                .addOnFailureListener(e -> {
                    button.disableWaitMode();
                    onRegistrationFailed(e);
                });
    }

    private void onUserRegistered() {
        DataService dataService = DataAccess.getDataService();
        String userId = dataService.getAuthData().getUserId();

        dataService.getUserData().addUser(new User(userId, "", "Male", "1/1/2000", "", username, password));
        Toast.makeText(this, "Учетная запись успешно создана!", Toast.LENGTH_SHORT).show();

        Notify notify = new Notify(
                "Добро пожаловать!",
                "Благодарим вас за использование приложения! \n" +
                "Пожалуйста, измените свою личную информацию, нажав на значок пользователя в разделе профиля!",
                userId
        );

        dataService.getNotificationData().addNotify(notify);

        // Return Login
        Intent intent = new Intent();
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        setResult(0, intent);
        finish();
    }

    private void onRegistrationFailed(Throwable e) {
        e.printStackTrace();
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}