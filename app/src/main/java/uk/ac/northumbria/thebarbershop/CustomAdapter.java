package uk.ac.northumbria.thebarbershop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * OLD CLASS
 * used in earlier version which built list view from array adapter
 */
public class CustomAdapter extends ArrayAdapter<appointmentInfo> {

    List<appointmentInfo> appointmentList;
    Context myContext;

    public CustomAdapter(@NonNull Context context, @NonNull List<appointmentInfo> objects) {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
        appointmentList = objects;
        myContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //View view = super.getView(position, convertView, parent);
        /*
        TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) view.findViewById(android.R.id.text2);

        textView1.setText( appointmentList.get(position).getDate());
        textView2.setText("Additional Infof for " + appointmentList.get(position).getTime());*/

        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService( myContext.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.customrow, parent, false);

        TextView textView1 = (TextView) rowView.findViewById(R.id.list_text1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.list_text2);

        textView1.setText( appointmentList.get(position).getDate());
        textView2.setText("Additional info for " + appointmentList.get(position).getTime());

        return rowView;
    }
}
