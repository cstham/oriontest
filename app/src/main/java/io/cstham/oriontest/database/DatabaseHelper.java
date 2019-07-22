package io.cstham.oriontest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import io.cstham.oriontest.entity.Record;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "orion_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create tables
        db.execSQL(Record.CREATE_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Record.TABLE_NAME);

        onCreate(db);
    }

    public long insertRecord(String title, String author, String description, String url,
                             String image, String content) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Record.COLUMN_TITLE, title);
        values.put(Record.COLUMN_AUTHOR, author);
        values.put(Record.COLUMN_DESCRIPTION, description);
        values.put(Record.COLUMN_URL, url);
        values.put(Record.COLUMN_IMAGE, image);
        values.put(Record.COLUMN_CONTENT, content);

        // insert row
        long id = db.insert(Record.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Record getRecord(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Record.TABLE_NAME,
                new String[]{Record.COLUMN_ID,
                        Record.COLUMN_TITLE,
                        Record.COLUMN_AUTHOR,
                        Record.COLUMN_DESCRIPTION,
                        Record.COLUMN_URL,
                Record.COLUMN_IMAGE,
                Record.COLUMN_CONTENT,
                Record.COLUMN_TIMESTAMP},
                Record.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare record object
        Record record = new Record(
                cursor.getInt(cursor.getColumnIndex(Record.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Record.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(Record.COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(Record.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(Record.COLUMN_URL)),
                cursor.getString(cursor.getColumnIndex(Record.COLUMN_IMAGE)),
                cursor.getString(cursor.getColumnIndex(Record.COLUMN_CONTENT)),
                cursor.getString(cursor.getColumnIndex(Record.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return record;
    }

    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Record.TABLE_NAME + " ORDER BY " +
                Record.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getInt(cursor.getColumnIndex(Record.COLUMN_ID)));
                record.setTitle(cursor.getString(cursor.getColumnIndex(Record.COLUMN_TITLE)));
                record.setAuthor(cursor.getString(cursor.getColumnIndex(Record.COLUMN_AUTHOR)));
                record.setDescription(cursor.getString(cursor.getColumnIndex(Record.COLUMN_DESCRIPTION)));
                record.setUrl(cursor.getString(cursor.getColumnIndex(Record.COLUMN_URL)));
                record.setImage(cursor.getString(cursor.getColumnIndex(Record.COLUMN_IMAGE)));
                record.setContent(cursor.getString(cursor.getColumnIndex(Record.COLUMN_CONTENT)));
                record.setTimestamp(cursor.getString(cursor.getColumnIndex(Record.COLUMN_TIMESTAMP)));

                records.add(record);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return records list
        return records;
    }

    public int getRecordsCount() {
        String countQuery = "SELECT  * FROM " + Record.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Record.COLUMN_TITLE, record.getTitle());
        values.put(Record.COLUMN_AUTHOR, record.getTitle());
        values.put(Record.COLUMN_DESCRIPTION, record.getTitle());
        values.put(Record.COLUMN_URL, record.getTitle());
        values.put(Record.COLUMN_IMAGE, record.getTitle());
        values.put(Record.COLUMN_CONTENT, record.getTitle());

        // updating row
        return db.update(Record.TABLE_NAME, values, Record.COLUMN_ID + " = ?",
                new String[]{String.valueOf(record.getId())});
    }

    public void deleteRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Record.TABLE_NAME, Record.COLUMN_ID + " = ?",
                new String[]{String.valueOf(record.getId())});
        db.close();
    }

    public void deleteAllRecords(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ Record.TABLE_NAME);
    }

}

