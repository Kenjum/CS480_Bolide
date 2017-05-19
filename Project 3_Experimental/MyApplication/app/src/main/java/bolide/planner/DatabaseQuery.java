package bolide.planner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseQuery extends SQLiteOpenHelper{
    private SQLiteDatabase db;
    public static final String DATABASE_NAME = "events.db";
    public static final String TABLE_NAME = "events";
    public static final String COL_0 = "_id";    //Audio files will be related to ID
    public static final String COL_1 = "message";
    public static final String COL_2 = "reminder";
    public static final String COL_3 = "end";

    public DatabaseQuery(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `message` TEXT NOT NULL, `reminder` TEXT NOT NULL, `end` TEXT");
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_0,1);
        contentValues.put(COL_1, "test");
        contentValues.put(COL_2, "test");
        contentValues.put(COL_3, "test");
        db.insert(TABLE_NAME, null, contentValues);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public List<EventObjects> getAllFutureEvents(){
        Date dateToday = new Date();
        List<EventObjects> events = new ArrayList<>();
        /**Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String message = cursor.getString(cursor.getColumnIndexOrThrow("message"));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
                //convert start date to date object
                Date reminderDate = convertStringToDate(startDate);
                if(reminderDate.after(dateToday) || reminderDate.equals(dateToday)){
                    events.add(new EventObjects(id, message, reminderDate));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();**/
        return events;
    }
    private Date convertStringToDate(String dateInString){
        DateFormat format = new SimpleDateFormat("d-MM-yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}