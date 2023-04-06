package hcmute.edu.vn.phamdinhquochoa.flatyapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.Flatyapp.databinding.ActivityUserInformationBinding;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Notify;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;

public class UserInformationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityUserInformationBinding binding;
    private Calendar calendar;
    private String newUser_name, newUser_phone, newUser_DoB, newUser_gender, newUser_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInformationBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        referencesComponent();

        User user = DataAccess.getUser();

        binding.editTextUserName.setText(user.getName());
        binding.editTextUserPhone.setText(user.getPhone());
        binding.userBirthdayPick.setText(user.getDateOfBirth());
        binding.editTextUserPassword.setText(user.getPassword());

        int selection = 2;

        switch (user.getGender()){
            case "Male": selection = 0; break;
            case "Female": selection = 1; break;
        }

        binding.spinnerUserGender.setSelection(selection);
    }

    private void referencesComponent(){
        binding.userBirthdayPick.setOnClickListener(view -> PickDate());

        Spinner spUser_gender = binding.spinnerUserGender;
        ArrayAdapter<CharSequence> genders = ArrayAdapter.createFromResource(
                this,
                R.array.genders,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        );
        genders.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spUser_gender.setAdapter(genders);
        spUser_gender.setOnItemSelectedListener(this);

        binding.btnChangeUserInformation.setOnClickListener(view -> onChangeButtonClicked());
        binding.btnCancelChangeUserInformation.setOnClickListener(view -> finish());
    }

    private void fillUserInput() {
        newUser_name = binding.editTextUserName.getText().toString().trim();
        newUser_gender = binding.spinnerUserGender.getSelectedItem().toString();
        newUser_phone = binding.editTextUserPhone.getText().toString().trim();
        newUser_DoB = binding.userBirthdayPick.getText().toString();
        newUser_password = binding.editTextUserPassword.getText().toString();
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

        User user = DataAccess.getUser();

        Objects.requireNonNull(user, "User cannot be null!");

        String oldPassword = user.getPassword();
        User editUser = user.copyAndApply(newUser -> {
            newUser.setName(newUser_name);
            newUser.setGender(newUser_gender);
            newUser.setDateOfBirth(newUser_DoB);
            newUser.setPhone(newUser_phone);
            newUser.setPassword(newUser_password);
        });

        DataAccess.getDataService().getUserData().updateUser(editUser);

        if(!oldPassword.equals(newUser_password)){
            DataAccess.getDataService().getAuthData().changePassword(newUser_password);
            Toast.makeText(this, "Пожалуйста, войдите снова!", Toast.LENGTH_SHORT).show();

            Notify notify = new Notify(
                    "Пароль изменен!",
                    "Пожалуйста, повторно войдите в приложение, чтобы обновить новую личную информацию!",
                    user.getId()
            );

            DataAccess.getDataService().getNotificationData().addNotify(notify);
        } else {
            Toast.makeText(this, "Информация успешно изменена!", Toast.LENGTH_SHORT).show();
        }
        DataAccess.getDataService().getAuthData().setUser(editUser);

        finish();
    }

    private void PickDate(){
        calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            calendar.set(year, month, day);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            binding.userBirthdayPick.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        binding.spinnerUserGender.setTextAlignment(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        binding.spinnerUserGender.setTransitionName(DataAccess.getUser().getGender());
    }
}