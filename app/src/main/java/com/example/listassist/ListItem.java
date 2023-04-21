package com.example.listassist;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.listassist.db.AppDatabase;

import java.util.Date;

@Entity(tableName = AppDatabase.LIST_TABLE)
public class ListItem {
   @PrimaryKey(autoGenerate = true)
   private int mListID;
   private int mUserID;
   private Date mDate;
   private String mListItem;
   private int mPriority;

   @Ignore
   public ListItem(int userID, String listItem) {
      mUserID = userID;
      mListItem = listItem;
      mDate = new Date();
      mPriority = 0;
   }


   public ListItem(int userID, String listItem, int priority) {
      mUserID = userID;
      mListItem = listItem;
      mDate = new Date();
      mPriority = priority;
   }

   public int getListID() {
      return mListID;
   }

   public int getUserID() {
      return mUserID;
   }

   public Date getDate() {
      return mDate;
   }

   public String getListItem() {
      return mListItem;
   }

   public int getPriority() {
      return mPriority;
   }

   public void setListID(int mListID) {
      this.mListID = mListID;
   }

   public void setUserID(int mUserID) {
      this.mUserID = mUserID;
   }

   public void setDate(Date mDate) {
      this.mDate = mDate;
   }

   public void setListItem(String mListItem) {
      this.mListItem = mListItem;
   }

   public void setPriority(int mPriority) {
      this.mPriority = mPriority;
   }
}
