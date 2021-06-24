package uk.ac.northumbria.thebarbershop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Database helper class for adding, updating and removing booking tasks
 */
public class DBJHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "barbershop.db";

    public DBJHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_APPOINTMENT = "CREATE TABLE " + Appointment.TABLE  + "("
                + Appointment.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Appointment.KEY_date + " DATE, "
                + Appointment.KEY_time + " TIME, "
                + Appointment.KEY_duration + " INTEGER, "
                + Appointment.KEY_barberName + " TEXT, "
                + Appointment.KEY_booked + " INTEGER, "
                + Appointment.KEY_customerName + " TEXT )";

        db.execSQL(CREATE_TABLE_APPOINTMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Appointment.TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * adds new appointment for customers to book
     * @param date the date of the appointment
     * @param time the time of the appointment
     * @param barberName the name of the barber
     */
    public void addAppointment(String date, String time, String barberName){
        Log.i("Lee", "Start of book appointment");
        //take input
        int dataOK = 0;
        //check for existing events
        if (date == null || time == null){
            dataOK = 1;
        }
        //if no events exist then book event
        if(dataOK == 0){
            //insert to db
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Appointment.KEY_date, date);
            values.put(Appointment.KEY_time, time);
            values.put(Appointment.KEY_duration, 30);
            values.put(Appointment.KEY_barberName, barberName);
            values.put(Appointment.KEY_booked, 0);
            values.put(Appointment.KEY_customerName, "");
            db.insertOrThrow(Appointment.TABLE, null, values);
            Log.i("Booking","Booking success");
            //return "Appointment Created!";
        }else{
            //do stuff
            Log.i("Booking","Booking failed");
        }
    }

    /**
     * updates appointment record with customer info who booked
     * @param id the id of the appointment in the db
     * @param name the name of customer who booked
     */
    public void bookAppointment(int id, String name){
        String varName;
        //check null name
        if (name.equals("")){
            varName = "guest";
        }else{
            varName = name;
        }
        //update record db
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Appointment.KEY_booked, 1);
        values.put(Appointment.KEY_customerName, varName);

        String where = Appointment.KEY_ID + "=" + id;
        db.update(Appointment.TABLE, values, where, null);
    }

    /**
     * cancels appointment
     * @param id the id of the appointment in the db
     */
    public void cancelAppointment(int id){
        //insert to db
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Appointment.KEY_booked, 0);
        values.put(Appointment.KEY_customerName, "");

        String where = Appointment.KEY_ID + "=" + id;
        db.update(Appointment.TABLE, values, where, null);
    }

    /**
     * deletes appointment from the whole system
     * @param id the id of the appointment in the db
     */
    public void deleteAppointment(int id){
        //insert to db
        SQLiteDatabase db = getWritableDatabase();
        //criteria for delete
        String where = Appointment.KEY_ID + "=" + id;
        //delete statement
        db.delete(Appointment.TABLE, where , null);
    }

    /**
     * counts number of records returned with same booking date/time
     * @param uDate user input date for proposed appointment date
     * @param uTime user input time for proposed appointment time
     * @return countRows the number of events with the same params
     */
    public int checkClashes(String uDate, String uTime){
        //get db
        SQLiteDatabase db = getWritableDatabase();
        //query using ? as a prepared statement values are added in selection args string to prevent sql injection
        String sql = "SELECT id as _id, id, date, time, barberName, duration, booked, customerName FROM Appointments WHERE date = ? AND time = ?";
        //raw query as cursor needs id as _id
        Cursor cursorCount = db.rawQuery(sql, new String[] {String.valueOf(uDate), String.valueOf(uTime)});
        //return count rows
        int countRows = cursorCount.getCount();
        //close cursor
        cursorCount.close();
        //return value
        return countRows;
    }
}
