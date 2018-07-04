package takshak.mace.takshak2k18;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends ArrayAdapter<StudentObject> {
    LayoutInflater layoutInflater;
    ArrayList<StudentObject> studentObjectLists ;
    public MyListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<StudentObject> objects) {
        super(context, resource, objects);
        this.studentObjectLists = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.mylist, null);
        TextView textViewIdentifier = (TextView) v.findViewById(R.id.identifier);
        TextView textViewName = (TextView) v.findViewById(R.id.name);
        TextView textViewMobileno = (TextView) v.findViewById(R.id.mobileno);

        textViewIdentifier.setText(studentObjectLists.get(position).getIdentifier());
        textViewName.setText(studentObjectLists.get(position).getName());
        textViewMobileno.setText(studentObjectLists.get(position).getMobileno());

        return v;
    }
}
