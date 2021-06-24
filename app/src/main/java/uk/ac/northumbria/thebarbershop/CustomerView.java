package uk.ac.northumbria.thebarbershop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.*;
import java.util.Date;

public class CustomerView extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //old elements used for earlier version using array adapter
    //kept in but commented out to show development of project

    //ArrayList<String> appointments;
    //ArrayList<appointmentInfo> appointmentList;

    //ArrayAdapter<uk.ac.northumbria.listview.appointmentInfo> arrayAdapter;
    //CustomAdapter appAdapter;
    //dbCustomAdapter customDbAdapter;

    //main elements
    ListView myList;

    //Intent variables
    String username;
    Intent user;

    //Database info
    DBJHelper dbHelper;
    Cursor cursor;
    dbCustomAdapter cursorAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);

        //database
        dbHelper = new DBJHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //get info from intent
        user = getIntent();
        username = user.getStringExtra("username");

        if (username.equals("")){
            username = "guest";
        }
        //make title
        this.setTitle("Customer View " + username);

        //get current time/date
        /*SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateNow = sdf.format(new Date());

        SimpleDateFormat stf = new SimpleDateFormat("hh:mm");
        String timeNow = stf.format(new Date());*/

        //early testing populating the db (old)
        /*ContentValues values = new ContentValues();
        values.put(Appointment.KEY_date, dateNow);
        values.put(Appointment.KEY_time, timeNow);
        values.put(Appointment.KEY_duration, 30);
        values.put(Appointment.KEY_barberName, "Lee");
        values.put(Appointment.KEY_booked, 0);
        values.put(Appointment.KEY_customerName, "Bill");
        db.insertOrThrow(Appointment.TABLE, null, values);*/

        //populate db uncomment as needed if database rebuilt
        /*dbHelper.addAppointment("01-06-2021", "15:30", "BOB");
        dbHelper.addAppointment("12-06-2021", "12:30", "BILLY");
        dbHelper.addAppointment("06-06-2021", "17:30", "BOB");
        dbHelper.addAppointment("22-06-2021", "11:30", "BILLY");*/

        //query database
        //raw query as cursor needs id as _id change with caution!!
        cursor = db.rawQuery("SELECT id as _id, id, date, time, barberName, duration, booked, customerName FROM Appointments WHERE booked = 0 ORDER BY date DESC", null);


        //custom db adaptor populates list
        myList = (ListView) this.findViewById(R.id.myListView);
        cursorAdaptor = new dbCustomAdapter(this, cursor);
        myList.setAdapter(cursorAdaptor);
        cursorAdaptor.changeCursor(cursor);
        myList.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.logout_menu){
            Log.i("User", "Logging out user");
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            return true;
        }else{
            Log.i("Menu", "Option not yet added to case");
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //log event
        //Log.i("Lee", "Clicked " + appointmentList.get(position));

        Log.i("Lee", "Item Clicked Start");
        cursor.moveToPosition(position);
        int valID = cursor.getInt(cursor.getColumnIndex("_id"));
        String valTime = cursor.getString(cursor.getColumnIndex(Appointment.KEY_time));
        String valDate = cursor.getString(cursor.getColumnIndex(Appointment.KEY_date));
        Log.i("Lee", "Clicked " + valID);
        Log.i("Lee", "Clicked " + valTime);

        //create dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        String msg = "Confirm booking for " + valTime + " on " + valDate + ".";

        DialogInterface.OnClickListener dcl = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE: {
                        //call booking function
                        dbHelper.bookAppointment(valID, username);
                        //update list view to reflect changes
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        cursor = db.rawQuery("SELECT id as _id, id, date, time, barberName, duration, booked, customerName FROM Appointments WHERE booked = 0 ORDER BY date DESC", null);
                        cursorAdaptor.swapCursor(cursor);
                        cursorAdaptor.notifyDataSetChanged();
                        //log
                        Log.i("Booking", "Booking successful");
                        //toast
                        Context context = getApplicationContext();
                        CharSequence text = "Booking successful";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    break;
                    case DialogInterface.BUTTON_NEGATIVE: {
                        //log outcome
                        Log.i("Booking", "Booking stopped");
                        //toast
                        Context context = getApplicationContext();
                        CharSequence text = "Booking terminated";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    break;
                    default: assert false : "unreachable";
                }
            }
        };

        builder.setMessage(msg);
        builder.setPositiveButton("Yes", dcl);
        builder.setNegativeButton("No", dcl);
        builder.show();
    }
    public void goToCustBookings(View view){
        //intent to pass username
        Intent intent = new Intent(this,CustomerBookings.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}