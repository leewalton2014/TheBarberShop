package uk.ac.northumbria.thebarbershop;

import android.app.LauncherActivity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * dbCustomAdapter used to convert cursor into list view to show content
 * list view built using custom view in xml layout file allows for styling and multiple lines of text
 */
public class dbCustomAdapter extends CursorAdapter {

    //List<appointmentInfo> appointmentList;
    Context myContext;

    public dbCustomAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        //appointmentList = objects;
        myContext = context;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.customrow, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView textView1 = (TextView) view.findViewById(R.id.list_text1);
        TextView textView2 = (TextView) view.findViewById(R.id.list_text2);

        // Extract properties from cursor
        String idC = cursor.getString(cursor.getColumnIndexOrThrow(Appointment.KEY_ID));
        String dateC = cursor.getString(cursor.getColumnIndexOrThrow(Appointment.KEY_date));
        String timeC = cursor.getString(cursor.getColumnIndexOrThrow(Appointment.KEY_time));
        String durationC = cursor.getString(cursor.getColumnIndexOrThrow(Appointment.KEY_duration));
        String barberC = cursor.getString(cursor.getColumnIndexOrThrow(Appointment.KEY_barberName));
        int bookedC = cursor.getInt(cursor.getColumnIndexOrThrow(Appointment.KEY_booked));
        String customerC = cursor.getString(cursor.getColumnIndexOrThrow(Appointment.KEY_customerName));

        //build the string values for the text in the custom rows
        String heading = dateC + " @ " + timeC + " (" + durationC + " Mins)";
        String body;

        //if the booking is booked show this info if no booking only show barber name
        if (bookedC == 1){
            //booked show who booked slot
            if (barberC == null){
                body = "Booked by " + customerC;
            }else{
                body = "Booked by " + customerC + " with barber " + barberC;
            }

        }else{
            //not yet booked display barber
            if (barberC == null){
                body = "Available";
            }else{
                body = "Available with " + barberC;
            }
        }



        // Populate fields with extracted properties
        textView1.setText(heading);
        textView2.setText(body);

    }
}
