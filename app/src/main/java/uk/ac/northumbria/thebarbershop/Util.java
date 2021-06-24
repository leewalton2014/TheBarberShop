package uk.ac.northumbria.thebarbershop;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Util class for error dialogs and elements used in multiple classes
 */
public class Util extends AppCompatActivity {

    /**
     * errorDialog
     * @param msg - error message
     * @param c - view.getContext(); in most cases must be passed from previous activity
     */
    public static void errorDialog(String msg, Context c){
        //log
        Log.i("Error Message", msg);
        //create dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        //String msg = "Staff members must enter a name";

        DialogInterface.OnClickListener dcl = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEGATIVE: {
                        //log outcome
                        Log.i("Info", "Clicked negative.");
                        //toast
                        //Context context = getApplicationContext();
                        CharSequence text = "Try again";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(c, text, duration);
                        toast.show();
                    }
                    break;
                    default: assert false : "unreachable";
                }
            }
        };
        builder.setTitle("Error Message");
        builder.setMessage(msg);
        builder.setIcon(R.drawable.logo);
        builder.setNegativeButton("Try again", dcl);
        builder.show();
    }
}
