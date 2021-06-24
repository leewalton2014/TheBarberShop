package uk.ac.northumbria.thebarbershop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * class to manage staff input for creating new appointments on the system for users to book
 */
public class NewAppointment extends AppCompatActivity {

    //Intent variables
    String username;
    Intent user;

    TextView answer;
    Button btnDatePicker, btnTimePicker;
    TextView txtDate, txtTime, eventFreq;
    int mYear, mMonth, mDay, mHour, mMinute;
    String finalTime, finalDate;
    String convMin, convHour, convDay, convMonth;

    //multiple event vars
    NumberPicker np;
    int increment, numEvents;

    DBJHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        //get info from intent
        user = getIntent();
        username = user.getStringExtra("username");

        if (username.equals("")){
            username = "guest";
        }
        //make title
        this.setTitle("Staff View " + username);
        //set increment default
        increment = 0;
        finalDate = "";
        finalTime = "";

        //DATABASE STUFF
        dbHelper = new DBJHelper(getApplicationContext());

        //number picker
        np = (NumberPicker)findViewById(R.id.numOfEvents);
        np.setMaxValue(10);
        np.setMinValue(1);
        np.setValue(1);

        //Picker Dialog inputs
        btnDatePicker = (Button)findViewById(R.id.btn_date);
        btnTimePicker = (Button)findViewById(R.id.btn_time);
        txtDate = (TextView)findViewById(R.id.in_date);
        txtTime = (TextView)findViewById(R.id.in_time);
        eventFreq = (TextView)findViewById(R.id.event_freq);
        Log.i("Lee","On Create Method");



    }

    /**
     * assigned to onclick button to select date via dialog
     */
    public void doDate ( View view ){
        // Get Current Date
        Log.i("Lee", "Do Date");
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //convert date from format 5/5/2021 to 05/05/2021
                        if(dayOfMonth<10){
                            convDay = "0" + dayOfMonth;
                        }else{
                            convDay = "" + dayOfMonth;
                        }
                        if(monthOfYear<10){
                            convMonth = "0" + (monthOfYear + 1);
                        }else{
                            convMonth = "" + (monthOfYear + 1);
                        }
                        //update TextView
                        finalDate = convDay + "-" + convMonth + "-" + year;
                        txtDate.setText(finalDate);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    /**
     * assigned to onclick button to select time via dialog
     */
    public void doTime ( View view ){
        // Get Current Time
        Log.i("Lee", "Test1");
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        //convert time format from 5:5 to 05:05
                        if (minute<10){
                            convMin = "0" + minute;
                        }else{
                            convMin = "" + minute;
                        }
                        if(hourOfDay<10){
                            convHour = "0" + hourOfDay;
                        }else{
                            convHour = "" + hourOfDay;
                        }
                        //update TextView
                        finalTime = convHour + ":" + convMin;
                        txtTime.setText(finalTime);

                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    /**
     * assigned to onclick button to select event frequency via dialog
     */
    public void onSelectDialog(View view){
        //here
        final String[] freq = {"Daily", "Weekly", "Bi-Weekly", "4-Weeks"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an event frequency");
        builder.setItems(freq, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //set text to clicked option
                eventFreq.setText(freq[which]);
                //log selection for debugging
                Log.i("Booking", "Frequency selected " + freq[which]);

                if ("Daily".equals(freq[which])) {
                    increment = 1;
                }
                else if ("Weekly".equals(freq[which])) {
                    increment = 7;
                }
                else if ("Bi-Weekly".equals(freq[which])){
                    increment = 14;
                }
                else if ("4-Weeks".equals(freq[which])){
                    increment = 28;
                }

            }
        });
        builder.show();
    }

    /**
     * assigned to onclick button to process input data
     * do checks for validity and add the appointment info to database using db helper functions within
     */
    public void onSubmit(View view) throws ParseException {
        //do stuff
        Log.i("Lee", "Start of function");

        //old code from earlier versions and testing methods
        //TextView dateF = (TextView) view.findViewById(R.id.in_date);
        //TextView timeF = (TextView) view.findViewById(R.id.in_time);

        //convert date and time examples not needed anymore
        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        //String newDate = sdf.format(finalDate);

        //SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss");
        //String newTime = stf.format(finalTime);

        //rename username for easier reference
        String barberName = username;
        //String finalDate = dateF.getText().toString();
        //String finalTime = timeF.getText().toString();
        Log.i("Lee", "Date " + finalDate);
        Log.i("Lee", "Time " + finalTime);
        Log.i("Lee", "Barber " + barberName);

        //get num of events to add
        numEvents = np.getValue();

        //check input for empty values to ensure all data is valid
        if(finalTime.isEmpty() || finalDate.isEmpty()){
            //error
            String msg = "You must select a date and time";
            Util.errorDialog(msg, view.getContext());
        }else{
            //other checks
            String error = "";
            //check date in try catch as may be error with cal module
            try {
                SimpleDateFormat selectedDate = new SimpleDateFormat("dd-MM-yyyy");
                Date bookingDate = selectedDate.parse(finalDate);
                if (new Date().after(bookingDate)){
                    error = "Select a date in the future";
                }
            } catch (ParseException e) {
                //print error
                e.printStackTrace();
                Util.errorDialog("An error occurred", view.getContext());
            }
            //if needed by client could add checks for time also to allow bookings for later in day if date = today
            //decided for prototype not to allow since there needs to be 1 day notice for appointments

            //check time ends on or at half the hour
            if (!convMin.equals("00") && !convMin.equals("30")){
                error = "Appointments run on a half hourly basis please select a time on or at half the hour";
            }
            //check for clashes
            if (dbHelper.checkClashes(finalDate, finalTime) > 0){
                //error message
                error = "An event already exists at that time try another time";
            }


            //if errors show error message else add events
            if (!error.isEmpty()){
                Log.i("Booking", error);
                Util.errorDialog(error, view.getContext());
            }else{
                //no errors log info
                Log.i("Booking", "No errors");
                //add events
                if (numEvents > 1 && increment > 0){
                    //multiple events
                    Log.i("Booking", "Multiple events process");
                    for (int i = 0; i < numEvents; i++){
                        if (i > 0){
                            //not first event so add increment to date in try catch as may be error with cal module
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                Calendar c = Calendar.getInstance();
                                c.setTime(sdf.parse(finalDate));
                                c.add(Calendar.DATE, increment);  // number of days to add
                                finalDate = sdf.format(c.getTime());  // dt is now the new date
                            } catch (ParseException e) {
                                //print error
                                e.printStackTrace();
                                Util.errorDialog("An error occurred", view.getContext());
                            }

                        }
                        //check updated dates for clashes
                        if (dbHelper.checkClashes(finalDate, finalTime) == 0){
                            //no clashes so add to db
                            //Log dates for debugging
                            Log.i("Booking", "Booking date is " + finalDate);
                            //add to database
                            dbHelper.addAppointment(finalDate, finalTime, barberName);
                        }else{
                            Log.i("Booking", "One of the events was a clash no need to alert customer as appointment already exists and no duplicates were added");
                        }

                    }
                    //confirmation and dialog outside of for loop
                    //end if
                }else{
                    //not multiple events
                    //since single event initial input date already checked for clashes so no need to check clashes
                    Log.i("Booking", "Single event process");
                    //add event
                    dbHelper.addAppointment(finalDate, finalTime, barberName);
                    //end else
                }
                //toast with confirmation
                Context context = getApplicationContext();
                CharSequence text = "Appointment Added!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                //ask next action go to bookings or add another appointment
                nextActionDialog(view);
            }

        }


    }

    /**
     * called after records added to ask next step via dialogue add another record or view appointments
     * also clears input data if user wishes to add another record
     */
    public void nextActionDialog(View view){
        //create dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        String msg = "Do you wish to add another appointment?";

        DialogInterface.OnClickListener dcl = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE: {
                        //clear value of elements
                        txtDate.setText("");
                        txtTime.setText("");
                        np.setValue(1);

                        //log
                        Log.i("Booking", "Add another appointment");
                        //toast
                        Context context = getApplicationContext();
                        CharSequence text = "Add another appointment";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    break;
                    case DialogInterface.BUTTON_NEGATIVE: {
                        //log outcome
                        Log.i("Booking", "Return to appointment list");
                        //intent to staff screen
                        Intent intent = new Intent(NewAppointment.this,StaffView.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                    break;
                    default: assert false : "unreachable";
                }
            }
        };
        builder.setMessage(msg);
        builder.setPositiveButton("Add Another", dcl);
        builder.setNegativeButton("Back to Staff View", dcl);
        builder.show();
    }

    /**
     * goBack creates an intent to pass back the username and link the previous page to the current page
     * by passing in username this will keep the user info present and prevent null values on the previous page
     */
    public void goBack(View view){
        //intent to go back to previous page
        Intent intent = new Intent(this,StaffView.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }



}