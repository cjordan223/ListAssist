package com.example.listassist;



import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.listassist.db.AppDatabase;
import com.example.listassist.db.ListAssistDAO;

public class DeleteUserActivity extends AppCompatActivity {

    TextView mUsername;
    Button mDeleteButton;
    ListAssistDAO mListAssistDAO;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        mUsername = findViewById(R.id.usernametodelete);
        mDeleteButton = findViewById(R.id.deleteUser);
        Button backToAdminButton = findViewById(R.id.back_to_admin_button);

        backToAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingPage.intentFactory(DeleteUserActivity.this, 2);
                startActivity(intent);
            }
        });


        System.out.println(username);

        getDatabase();

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = mUsername.getText().toString();

                if(mListAssistDAO.getUserByUsername(username) != null){
                    User user  = mListAssistDAO.getUserByUsername(username);
                    mListAssistDAO.delete(user);
                    Util.toastMaker(getApplicationContext(), "User Deleted");
                }else{
                    Util.toastMaker(getApplicationContext(), "No User by that name found.");
                }

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
        Intent intent =  new Intent(context, DeleteUserActivity.class);
        return intent;
    }
}

