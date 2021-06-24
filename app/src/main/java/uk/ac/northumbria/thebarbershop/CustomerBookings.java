package uk.ac.northumbria.thebarbershop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class CustomerBookings extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Intent variables
    String username;
    Intent user;

    //Database info
    DBJHelper dbHelper;
    Cursor cursor;
    dbCustomAdapter cursorAdaptor;

    //elements
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_bookings);

        //get username from intent
        user = getIntent();
        username = user.getStringExtra("username");

        if (username.equals("")){
            username = "guest";
        }

        this.setTitle("Bookings for " + username);

        //database
        dbHelper = new DBJHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //query using ? as a prepared statement values are added in selection args string to prevent sql injection
        String sql = "SELECT id as _id, id, date, time, barberName, duration, booked, customerName FROM Appointments WHERE booked = 1 AND customerName = ? ORDER BY date DESC";
        //raw query as cursor needs id as _id
        cursor = db.rawQuery(sql, new String[] {String.valueOf(username)});

        //custom db adaptor
        myList = (ListView) this.findViewById(R.id.myListView);
        cursorAdaptor = new dbCustomAdapter(this, cursor);
        myList.setAdapter(cursorAdaptor);
        cursorAdaptor.changeCursor(cursor);
        myList.setOnItemClickListener(this);

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
        String msg = "Are you sure you want to cancel booking for " + valTime + " on " + valDate + ".";

        DialogInterface.OnClickListener dcl = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE: {
                        //call booking function
                        dbHelper.cancelAppointment(valID);
                        //update list view to reflect changes
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        //query using ? as a prepared statement values are added in selection args string to prevent sql injection
                        String sql = "SELECT id as _id, id, date, time, barberName, duration, booked, customerName FROM Appointments WHERE booked = 1 AND customerName = ? ORDER BY date DESC";
                        //raw query as cursor needs id as _id
                        cursor = db.rawQuery(sql, new String[] {String.valueOf(username)});
                        //setup custom adaptor
                        cursorAdaptor.swapCursor(cursor);
                        cursorAdaptor.notifyDataSetChanged();
                        //log
                        Log.i("Booking", "Cancelled Booking");
                        //toast
                        Context context = getApplicationContext();
                        CharSequence text = "Cancelled Booking";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    break;
                    case DialogInterface.BUTTON_NEGATIVE: {
                        //log outcome
                        Log.i("Booking", "Cancellation interrupted");
                        //toast
                        Context context = getApplicationContext();
                        CharSequence text = "Cancellation interrupted";
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

    public void goToCustView(View view){
        //intent back to previous view
        //intent to pass username
        Intent intent = new Intent(this,CustomerView.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}