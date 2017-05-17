package bolide.planner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "events.db";
    public static final String TABLE_NAME = "events_table";
    public static final String COL_0 = "ID";    //Audio files will be related to ID
    public static final String COL_1 = "TITLE";
    public static final String COL_2 = "BODY";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, BODY TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}