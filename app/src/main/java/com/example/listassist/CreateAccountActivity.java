package com.example.listassist;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.listassist.db.AppDatabase;
import com.example.listassist.db.ListAssistDAO;
import com.example.listassist.User;

import java.lang.ref.WeakReference;

public class CreateAccountActivity extends AppCompatActivity {
    EditText mUsernameInput;
    EditText mPasswordInput;
    Button mCreateAccountButton;
    Button mGoBackButton;

    private ListAssistDAO listAssistDAO;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mUsernameInput = findViewById(R.id.usernameInput);
        mPasswordInput = findViewById(R.id.passwordInput);
        mCreateAccountButton = findViewById(R.id.createAccountSubmitButton);
        mGoBackButton = findViewById(R.id.goBackButton);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();

        listAssistDAO = appDatabase.getListAssistDAO();

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsernameInput.getText().toString().trim();
                String password = mPasswordInput.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Please enter a valid username and password.", Toast.LENGTH_SHORT).show();
                } else {
                    createUser(username, password);
                }
            }
        });

        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void createUser(final String username, final String password) {
        new CreateUserTask(this, listAssistDAO).execute(username, password);
    }

    private static class CreateUserTask extends AsyncTask<String, Void, User> {
        private WeakReference<CreateAccountActivity> activityReference;
        private ListAssistDAO listAssistDAO;

        CreateUserTask(CreateAccountActivity context, ListAssistDAO listAssistDAO) {
            activityReference = new WeakReference<>(context);
            this.listAssistDAO = listAssistDAO;
        }

        @Override
        protected User doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            User existingUser = listAssistDAO.getUserByUsername(username);
            if (existingUser != null) {
                return null;
            } else {
                User newUser = new User(username, password);
                listAssistDAO.insert(newUser);
                return newUser;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            CreateAccountActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            if (user != null) {
                Toast.makeText(activity, "Account created successfully!", Toast.LENGTH_SHORT).show();
                activity.finish();
            } else {
                Toast.makeText(activity, "Username already exists. Please choose a different username.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
