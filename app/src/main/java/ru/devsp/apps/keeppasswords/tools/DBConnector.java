package ru.devsp.apps.keeppasswords.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Старая бд
 * Created by gen on 04.12.2017.
 */

public class DBConnector extends SQLiteOpenHelper {

    static final String DB_NAME = "keeppass";

    private static final int VERSION = 1;

    static SQLiteDatabase getDB(Context context) {
        SQLiteOpenHelper dbConnector = new DBConnector(context);
        return dbConnector.getWritableDatabase();
    }

    private DBConnector(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //not used
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //not used
    }

}
