package com.sl.rakoto.gyroacs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqlDB {
    private SqlHelper sqLiteHelper;
    private SQLiteDatabase db;
    private Cursor cursor;


    public SqlDB(Context context){
        sqLiteHelper = new SqlHelper(context);
        db = sqLiteHelper.getWritableDatabase();
    }

    public void getCursor(String tableName){
        cursor = db.rawQuery("SELECT * FROM " + tableName, null);
    }

    public int count(String tableName){
        getCursor(tableName);
        return cursor.getCount();
    }

    public int getId(int i){
        cursor.moveToPosition(i);
        return cursor.getInt(cursor.getColumnIndex(sqLiteHelper.COLUMN_ID));
    }

    public String[] getGyroDate(int id){
        cursor.moveToPosition(id);
        String[] gyroDate = new String[3];
        gyroDate[0] = (cursor.getString(cursor.getColumnIndex(sqLiteHelper.COLUMN_GYROX)));
        gyroDate[1] = (cursor.getString(cursor.getColumnIndex(sqLiteHelper.COLUMN_GYROY)));
        gyroDate[2] = (cursor.getString(cursor.getColumnIndex(sqLiteHelper.COLUMN_GYROZ)));

        return gyroDate;
    }

    public String[] getAcsDate(int id){
        cursor.moveToPosition(id);
        String[] acsDate = new String[3];
        acsDate[0] = (cursor.getString(cursor.getColumnIndex(sqLiteHelper.COLUMN_ACSX)));
        acsDate[1] = (cursor.getString(cursor.getColumnIndex(sqLiteHelper.COLUMN_ACSY)));
        acsDate[2] = (cursor.getString(cursor.getColumnIndex(sqLiteHelper.COLUMN_ACSZ)));

        return acsDate;
    }

    public void addDate(String tableName, float[] gyroDate, float[] acsDate){
        db.execSQL("INSERT INTO " + tableName + " (" + sqLiteHelper.COLUMN_GYROX + ", " + sqLiteHelper.COLUMN_GYROY  + ", "  + sqLiteHelper.COLUMN_GYROZ  + ", "
                                                    + sqLiteHelper.COLUMN_ACSX + ", " + sqLiteHelper.COLUMN_ACSY + ", " + sqLiteHelper.COLUMN_ACSZ + ") " +
                                                "VALUES (" + gyroDate[0] + ", " + gyroDate[1] +", " + gyroDate[2]
                                                            + ", " + acsDate[0] + ", " + acsDate[1] + ", " + acsDate[2] +")");
    }
}

