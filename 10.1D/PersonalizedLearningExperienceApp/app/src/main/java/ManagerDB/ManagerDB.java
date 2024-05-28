package ManagerDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
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
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + USERS_TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT, "
                + "email TEXT, "
                + "password TEXT, "
                + "phonenumber TEXT, "
                + "total_questions INTEGER DEFAULT 0, "
                + "correctly_answered INTEGER DEFAULT 0, "
                + "incorrect_answers INTEGER DEFAULT 0)";

        String createInterestTable = "CREATE TABLE " + INTEREST_TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "topic TEXT)";

        String createUserInterestTable = "CREATE TABLE " + USER_INTEREST_TABLE_NAME + " ("
                + "userId INTEGER, "
                + "interestId INTEGER, "
                + "PRIMARY KEY(userId, interestId), "
                + "FOREIGN KEY(userId) REFERENCES users(id), "
                + "FOREIGN KEY(interestId) REFERENCES interest(id))";

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
    public User addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("phonenumber", user.getPhoneNumber());
        values.put("total_questions", user.getTotalQuestions());
        values.put("correctly_answered", user.getCorrectlyAnswered());
        values.put("incorrect_answers", user.getIncorrectAnswers());

        long userId = db.insert(USERS_TABLE_NAME, null, values);
        db.close();

        if (userId == -1) {
            Log.e("DB_ERROR", "Failed to add user to database.");
            return null;
        }

        user.setId(userId);
        return user;
    }

    public User getUser(Long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERS_TABLE_NAME,
                new String[]{"id", "username", "email", "password", "phonenumber", "total_questions", "correctly_answered", "incorrect_answers"},
                "id=?", new String[]{String.valueOf(id)}, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int usernameIndex = cursor.getColumnIndex("username");
            int emailIndex = cursor.getColumnIndex("email");
            int passwordIndex = cursor.getColumnIndex("password");
            int phoneIndex = cursor.getColumnIndex("phonenumber");
            int totalQuestionsIndex = cursor.getColumnIndex("total_questions");
            int correctlyAnsweredIndex = cursor.getColumnIndex("correctly_answered");
            int incorrectAnswersIndex = cursor.getColumnIndex("incorrect_answers");

            if (idIndex != -1 && usernameIndex != -1 && emailIndex != -1 && passwordIndex != -1 &&
                    phoneIndex != -1 && totalQuestionsIndex != -1 && correctlyAnsweredIndex != -1 && incorrectAnswersIndex != -1) {

                user = new User();
                user.setId(cursor.getLong(idIndex));
                user.setUsername(cursor.getString(usernameIndex));
                user.setEmail(cursor.getString(emailIndex));
                user.setPassword(cursor.getString(passwordIndex));
                user.setPhoneNumber(cursor.getString(phoneIndex));
                user.setTotalQuestions(cursor.getInt(totalQuestionsIndex));
                user.setCorrectlyAnswered(cursor.getInt(correctlyAnsweredIndex));
                user.setIncorrectAnswers(cursor.getInt(incorrectAnswersIndex));
            } else {
                Log.e("DB_ERROR", "One or more columns not found in the users table.");
            }
        }
        cursor.close();
        db.close();
        return user;
    }

    public void deleteUser(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USERS_TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Execute SQL to delete all rows in the users table
        db.execSQL("DELETE FROM " + USERS_TABLE_NAME);
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
                user.setId(cursor.getLong(idIndex));
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
    public long addInterest(Interest interest) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("topic", interest.getTopic());

        // Insert the interest into the database and get the inserted row ID
        long interestId = db.insert(INTEREST_TABLE_NAME, null, values);
        db.close();

        // Return the row ID of the newly inserted row, or -1 if an error occurred
        return interestId;
    }

    public Interest getInterest(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(INTEREST_TABLE_NAME, new String[]{"id", "topic"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Interest interest = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int topicIndex = cursor.getColumnIndex("topic");

            if (idIndex != -1 && topicIndex != -1) {
                interest = new Interest();
                interest.setId(cursor.getLong(idIndex));
                interest.setTopic(cursor.getString(topicIndex));
            } else {
                Log.e("DB_ERROR", "One or more columns not found in the interest table.");
            }
        }
        cursor.close();
        db.close();
        return interest;
    }

    public void deleteInterest(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INTEREST_TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Interest> getAllInterests() {
        List<Interest> interests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(INTEREST_TABLE_NAME, new String[]{"id", "topic"}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int topicIndex = cursor.getColumnIndex("topic");

                if (idIndex != -1 && topicIndex != -1) {
                    Interest interest = new Interest();
                    interest.setId(cursor.getLong(idIndex));  // Assuming the ID setter accepts a long
                    interest.setTopic(cursor.getString(topicIndex));
                    interests.add(interest);
                } else {
                    Log.e("DB_ERROR", "One or more columns not found in the interest table.");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return interests;
    }

    public void deleteAllInterests() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Execute SQL to delete all rows in the interest table
        db.execSQL("DELETE FROM " + INTEREST_TABLE_NAME);
        db.close();
    }

    // User-Interest Relationship Methods
    public List<Interest> getUserInterests(Long userId) {
        List<Interest> interests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + INTEREST_TABLE_NAME + ".id, " + INTEREST_TABLE_NAME + ".topic FROM " + INTEREST_TABLE_NAME
                + " JOIN " + USER_INTEREST_TABLE_NAME + " ON " + INTEREST_TABLE_NAME + ".id = " + USER_INTEREST_TABLE_NAME + ".interestId"
                + " WHERE " + USER_INTEREST_TABLE_NAME + ".userId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int topicIndex = cursor.getColumnIndex("topic");

                if (idIndex != -1 && topicIndex != -1) {
                    Interest interest = new Interest();
                    interest.setId(cursor.getLong(idIndex));
                    interest.setTopic(cursor.getString(topicIndex));
                    interests.add(interest);
                } else {
                    Log.e("DB_ERROR", "One or more columns not found in the interest table.");
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return interests;
    }

    public boolean addUserInterest(long userId, long interestId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("DB_DEBUG", "Checking if user-interest pair already exists: userId=" + userId + ", interestId=" + interestId);

        // Check if this user-interest pair already exists
        Cursor cursor = db.rawQuery("SELECT 1 FROM user_interest WHERE userId = ? AND interestId = ?", new String[] { String.valueOf(userId), String.valueOf(interestId) });
        boolean exists = cursor.moveToFirst();
        cursor.close();

        if (!exists) {
            ContentValues values = new ContentValues();
            values.put("userId", userId);
            values.put("interestId", interestId);

            Log.d("DB_DEBUG", "Inserting new user-interest pair: userId=" + userId + ", interestId=" + interestId);
            long result = db.insert("user_interest", null, values); // Perform the insert

            db.close();

            if (result == -1) {
                Log.e("DB_ERROR", "Failed to insert user-interest pair: userId=" + userId + ", interestId=" + interestId);
                return false; // Insert failed
            } else {
                Log.d("DB_DEBUG", "Insert successful: userId=" + userId + ", interestId=" + interestId);
                return true; // Insert successful
            }
        } else {
            Log.d("DB_DEBUG", "User-interest pair already exists, not inserting: userId=" + userId + ", interestId=" + interestId);
            db.close();
            return false; // Insert not performed, pair already exists
        }
    }

    public void deleteUserInterest(Long userId, String interestTopic) {
        SQLiteDatabase db = this.getWritableDatabase();

        // First, find the interestId for the given interestTopic
        Cursor interestCursor = db.query(INTEREST_TABLE_NAME, new String[]{"id"}, "topic=?", new String[]{interestTopic}, null, null, null);
        if (interestCursor.moveToFirst()) {
            int idIndex = interestCursor.getColumnIndex("id");
            if (idIndex != -1) {
                long interestId = interestCursor.getLong(idIndex);
                interestCursor.close();

                // Now, delete the user-interest relationship with the found interestId and userId
                db.delete(USER_INTEREST_TABLE_NAME, "userId=? AND interestId=?", new String[]{String.valueOf(userId), String.valueOf(interestId)});
            } else {
                Log.e("DB_ERROR", "Column 'id' not found in interest table.");
            }
        } else {
            Log.e("DB_ERROR", "Interest with specified topic not found: " + interestTopic);
        }
        interestCursor.close();
        db.close();
    }

    public void insertDummyUserWithInterests() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // Start transaction for atomic operations

        try {
            // Insert the user into the users table
            ContentValues userValues = new ContentValues();
            userValues.put("username", "dummyUser");
            userValues.put("email", "dummyuser@example.com");
            userValues.put("password", "password123");
            userValues.put("phonenumber", "1234567890");
            userValues.put("total_questions", 10);
            userValues.put("correctly_answered", 8);
            userValues.put("incorrect_answers", 2);

            long userId = db.insert(USERS_TABLE_NAME, null, userValues);
            if (userId == -1) {
                Log.e("DB_ERROR", "Failed to add user to database.");
                return;
            }

            // List of dummy interests
            List<String> interests = new ArrayList<>();
            interests.add("Technology");
            interests.add("Science");
            interests.add("Art");

            // Insert interests and user-interest relationships
            for (String interestTopic : interests) {
                long interestId;
                // Check if interest already exists
                Cursor interestCursor = db.query(INTEREST_TABLE_NAME, new String[]{"id"}, "topic=?", new String[]{interestTopic}, null, null, null);
                if (interestCursor.moveToFirst()) {
                    interestId = interestCursor.getLong(interestCursor.getColumnIndex("id"));
                } else {
                    // Insert new interest
                    ContentValues interestValues = new ContentValues();
                    interestValues.put("topic", interestTopic);
                    interestId = db.insert(INTEREST_TABLE_NAME, null, interestValues);
                    if (interestId == -1) {
                        Log.e("DB_ERROR", "Failed to add interest to database.");
                        return;
                    }
                }

                // Insert user-interest relationship
                ContentValues userInterestValues = new ContentValues();
                userInterestValues.put("userId", userId);
                userInterestValues.put("interestId", interestId);
                long result = db.insert(USER_INTEREST_TABLE_NAME, null, userInterestValues);
                if (result == -1) {
                    Log.e("DB_ERROR", "Failed to add user-interest relationship to database.");
                    return;
                }
            }

            db.setTransactionSuccessful(); // Mark the transaction as successful
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error while inserting dummy user and interests: " + e.getMessage());
        } finally {
            db.endTransaction(); // End transaction
            db.close(); // Close the database
        }
    }


    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DB_NAME);
    }

}
