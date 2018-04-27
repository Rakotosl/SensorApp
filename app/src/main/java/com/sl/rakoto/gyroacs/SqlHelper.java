package com.sl.rakoto.gyroacs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "DateDb";
    private static final int SCHEMA = 1;

    public static final String UNDEFIND_TABLE = "Данные";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GYROX = "gyroX";
    public static final String COLUMN_GYROY = "gyroY";
    public static final String COLUMN_GYROZ = "gyroZ";
    public static final String COLUMN_ACSX = "acsX";
    public static final String COLUMN_ACSY = "acsY";
    public static final String COLUMN_ACSZ = "acsZ";
    public static final String COLUMN_TEXT = "text";


    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + UNDEFIND_TABLE + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_GYROX + " TEXT, "
                + COLUMN_GYROY + " TEXT, "
                + COLUMN_GYROZ + " TEXT, "
                + COLUMN_ACSX + " TEXT, "
                + COLUMN_ACSY + " TEXT, "
                + COLUMN_ACSZ + " TEXT, "
                + COLUMN_TEXT + " TEXT);");

        db.execSQL("INSERT INTO " + UNDEFIND_TABLE + " (" + COLUMN_GYROX + ", " + COLUMN_GYROY  + ", "  + COLUMN_GYROZ  + ", "
                                 + COLUMN_ACSX + ", " + COLUMN_ACSY + ", " + COLUMN_ACSZ + ") " +
                                 "VALUES ('1', '2', '3', '4', '5', '6')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
