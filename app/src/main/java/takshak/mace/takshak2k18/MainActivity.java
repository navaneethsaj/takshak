package takshak.mace.takshak2k18;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;
    String url = "https://demo1275613.mockable.io/test";
    FirebaseDatabase database;
    DatabaseReference notificationRef;
    ProgressBar progressBar;
    //TextView notificationTextview, readMore;
    //boolean ismessageExpanded = false;
    //LinearLayout notificationlayout;
    EditText messagebox;
    AlertDialog.Builder builder;
    Button sendbutton;
    AlertDialog dialog;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getColor(R.color.blue));
        toolbar.setTitleTextColor(Color.WHITE);




        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setRippleColor(Color.GREEN);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //goes from here
        listView = (ListView) findViewById(R.id.listview);
        messagebox = findViewById(R.id.messageBox);
        sendbutton = findViewById(R.id.messagesendButton);

        database = FirebaseDatabase.getInstance();
        notificationRef = database.getReference("notification");

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Wait").setMessage("Fetching applicant list from server");
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);


        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendbutton.setText("Sending");
                messagebox.setEnabled(false);
                sendbutton.setEnabled(false);
                notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String prevmsg = dataSnapshot.child("message").getValue().toString();
                        notificationRef.child("message").setValue("\u25C6"+messagebox.getText().toString() +"\n\n"+ prevmsg);
                        messagebox.setText("");
                        sendbutton.setText("Send");
                        Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_SHORT).show();
                        messagebox.setEnabled(true);
                        sendbutton.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        /*notificationRef = database.getReference("notification");
        notificationlayout = findViewById(R.id.notificationlayout);
        notificationTextview = findViewById(R.id.notificationBox);
        readMore =findViewById(R.id.readmore);
        notificationTextview.setMaxLines(2);

        notificationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String message = dataSnapshot.child("message").getValue().toString();
                notificationTextview.setText(message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        notificationlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ismessageExpanded == false){
                    notificationTextview.setMaxLines(15);
                    readMore.setText("Show less");
                    ismessageExpanded = true;
                }else {
                    notificationTextview.setMaxLines(2);
                    readMore.setText("Show more");
                    ismessageExpanded = false;
                }
            }
        });
        */

        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            // notify user you are online
            new MyAsyncTask().execute(url);

        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
                Toast.makeText(this,"Device offline\nConnect to internet and restart app",Toast.LENGTH_LONG).show();
            // notify user you are not online
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.login) {
            // login activity
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);


        }
         else if (id == R.id.about) {
            //about

        } else if (id == R.id.locate) {
            //locate in maps
            double latitude = 10.053952;
            double longitude = 76.619336;
            String label = "We are Here !";
            String uriBegin = "geo:" + latitude + "," + longitude;
            String query = latitude + "," + longitude + "(" + label + ")";
            String encodedQuery = Uri.encode(query);
            String uriString = uriBegin + "?q=" + encodedQuery + "&z=5";
            Uri uri = Uri.parse(uriString);
            Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
            startActivity(mapIntent);

        } else if (id ==R.id.facebook){
            //facebook
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyAsyncTask extends AsyncTask<String, Void, ArrayList<StudentObject>>{

        @Override
        protected ArrayList<StudentObject> doInBackground(String... strings) {
            ArrayList<StudentObject> studentLists = new ArrayList<StudentObject>();
            /*studentLists.add(new StudentObject("name1","id1","987654321"));
            studentLists.add(new StudentObject("name2","id2","887654321"));
            studentLists.add(new StudentObject("name4","id3","787654321"));
            */
            String url = strings[0];
            Log.d(url,"ok");
            String responseJson = null;

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    Log.d("response","ok");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    responseJson = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("response body ",responseJson);

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(responseJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i =0; i<jsonArray.length(); ++i){
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    studentLists.add(new StudentObject(jsonObject.getString("name"),jsonObject.getString("id"),jsonObject.getString("phno")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return studentLists;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<StudentObject> studentObjects) {
            super.onPostExecute(studentObjects);
            MyListAdapter adapter = new MyListAdapter(getApplicationContext(),R.layout.mylist,studentObjects);
            listView.setAdapter(adapter);
            dialog.dismiss();
        }
    }
}
