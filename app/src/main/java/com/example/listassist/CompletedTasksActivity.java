package com.example.listassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.example.listassist.db.AppDatabase;
import com.example.listassist.db.ListAssistDAO;
import java.util.List;

public class CompletedTasksActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.listAssist.userIdKey";
    private int mUserID = -1;
    ListAssistDAO mListAssistDAO;
    ListView mCompletedTasksListView;
    ArrayAdapter<String> mCompletedTasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);

        mUserID = getIntent().getIntExtra(USER_ID_KEY, -1);

        getDatabase();

        mCompletedTasksListView = findViewById(R.id.completed_tasks_listview);

        List<CompletedTask> completedTasks = mListAssistDAO.getCompletedTasksByUserID(mUserID);
        mCompletedTasksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mCompletedTasksListView.setAdapter(mCompletedTasksAdapter);

        Button backToLandingButton = findViewById(R.id.button_back_to_landing);
        backToLandingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompletedTasksActivity.this, LandingPage.class);
                intent.putExtra(USER_ID_KEY, mUserID); // Pass the user ID back to the LandingPage
                startActivity(intent);
            }
        });

        for (CompletedTask completedTask : completedTasks) {
            mCompletedTasksAdapter.add(completedTask.getTaskText());
        }
    }

    public static Intent intentFactory(Context context, int userID) {
        Intent intent = new Intent(context, CompletedTasksActivity.class);
        intent.putExtra(USER_ID_KEY, userID);
        return intent;
    }

    private void getDatabase() {
        mListAssistDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getListAssistDAO();
    }
}
