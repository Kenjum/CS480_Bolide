package bolide.planner;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelperNotes extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "Notes.db";
    public static final String TABLE_NAME = "notes_table";
    public static final String COL_0 = "ID";
    public static final String COL_1 = "TITLE";
    public static final String COL_2 = "BODY";
    public static final String COL_3 = "AUDIO";

    public DatabaseHelperNotes(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, BODY TEXT, AUDIO TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String title, String body, String audio){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, title);
        contentValues.put(COL_2, body);
        contentValues.put(COL_3, audio);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
}
