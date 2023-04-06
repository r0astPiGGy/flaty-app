package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Notify;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.NotifyToUser;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DAO;

public class UserInformationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edUser_name, edUser_phone, edUser_DoB, edUser_password;
    private Spinner spUser_gender;
    private Calendar calendar;
    private String newUser_name, newUser_phone, newUser_DoB, newUser_gender, newUser_password;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        referencesComponent();

        User user = HomeActivity.user;

        edUser_name.setText(user.getName());
        edUser_phone.setText(user.getPhone());
        edUser_DoB.setText(user.getDateOfBirth());
        edUser_password.setText(user.getPassword());

        switch (HomeActivity.user.getGender()){
            case "Male":
                spUser_gender.setSelection(0);
                break;
            case "Female":
                spUser_gender.setSelection(1);
                break;
            default:
                spUser_gender.setSelection(2);
                break;
        }

        dao = new DAO(this);
    }

    private void referencesComponent(){
        edUser_name = findViewById(R.id.editText_user_name);
        edUser_phone = findViewById(R.id.editText_user_phone);
        edUser_DoB = findViewById(R.id.user_birthday_pick);
        edUser_DoB.setOnClickListener(view -> PickDate());

        edUser_password = findViewById(R.id.editText_user_password);

        spUser_gender = findViewById(R.id.spinner_user_gender);
        ArrayAdapter<CharSequence> genders = ArrayAdapter.createFromResource(this, R.array.genders, android.support.design.R.layout.support_simple_spinner_dropdown_item);
        genders.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
        spUser_gender.setAdapter(genders);
        spUser_gender.setOnItemSelectedListener(this);

        Button btnChange = findViewById(R.id.btnChangeUserInformation);
        btnChange.setOnClickListener(view -> onChangeButtonClicked());

        Button btnCancel = findViewById(R.id.btnCancelChangeUserInformation);
        btnCancel.setOnClickListener(view -> finish());
    }

    private void fillUserInput() {
        newUser_name = edUser_name.getText().toString().trim();
        newUser_gender = spUser_gender.getSelectedItem().toString();
        newUser_phone = edUser_phone.getText().toString().trim();
        newUser_DoB = edUser_DoB.getText().toString();
        newUser_password = edUser_password.getText().toString();
    }

    private boolean isUserInputEmpty() {
        return newUser_name.isEmpty() || newUser_phone.isEmpty() || newUser_DoB.isEmpty() || newUser_password.isEmpty();
    }

    private void onChangeButtonClicked() {
        fillUserInput();

        if(isUserInputEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните всю информацию!", Toast.LENGTH_SHORT).show();
            return;
        }

        String oldPassword = HomeActivity.user.getPassword();
        User editUser = HomeActivity.user.copyAndApply(newUser -> {
            newUser.setName(newUser_name);
            newUser.setGender(newUser_gender);
            newUser.setDateOfBirth(newUser_DoB);
            newUser.setPhone(newUser_phone);
            newUser.setPassword(newUser_password);
        });

        dao.updateUser(editUser);

        if(!oldPassword.equals(newUser_password)){
            Toast.makeText(this, "Пожалуйста, войдите снова!", Toast.LENGTH_SHORT).show();
            // Make notify
            dao.addNotify(new Notify(1, "Пароль изменен!",
                    "Пожалуйста, повторно войдите в приложение, чтобы обновить новую личную информацию!",
                    dao.getDate()));
            dao.addNotifyToUser(new NotifyToUser(dao.getNewestNotifyId(), dao.getNewestUserId()));
        } else {
            Toast.makeText(this, "Информация успешно изменена!", Toast.LENGTH_SHORT).show();
        }

        HomeActivity.user = editUser;
        finish();
    }

    private void PickDate(){
        calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            calendar.set(year, month, day);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            edUser_DoB.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spUser_gender.setTextAlignment(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        spUser_gender.setTransitionName(HomeActivity.user.getGender());
    }
}