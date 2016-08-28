package com.example.sergey.bagwords;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sergey on 23.08.2016.
 */
public class WordsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MyWordsData"; // Имя базы данных
    private static final int DB_VERSION = 1; // Версия базы данных

    WordsDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create database
        updateMyDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        updateMyDatabase(sqLiteDatabase, i, i1);
    }

    private void updateMyDatabase(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // if database clear
        if (oldVersion < 1) {

            // Create table in database
            sqLiteDatabase.execSQL("CREATE TABLE MY_WORDS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "EN_WORD TEXT, "
                    + "RU_WORD TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER);");
           // insertWord(sqLiteDatabase, "Hello", "Привет", 1);
        }

//        if (oldVersion < 2) {
//
//        }
    }

    private static void insertWord(SQLiteDatabase sqLiteDatabase, String name,
                                    String description, int resourceId) {

        ContentValues wordValues = new ContentValues();
        wordValues.put("EN_WORD", name);
        wordValues.put("RU_WORD", description);
        wordValues.put("IMAGE_RESOURCE_ID", resourceId);
        sqLiteDatabase.insert("MY_WORDS", null, wordValues);
    }
}
