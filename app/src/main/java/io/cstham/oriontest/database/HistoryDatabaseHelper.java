package io.cstham.oriontest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import io.cstham.oriontest.entity.History;


public class HistoryDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "history_db";

    public HistoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create tables
        //db.execSQL(Record.CREATE_TABLE);

        db.execSQL(History.CREATE_TABLE);

        //System.out.println("ever called: lolz");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + Record.TABLE_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + History.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    //==============================================================================================
    public long insertHistory(String title, String author, String description, String url,
                              String image, String content) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(History.COLUMN_TITLE, title);
        values.put(History.COLUMN_AUTHOR, author);
        values.put(History.COLUMN_DESCRIPTION, description);
        values.put(History.COLUMN_URL, url);
        values.put(History.COLUMN_IMAGE, image);
        values.put(History.COLUMN_CONTENT, content);

        // insert row
        long id = db.insert(History.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public History getHistory(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(History.TABLE_NAME,
                new String[]{History.COLUMN_ID,
                        History.COLUMN_TITLE,
                        History.COLUMN_AUTHOR,
                        History.COLUMN_DESCRIPTION,
                        History.COLUMN_URL,
                        History.COLUMN_IMAGE,
                        History.COLUMN_CONTENT,
                        History.COLUMN_TIMESTAMP},
                History.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare record object
        History history = new History(
                cursor.getInt(cursor.getColumnIndex(History.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(History.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(History.COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(History.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(History.COLUMN_URL)),
                cursor.getString(cursor.getColumnIndex(History.COLUMN_IMAGE)),
                cursor.getString(cursor.getColumnIndex(History.COLUMN_CONTENT)),
                cursor.getString(cursor.getColumnIndex(History.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return history;
    }


    public List<History> getAllHistories() {
        List<History> records = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + History.TABLE_NAME + " ORDER BY " +
                History.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                History record = new History();
                record.setId(cursor.getInt(cursor.getColumnIndex(History.COLUMN_ID)));
                record.setTitle(cursor.getString(cursor.getColumnIndex(History.COLUMN_TITLE)));
                record.setAuthor(cursor.getString(cursor.getColumnIndex(History.COLUMN_AUTHOR)));
                record.setDescription(cursor.getString(cursor.getColumnIndex(History.COLUMN_DESCRIPTION)));
                record.setUrl(cursor.getString(cursor.getColumnIndex(History.COLUMN_URL)));
                record.setImage(cursor.getString(cursor.getColumnIndex(History.COLUMN_IMAGE)));
                record.setContent(cursor.getString(cursor.getColumnIndex(History.COLUMN_CONTENT)));
                record.setTimestamp(cursor.getString(cursor.getColumnIndex(History.COLUMN_TIMESTAMP)));

                records.add(record);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return records list
        return records;
    }

    public void deleteHistory(History history) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(History.TABLE_NAME, History.COLUMN_ID + " = ?",
                new String[]{String.valueOf(history.getId())});
        db.close();
    }

    public void deleteAllHistories(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ History.TABLE_NAME);
    }
}
