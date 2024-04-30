package ManagerDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import Class.User;
import Class.Interest;

import java.util.ArrayList;
import java.util.List;

public class ManagerDB extends SQLiteOpenHelper {
    private static final String USERS_TABLE_NAME = "users";
    private static final String INTEREST_TABLE_NAME = "interest";
    private static final String USER_INTEREST_TABLE_NAME = "user_interest";
    private static final String DB_NAME = "UserManagerDB.db";

    public ManagerDB(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + USERS_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, email TEXT, password TEXT, phonenumber TEXT)";
        String createInterestTable = "CREATE TABLE " + INTEREST_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT)";
        String createUserInterestTable = "CREATE TABLE " + USER_INTEREST_TABLE_NAME + " (userId INTEGER, interestId INTEGER, PRIMARY KEY(userId, interestId), FOREIGN KEY(userId) REFERENCES users(id), FOREIGN KEY(interestId) REFERENCES interest(id))";

        db.execSQL(createUsersTable);
        db.execSQL(createInterestTable);
        db.execSQL(createUserInterestTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + INTEREST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_INTEREST_TABLE_NAME);
        onCreate(db);
    }

    // User Table Methods
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("phonenumber", user.getPhoneNumber());
        db.insert(USERS_TABLE_NAME, null, values);
        db.close();
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERS_TABLE_NAME, new String[]{"id", "username", "email", "password", "phonenumber"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int usernameIndex = cursor.getColumnIndex("username");
            int emailIndex = cursor.getColumnIndex("email");
            int passwordIndex = cursor.getColumnIndex("password");
            int phoneIndex = cursor.getColumnIndex("phonenumber");

            if (idIndex != -1 && usernameIndex != -1 && emailIndex != -1 && passwordIndex != -1 && phoneIndex != -1) {
                user = new User();
                user.setId(cursor.getString(idIndex));
                user.setUsername(cursor.getString(usernameIndex));
                user.setEmail(cursor.getString(emailIndex));
                user.setPassword(cursor.getString(passwordIndex));
                user.setPhoneNumber(cursor.getString(phoneIndex));
            } else {
                Log.e("DB_ERROR", "One or more columns not found in the users table.");
            }
        }
        cursor.close();
        db.close();
        return user;
    }


    public void deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USERS_TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public User verifyCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERS_TABLE_NAME, new String[]{"id", "username", "email", "password", "phonenumber"}, "username=? AND password=?", new String[]{username, password}, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int usernameIndex = cursor.getColumnIndex("username");
            int emailIndex = cursor.getColumnIndex("email");
            int passwordIndex = cursor.getColumnIndex("password");
            int phoneIndex = cursor.getColumnIndex("phonenumber");

            if (idIndex != -1 && usernameIndex != -1 && emailIndex != -1 && passwordIndex != -1 && phoneIndex != -1) {
                user = new User();
                user.setId(cursor.getString(idIndex));
                user.setUsername(cursor.getString(usernameIndex));
                user.setEmail(cursor.getString(emailIndex));
                user.setPassword(cursor.getString(passwordIndex));
                user.setPhoneNumber(cursor.getString(phoneIndex));
            } else {
                Log.e("DB_ERROR", "One or more columns not found in the users table.");
            }
        }
        cursor.close();
        db.close();
        return user;
    }


    // Interest Table Methods
    public void addInterest(Interest interest) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("topic", interest.getTopic());
        db.insert(INTEREST_TABLE_NAME, null, values);
        db.close();
    }

    public Interest getInterest(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(INTEREST_TABLE_NAME, new String[]{"id", "topic"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Interest interest = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int topicIndex = cursor.getColumnIndex("topic");

            if (idIndex != -1 && topicIndex != -1) {
                interest = new Interest();
                interest.setId(cursor.getString(idIndex));
                interest.setTopic(cursor.getString(topicIndex));
            } else {
                Log.e("DB_ERROR", "One or more columns not found in the interest table.");
            }
        }
        cursor.close();
        db.close();
        return interest;
    }

    public void deleteInterest(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INTEREST_TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // User-Interest Relationship Methods
    public void addUserInterest(int userId, int interestId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("interestId", interestId);
        db.insert(USER_INTEREST_TABLE_NAME, null, values);
        db.close();
    }

    public void deleteUserInterest(int userId, int interestId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_INTEREST_TABLE_NAME, "userId=? AND interestId=?", new String[]{String.valueOf(userId), String.valueOf(interestId)});
        db.close();
    }
}
