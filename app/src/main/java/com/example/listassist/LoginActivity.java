package com.example.listassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listassist.db.AppDatabase;
import com.example.listassist.db.ListAssistDAO;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private TextView mDisplay;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
    private ListAssistDAO mListAssistDAO;
    private int mUserId = -1;
    private Button mGoBackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.userName);
        mPassword = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.loginButton);
        mDisplay = findViewById(R.id.display);
        mDisplay.setMovementMethod(new ScrollingMovementMethod());

        getDatabase();
        initializeDatabase();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = getValuesFromDisplay();
                if(checkUserInDatabase(user)){
                    if(!validatePassword(user)){
                        Util.toastMaker(LoginActivity.this, "Wrong Password");
                    }else{
                        User current  = mListAssistDAO.getUserByID(mUserId);
                        if(user.getAdmin()){

                        }else{
                            Intent intent = LandingPage.intentFactory(getApplicationContext(), mUserId);
                            startActivity(intent);
                        }

                    }
                }

            }
        });

        mGoBackButton = findViewById(R.id.goBackButton);
        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
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

    private void initializeDatabase(){
        if(mListAssistDAO.getUserByUsername("admin2") == null || mListAssistDAO.getUserByUsername("testuser1") == null){
            mListAssistDAO.purgeUserTable();
            User testUser = new User("testuser1", "testuser1");
            User admin = new User("admin2", "admin2");
            admin.setAdmin(true);
            mListAssistDAO.insert(testUser);
            mListAssistDAO.insert(admin);
        }
    }

    private boolean validatePassword(User user){
        if(user != null){
            User userFromDB = mListAssistDAO.getUserByUsername(user.getUsername());
            if(userFromDB.getPassword().equals(user.getPassword())){
                return true;
            }
        }
        return false;
    }
    private boolean checkUserInDatabase(User user){

        if(user == null || mListAssistDAO.getUserByUsername(user.getUsername()) == null ){
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            return false;
        }
        mUserId = mListAssistDAO.getUserByUsername(user.getUsername()).getUserID();
        return true;
    }

    private User getValuesFromDisplay(){
        String username;
        String password;

        username = mUsername.getText().toString();
        password = mPassword.getText().toString();

        return new User(username, password);
    }

//    private void refreshDisplay(){
//        mUsers = mListAssistDAO.getUsers();
//        StringBuilder sb = new StringBuilder();
//
//        if(mUsers.size() <= 0){
//            mDisplay.setText(R.string.no_users);
//            return;
//        }
//        for(User user: mUsers){
//            sb.append(user);
//            sb.append("\n");
//        }
//        mDisplay.setText(sb.toString());
//    }

    public static Intent getIntent(Context context){
        Intent intent =  new Intent(context, LoginActivity.class);
        return intent;
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
        return intent;
    }
}