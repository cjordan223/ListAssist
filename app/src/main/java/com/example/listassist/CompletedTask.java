package com.example.listassist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.listassist.db.AppDatabase;

@Entity(tableName = AppDatabase.COMPLETED_TASK_TABLE)
public class CompletedTask {
    @PrimaryKey(autoGenerate = true)
    private int mID;
    @ColumnInfo(name = "mUserID")
    private int mUserID;
    @ColumnInfo(name = "completed_task")
    private String mTaskText;


    public CompletedTask(int userID, String taskText) {
        mUserID = userID;
        mTaskText = taskText;
    }

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public int getUserID() {
        return mUserID;
    }

    public void setUserID(int userID) {
        mUserID = userID;
    }

    public String getTaskText() {
        return mTaskText;
    }

    public void setTaskText(String taskText) {
        mTaskText = taskText;
    }
}
