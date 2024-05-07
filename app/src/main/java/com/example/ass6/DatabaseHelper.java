package com.example.ass6;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "ass6_2.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_USERNAME = "username";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_INTEREST = "interest";
    private static final String KEY_USER_SUBSCRIPTION = "subscription"; // New column for subscription

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_USERNAME + " TEXT UNIQUE," +
                KEY_USER_PASSWORD + " TEXT," +
                KEY_USER_INTEREST + " TEXT," +
                KEY_USER_SUBSCRIPTION + " TEXT DEFAULT 'Default'" + // Default subscription value
                ")";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }

    // Insert a user into the database
    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_USERNAME, user.getUsername());
            values.put(KEY_USER_PASSWORD, user.getPassword());
            values.put(KEY_USER_INTEREST, TextUtils.join(",", user.getInterest()));
            values.put(KEY_USER_SUBSCRIPTION, "Default"); // Set default subscription

            // Insert the user
            db.insertOrThrow(TABLE_USERS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    // Update user's interest
    public void updateUserInterest(String username, List<String> interest) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_INTEREST, TextUtils.join(",", interest));

            // Updating interest for user with that username
            db.update(TABLE_USERS, values, KEY_USER_USERNAME + " = ?", new String[]{username});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    // Get user by username
    public User getUserByUsername(String username) {
        User user = null;
        String USERS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = ?",
                        TABLE_USERS, KEY_USER_USERNAME);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, new String[]{String.valueOf(username)});
        try {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String userUsername = cursor.getString(cursor.getColumnIndex(KEY_USER_USERNAME));
                @SuppressLint("Range") String userPassword = cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD));
                @SuppressLint("Range") List<String> interest = new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(KEY_USER_INTEREST)).split(",")));
                @SuppressLint("Range") String subscription = cursor.getString(cursor.getColumnIndex(KEY_USER_SUBSCRIPTION));
                user = new User(userUsername, userPassword, "", "", interest, subscription);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return user;
    }

    // Change user's subscription
    public void changeSubscription(String userName, String newSubscription) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_SUBSCRIPTION, newSubscription);

            int rowsAffected = db.update(TABLE_USERS, values, KEY_USER_USERNAME + " = ?", new String[]{userName});

            if (rowsAffected > 0) {
                db.setTransactionSuccessful();
            } else {
                Log.e("DatabaseError", "No user found with username: " + userName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
