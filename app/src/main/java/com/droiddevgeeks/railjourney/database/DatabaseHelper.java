package com.droiddevgeeks.railjourney.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.droiddevgeeks.railjourney.interfaces.IDatabaseListener;
import com.droiddevgeeks.railjourney.models.PnrDBVO;

import java.util.ArrayList;

/**
 * Created by kunal.sale on 10/19/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "pnr_database";

    // Contacts table name
    private static final String TABLE_PNR = "pnr_table";

    private static String KEY_PNR = "pnr";
    private static String KEY_TIME = "name";

    private IDatabaseListener _iDatabaseListener;

    public DatabaseHelper( Context context, IDatabaseListener iDatabaseListener )
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _iDatabaseListener = iDatabaseListener;
    }

    public DatabaseHelper( Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler )
    {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate( SQLiteDatabase db )
    {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PNR + "("
                + KEY_PNR + " TEXT PRIMARY KEY,"
                + KEY_TIME + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PNR);

        // Create tables again
        onCreate(db);
    }

    public void addPnr( PnrDBVO pnrDBVO )
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PNR, pnrDBVO.getPnr()); // Contact Name
        values.put(KEY_TIME, pnrDBVO.getTime()); // Contact Phone

        // Inserting Row
        int size = getTableSize();

        long res = 0;
        if ( size >= 5 ) {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String countQuery = "SELECT  MIN("+ KEY_TIME+") FROM " + TABLE_PNR;

            String pnrQuery = "SELECT * FROM " + TABLE_PNR +
                    " ORDER BY " + KEY_TIME + " LIMIT "+5;
           Cursor c = sqLiteDatabase.rawQuery( pnrQuery, null);
            int sizeOFLimit = c.getCount();
            if ( c.moveToFirst() )
            {
                do {
                    String pnr = c.getString( c.getColumnIndex(KEY_PNR));
                    String time = c.getString( c.getColumnIndex(KEY_TIME));
                    Log.v("HomeScreenFragment", pnr+" "+time+" "+sizeOFLimit);
                    //db.update( TABLE_PNR, values, KEY_PNR = c.getString( c.getColumnIndex(KEY_PNR)), null);
                    SQLiteDatabase sqLiteDatabase1 = this.getWritableDatabase();
                    sqLiteDatabase1.delete( TABLE_PNR, KEY_PNR = pnr,null);
                    sqLiteDatabase1.insert( TABLE_PNR, null, values);
                    break;
                    // Adding contact to list
                }
                while ( c.moveToNext() );
            }


            //db.update( TABLE_PNR, values, KEY_PNR +"=?"+ query, null);
        }
        else
        {
            res = db.insert(TABLE_PNR, null, values);
        }

        if ( res > 0 ) {
            _iDatabaseListener.onInsertionSuccess();
        }
        else {
            _iDatabaseListener.onInsertionFailed();
        }
        db.close(); // Closing database connection
    }

    public ArrayList<PnrDBVO> getPnrValues()
    {
        ArrayList<PnrDBVO> pnrDBVOArrayList = new ArrayList<>();
        String pnrQuery = "SELECT * FROM " + TABLE_PNR +
                " ORDER BY " + KEY_TIME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(pnrQuery, null);

        if ( cursor.moveToFirst() ) {
            do {
                pnrDBVOArrayList.add(new PnrDBVO(cursor.getString(0), cursor.getString(1)));
            }
            while ( cursor.moveToNext() );
        }

        // return contact list
        return pnrDBVOArrayList;
    }

    public int getTableSize()
    {
        String countQuery = "SELECT  * FROM " + TABLE_PNR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int size =  cursor.getCount();
        cursor.close();
        return size;
    }
}
