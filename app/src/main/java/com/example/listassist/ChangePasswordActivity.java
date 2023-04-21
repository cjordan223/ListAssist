package com.example.listassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listassist.db.AppDatabase;
import com.example.listassist.db.ListAssistDAO;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.listAssist.userIdKey";
    ListAssistDAO mListAssistDAO;
    User mUser;
    EditText mCurrentPasswordEditText;
    EditText mNewPasswordEditText;
    EditText mConfirmPasswordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getDatabase();
        checkForUser();
        mUser = mListAssistDAO.getUserByID(getIntent().getIntExtra(USER_ID_KEY, -1));

        mCurrentPasswordEditText = findViewById(R.id.current_password_edit_text);
        mNewPasswordEditText = findViewById(R.id.new_password_edit_text);
        mConfirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);

        Button changePasswordButton = findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = mCurrentPasswordEditText.getText().toString();
                String newPassword = mNewPasswordEditText.getText().toString();
                String confirmPassword = mConfirmPasswordEditText.getText().toString();

                if (currentPassword.equals(mUser.getPassword()) && newPassword.equals(confirmPassword)) {
                    mUser.setPassword(newPassword);
                    mListAssistDAO.update(mUser);
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (!currentPassword.equals(mUser.getPassword())) {
                    Toast.makeText(ChangePasswordActivity.this, "Incorrect current password.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "New passwords do not match.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changePasswordForUser(int userID) {
        Intent intent = ChangePasswordActivity.getIntent(this, userID);
        startActivity(intent);
    }

    private void getDatabase(){
        mListAssistDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getListAssistDAO();
    }

    private void checkForUser(){
        int userID = getIntent().getIntExtra(USER_ID_KEY, -1);
        if(userID == -1 || mListAssistDAO.getUserByID(userID) == null) {
            finish();
        }
    }

    public static Intent getIntent(Context context, int userID) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        intent.putExtra(USER_ID_KEY, userID);
        return intent;
    }

}
