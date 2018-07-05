package takshak.mace.takshak2k18;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends ArrayAdapter<StudentObject> {
    LayoutInflater layoutInflater;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Context context;
    ArrayList<StudentObject> studentObjectLists ;
    public MyListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<StudentObject> objects) {
        super(context, resource, objects);
        this.studentObjectLists = objects;
        this.context = context;

         database = FirebaseDatabase.getInstance();
         myRef = database.getReference("ranklist");

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.mylist, null);
        TextView textViewIdentifier = (TextView) v.findViewById(R.id.identifier);
        TextView textViewName = (TextView) v.findViewById(R.id.name);
        TextView textViewMobileno = (TextView) v.findViewById(R.id.mobileno);
        Button rankbutton = (Button) v.findViewById(R.id.rankbutton);

        textViewIdentifier.setText(studentObjectLists.get(position).getIdentifier());
        textViewName.setText(studentObjectLists.get(position).getName());
        textViewMobileno.setText(studentObjectLists.get(position).getMobileno());

        rankbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"clicked user id : "+studentObjectLists.get(position).getIdentifier()+"\nMake url request",Toast.LENGTH_SHORT).show();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int rankupValue = 5;
                        int rank;
                        Object obj = dataSnapshot.child(studentObjectLists.get(position).getIdentifier()).getValue();
                        if (obj != null){
                            rank = Integer.valueOf(obj.toString());
                        }else {
                            rank = 0;
                        }
                        myRef.child(studentObjectLists.get(position).getIdentifier()).setValue(rank+rankupValue);
                        Toast.makeText(context,
                                "UID "+studentObjectLists.get(position).getIdentifier()+
                                    "\nScore "+(rank+rankupValue)
                                    ,Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        return v;
    }
}
