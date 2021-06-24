package uk.ac.northumbria.thebarbershop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Lee Walton (17007224)
 * @version 1.0
 * Demo video uploaded to blackboard
 * Checking for clashes forgotten from demo video but functionality works as expected and checking is shown in NewAppointment.java
 * main activity used to set users name input
 * once name entered once username will be saved to user prefs and EditText will auto fill with previous input
 * where params are view this is used to pass current view for context within each function
 * similar methods are used throughout so only main classes are commented to reduce over commenting similar methods
 */
public class MainActivity extends AppCompatActivity {

    EditText username;
    Button btnStaff, btnCustomer;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if preferences populate name
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String defaultName = sharedpreferences.getString(Name, "");
        if(!defaultName.isEmpty()){
            //if name is saved then populate edit text
            username = (EditText)findViewById(R.id.txt_enterName);
            username.setText(defaultName);
        }


    }

    /**
     * Intent to go to customer view attached to onClick for customer button within xml layout file
     */
    public void goToCustomer (View view){
        //get name
        username = (EditText)findViewById(R.id.txt_enterName);
        //save name for next time
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, username.getText().toString());
        editor.apply();

        //intent
        Intent intent = new Intent(this,CustomerView.class);
        intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    /**
     * Intent to go to staff view attached to onClick for staff button within xml layout file
     * staff have to enter name and cannot be guest
     */
    public void goToStaff (View view){
        //get name
        username = (EditText)findViewById(R.id.txt_enterName);
        String userTxt = username.getText().toString();

        //save name for next time
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, username.getText().toString());
        editor.apply();

        if (userTxt.isEmpty()){
            //error message if no staff name provided
            String msg = "Staff members must enter a name";
            //log
            Log.i("Warning", msg);
            //error message
            Util.errorDialog(msg, view.getContext());

        }else{
            //if not empty go to staff page
            //intent
            Intent intent = new Intent(this,StaffView.class);
            intent.putExtra("username", userTxt);
            startActivity(intent);
        }

    }
}