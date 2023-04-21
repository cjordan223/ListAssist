package com.example.listassist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.listassist.db.AppDatabase;
import com.example.listassist.db.ListAssistDAO;

public class AddUserActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mAddUserButton;

    private ListAssistDAO mListAssistDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        getDatabase();

        mUsernameEditText = findViewById(R.id.username_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        mAddUserButton = findViewById(R.id.add_user_button);

        Button backToAdminButton = findViewById(R.id.back_to_admin_button);
        backToAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingPage.intentFactory(AddUserActivity.this, 2);
                startActivity(intent);
            }
        });

        mAddUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                User user = new User(username, password);
                mListAssistDAO.insert(user);

                Toast.makeText(AddUserActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void getDatabase(){
        mListAssistDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getListAssistDAO();
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, AddUserActivity.class);
        return intent;
    }
}

