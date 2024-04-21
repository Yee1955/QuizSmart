package com.example.itube;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserManagerDB extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "users";
    private static final String DB_NAME = "UserManagerDB.db";
    public UserManagerDB(Context context) {super(context, DB_NAME, null, 1);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE users (id TEXT PRIMARY KEY, fullname TEXT, username TEXT, password TEXT)";
        String createPlaylistsTable = "CREATE TABLE playlists (id INTEGER PRIMARY KEY AUTOINCREMENT, url TEXT, userId TEXT, FOREIGN KEY(userId) REFERENCES users(id))";
        db.execSQL(createUsersTable);
        db.execSQL(createPlaylistsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addPlaylist(String userId, String url) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("url", url);
        values.put("userId", userId);

        long result = db.insert("playlists", null, values);
        db.close();
        return result != -1;
    }

    public List<PlayList> getPlaylistsForUser(String userId) {
        List<PlayList> playlists = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("playlists", new String[]{"id", "url"}, "userId=?", new String[]{userId}, null, null, null);

        // Check if the 'url' column index is valid
        int urlColumnIndex = cursor.getColumnIndex("url");
        if (urlColumnIndex == -1) {
            // Log an error, handle the error, or throw an exception
            cursor.close();
            db.close();
            throw new IllegalStateException("URL column not found in the database.");
        }

        while (cursor.moveToNext()) {
            String url = cursor.getString(urlColumnIndex);
            playlists.add(new PlayList(url));
        }

        cursor.close();
        db.close();
        return playlists;
    }


    public User getUser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[] {"id", "fullname", "username", "password"},
                "id=?", new String[] { id }, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            user.setPlayList(getPlaylistsForUser(id));  // Fetch and set playlists
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public Boolean addUser(User user) {
        SQLiteDatabase sql_DB = getWritableDatabase();
        ContentValues cal = new ContentValues();
        cal.put("id", user.getId());
        cal.put("fullname", user.getFullName());
        cal.put("username", user.getUserName());
        cal.put("password", user.getPassword());

        long rowId = sql_DB.insert(TABLE_NAME, null, cal);
        sql_DB.close();
        if (rowId > -1) {
            System.out.println("User added" + rowId);
            return true;
        } else {
            System.out.println("Insert failed | ERROR");
            return false;
        }
    }

    public Boolean updateUser(User user) {
        SQLiteDatabase sql_DB = getWritableDatabase();
        ContentValues cal = new ContentValues();
        cal.put("id", user.getId());
        cal.put("fullname", user.getUserName());
        cal.put("username", user.getPassword());
        cal.put("password", user.getFullName());

        // Corrected column name from "_id" to "id"
        int numOfRowsAffected = sql_DB.update(TABLE_NAME, cal, "id = ?", new String[]{user.getId()});
        sql_DB.close();

        // Return true if at least one row was updated.
        return numOfRowsAffected > 0;
    }

    public List<User> getAllUser() {

        SQLiteDatabase sql_DB = getReadableDatabase();
        Cursor query = sql_DB.query(TABLE_NAME, null, null, null, null, null, null);
        List<User> result = new ArrayList<>();

        while (query.moveToNext()) {
            result.add(new User(query.getString(0), query.getString(1), query.getString(2), query.getString(3)));
        }
        query.close();
        sql_DB.close();
        return result;
    }

    public Boolean deleteUser(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = {id};
        int numRowsDeleted = db.delete(TABLE_NAME, "id" + "= ?", whereArgs);
        db.close();
        if (numRowsDeleted > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String generateUniqueId(UserManagerDB userManagerDB) {
        List<User> userList = userManagerDB.getAllUser();
        int newId = 1;
        boolean isUnique;
        do {
            isUnique = true;
            String newIdStr = String.format("%05d", newId);
            for (User user : userList) {
                if (user.getId().equals(newIdStr)) {
                    isUnique = false;
                    break;
                }
            }
            if (!isUnique) {
                newId++;
            }
        } while (!isUnique);
        return String.format("%05d", newId);
    }

    public boolean deleteAllUser() {
        SQLiteDatabase sql_DB = getReadableDatabase();
        try {
            sql_DB.delete(TABLE_NAME, null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public String verifyUserCredentials(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {"id"};
        String selection = "username=? AND password=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            db.close();
            Log.e("Database", "Cursor is null, query failed to execute");
            return null; // Return null if the query failed to execute
        }

        String userId = null; // Initialize userId as null
        if (cursor.moveToFirst()) { // Check if the Cursor is not empty
            int idColumnIndex = cursor.getColumnIndex("id");
            if (idColumnIndex != -1) { // Ensure the column index is valid
                userId = cursor.getString(idColumnIndex);
            } else {
                Log.e("Database", "ID column not found in the database.");
                throw new IllegalStateException("ID column not found in the database.");
            }
        } else {
            Log.d("Database", "No matching user found for provided credentials.");
        }

        cursor.close();
        db.close();
        return userId; // Return the user ID or null if no matching user is found
    }
}

