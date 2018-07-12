package takshak.mace.takshak2k18;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends ArrayAdapter<StudentObject> {
    LayoutInflater layoutInflater;
    String MyPREFERENCES = "buttondisablepreference";
    FirebaseDatabase database;
    DatabaseReference myRef;
    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ArrayList<StudentObject> studentObjectLists ;
    private FirebaseAuth mAuth;
    public MyListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<StudentObject> objects) {
        super(context, resource, objects);
        this.studentObjectLists = objects;
        this.context = context;

         database = FirebaseDatabase.getInstance();
         myRef = database.getReference("ranklist");

         sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

         editor = sharedpreferences.edit();
         /*mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Log.d("auth","Not Authenticated");
            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Log.d("auth", "signInAnonymously:success");
                    }else {
                        Log.w("auth", "signInAnonymously:failure", task.getException());
                    }
                }
            });
        }else {
            Log.d("auth","Authenticated user");
        }*/
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
        final Button rankbutton = (Button) v.findViewById(R.id.rankbutton);

        textViewIdentifier.setText(studentObjectLists.get(position).getIdentifier());
        textViewName.setText(studentObjectLists.get(position).getName());
        textViewMobileno.setText(studentObjectLists.get(position).getMobileno());

        if(sharedpreferences.getBoolean(studentObjectLists.get(position).getIdentifier(),false) == true){
            rankbutton.setText("Ranked");
            rankbutton.setEnabled(false);
        }else {
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
                    editor.putBoolean(studentObjectLists.get(position).getIdentifier(),true);
                    editor.commit();
                    rankbutton.setText("Ranked");
                    rankbutton.setEnabled(false);
                }
            });
        }
        return v;
    }
}
