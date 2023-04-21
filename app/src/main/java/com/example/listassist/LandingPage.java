package com.example.listassist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.listassist.db.AppDatabase;
import com.example.listassist.db.ListAssistDAO;

import java.util.List;

public class LandingPage extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.listAssist.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.listAssist.PREFERENCES_KEY";
    private int mUserID = -1;
    ListAssistDAO mListAssistDAO;
    TextView mUserInfoDisplay;
    Button mAdminButton;
    User mUser;
    TextView mAdminDisplay;
    Button mDeleteButton;
    Button mAddButton;
    ListView mItemsListView;
    ArrayAdapter<String> mItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDatabase();
        checkForUser();
        mUser = mListAssistDAO.getUserByID(mUserID);

        if(mUser.getUserID() == 2){
            setContentView(R.layout.admin_landing_page);
            mAdminDisplay = findViewById(R.id.welcomeadminview);
            mDeleteButton = findViewById(R.id.deleteuserbutton);
            mAddButton = findViewById(R.id.adduserbutton);
            mAdminButton  = findViewById(R.id.admin_welcome_button);
            Button changePasswordButton = findViewById(R.id.change_user_password_button);


            mAdminButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(LandingPage.this, "King of the castle!", Toast.LENGTH_SHORT).show();
                }
            });
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent  = DeleteUserActivity.getIntent(getApplicationContext());
                    startActivity(intent);
                }
            });
            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent  = AddUserActivity.getIntent(getApplicationContext());
                    startActivity(intent);
                }
            });

            Button changeUserPasswordButton = findViewById(R.id.change_user_password_button);

            changeUserPasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LandingPage.this);
                    builder.setTitle("Enter User's Username");

                    final EditText input = new EditText(LandingPage.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String targetUsername = input.getText().toString();
                            // Verify that the target user exists
                            User targetUser = mListAssistDAO.getUserByUsername(targetUsername);
                            if (targetUser != null) {
                                Intent intent = ChangePasswordActivity.getIntent(getApplicationContext(), targetUser.getUserID());
                                startActivity(intent);
                            } else {
                                Toast.makeText(LandingPage.this, "User not found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });

            Button userCountButton = findViewById(R.id.user_count_button);
            userCountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userCount = mListAssistDAO.getUserCount();
                    Toast.makeText(LandingPage.this, "Number of Users: " + userCount, Toast.LENGTH_SHORT).show();
                }
            });


            Button logoutButton = findViewById(R.id.logout_button);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

        }else{
            setContentView(R.layout.activity_landing_page);
            mUserInfoDisplay = findViewById(R.id.textView6);
            mAdminButton = findViewById(R.id.button);
            mUserInfoDisplay.setText(mUser.getUsername()+ "!");

            mItemsListView = findViewById(R.id.listView);

            List<ListItem> items = mListAssistDAO.getListItemByUserByID(mUser.getUserID());
            mItemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            mItemsListView.setAdapter(mItemsAdapter);

            for (ListItem item : items) {
                mItemsAdapter.add(item.getListItem());
            }

            mItemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    deleteTask(position);
                    return true;
                }
            });

            mItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showEditTaskDialog(position);
                }
            });

            Button addTaskButton = findViewById(R.id.button);
            addTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddTaskDialog();
                }
            });

            Button completedTasksButton = findViewById(R.id.completed_tasks_button);
            completedTasksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CompletedTasksActivity.intentFactory(LandingPage.this, mUserID);
                    startActivity(intent);
                }
            });

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new MyFragment())
                        .commit();
            }

        }

            Button logoutButton = findViewById(R.id.logout_button);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Task");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        //priority
        final CheckBox highPriorityCheckbox = new CheckBox(this);
        highPriorityCheckbox.setText("High Priority");
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(input);
        linearLayout.addView(highPriorityCheckbox);
        builder.setView(linearLayout);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskText = input.getText().toString();
                int priority1 = highPriorityCheckbox.isChecked() ? 1 : 0;
                if (!taskText.isEmpty()) { // add a check here to make sure the task text is not empty
                    addTask(taskText, priority1);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void addTask(String taskText, int priority) {
        ListItem listItem = new ListItem(mUser.getUserID(), taskText, priority);
        mListAssistDAO.insert(listItem);
        mItemsAdapter.add(listItem.getListItem());
    }

    private void logout() {
        mUserID = -1;
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        preferences.edit().remove(USER_ID_KEY).apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void deleteTask(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListItem listItem = mListAssistDAO.getListItemByUserByID(mUser.getUserID()).get(position);
                mListAssistDAO.delete(listItem);
                mItemsAdapter.remove(mItemsAdapter.getItem(position));
                mItemsAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void editTask(final int position, String newText, int priority, boolean isCompleted) {
        ListItem listItem = mListAssistDAO.getListItemByUserByID(mUser.getUserID()).get(position);
        if (isCompleted) {
            CompletedTask completedTask = new CompletedTask(mUser.getUserID(), listItem.getListItem());
            mListAssistDAO.insert(completedTask);
            mListAssistDAO.delete(listItem);
            mItemsAdapter.remove(mItemsAdapter.getItem(position));
            mItemsAdapter.notifyDataSetChanged();
        } else {
            listItem.setListItem(newText);
            listItem.setPriority(priority);
            mListAssistDAO.update(listItem);
            mItemsAdapter.remove(mItemsAdapter.getItem(position));
            String displayText = priority == 1 ? "★ " + newText : newText;
            mItemsAdapter.insert(displayText, position);
            mItemsAdapter.notifyDataSetChanged();
        }
    }

    private void showEditTaskDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(mItemsAdapter.getItem(position));
        builder.setView(input);

        final CheckBox highPriorityCheckbox = new CheckBox(this);
        highPriorityCheckbox.setText("High Priority");
        highPriorityCheckbox.setChecked(mListAssistDAO.getListItemByUserByID(mUser.getUserID()).get(position).getPriority() == 1);

        final CheckBox completedCheckbox = new CheckBox(this);
        completedCheckbox.setText("Completed");
        completedCheckbox.setChecked(false);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(input);
        linearLayout.addView(highPriorityCheckbox);
        linearLayout.addView(completedCheckbox);
        builder.setView(linearLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newText = input.getText().toString();
                int priority1 = highPriorityCheckbox.isChecked() ? 1 : 0;
                boolean isCompleted = completedCheckbox.isChecked();
                editTask(position, newText, priority1, isCompleted);

                // Update check box status
                if (priority1 == 1) {
                    mItemsListView.setItemChecked(position, true);
                } else {
                    mItemsListView.setItemChecked(position, false);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void getDatabase(){
        mListAssistDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getListAssistDAO();
    }

    public static Intent intentFactory(Context context, int userID){
        Intent intent = new Intent(context.getApplicationContext(), LandingPage.class);
        intent.putExtra(USER_ID_KEY, userID);
        return intent;
    }

    private void checkForUser(){
        mUserID = getIntent().getIntExtra(USER_ID_KEY, -1);

        if(mUserID != -1){
            User user = mListAssistDAO.getUserByID(mUserID);
            if (user != null) {
                return;
            }
        }

        SharedPreferences preferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        mUserID = preferences.getInt(USER_ID_KEY, -1);

        if(mUserID != -1) {
            User user = mListAssistDAO.getUserByID(mUserID);
            if (user != null) {
                return;
            }
        }

        // Redirect to the login page if the user is not found
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
