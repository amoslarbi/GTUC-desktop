package morelifeinc.gtuc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.ContextMenu;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.security.AccessController.getContext;

public class Lecportal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private TextView names, wtf;
    public static final String hello = "hellol";
    public static final String qhello = "hellolol";

    ListView listView;
    SearchView search;
    FloatingActionButton aboutback;
    EditText editText, editText1, watts;
    ArrayList<String> depa;
    // String[] depa;
    //MyAdapter adapter;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecportal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lecturer Portal");

        listView = (ListView) findViewById(R.id.listview);
        depa = new ArrayList<>();

        //adapter = new MyAdapter(getApplicationContext(), depa);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, depa);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(this);

        HashMap<String, String> postData = new HashMap<String, String>();

        PostResponseAsyncTask task = new PostResponseAsyncTask(Lecportal.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String str) {
                //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

                if (str.contains("success")) {


                    try {
                        JSONArray jArray = new JSONArray(str);

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject data = jArray.getJSONObject(i);
                            Log.d("JSONResponse", String.valueOf(data));

                            depa.add(data.getString("depa"));

                            adapter.notifyDataSetChanged();
                            //String kiev = textView3.getText().toString();
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


//                    SweetAlertDialog su = new SweetAlertDialog(Lecportal.this, SweetAlertDialog.SUCCESS_TYPE);
//                    su.setTitleText("Account created");
//                    su.show();

                }

                if (str.contains("Failed")) {

                    SweetAlertDialog su = new SweetAlertDialog(Lecportal.this, SweetAlertDialog.ERROR_TYPE);
                    su.setTitleText("Exception handler failed");
                    su.show();

                }

            }
        });

        //task.execute("http://aroma.one957.com/upload.php");
        //task.execute("http://192.168.137.1:8012/client/upload.php");
        task.execute("http://gtuc.one957.com/courses.php");
        task.setEachExceptionsHandler(new EachExceptionsHandler() {
            @Override
            public void handleIOException(IOException e) {
                Toast.makeText(getApplicationContext(), "Cannot Connect to server  ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleMalformedURLException(MalformedURLException e) {
                Toast.makeText(getApplicationContext(), "URL Error ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleProtocolException(ProtocolException e) {
                Toast.makeText(getApplicationContext(), "Protocol Error ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                Toast.makeText(getApplicationContext(), "Encoding Error ", Toast.LENGTH_SHORT).show();

            }

        });


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hvv = navigationView.getHeaderView(0);

        names = (TextView) hvv.findViewById(R.id.name);
        wtf = (TextView) hvv.findViewById(R.id.id);

        SharedPreferences sh = getSharedPreferences(hello, 0);
        names.setText(sh.getString("id", "nmoo"));
        wtf.setText(sh.getString("name", "nmoo1"));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){

                String food = String.valueOf(parent.getItemAtPosition(position));
                //Toast.makeText(getApplicationContext(), food, Toast.LENGTH_SHORT).show();
                Intent StartIntent = new Intent(getApplicationContext(), Leccourselist.class);

                StartIntent.putExtra("food",food);
                StartIntent.putExtra("lname",wtf.getText().toString());

                startActivity(StartIntent);

            }

        });


    }

    @Override
    public void onBackPressed() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent startMainScreen = new Intent(getApplicationContext(),Aboutus.class);
            startActivity(startMainScreen);

        } else if (id == R.id.nav_slideshow) {

            Intent startMainScreen = new Intent(getApplicationContext(),Userguide.class);
            startActivity(startMainScreen);

        } else if (id == R.id.nav_manage) {

            Intent startMainScreen = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(startMainScreen);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.getFilter().filter(newText);

        return false;
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.setHeaderTitle("Tap to Delete Appliance");
//        menu.add(0, v.getId(), 0, "Delete");
//
//    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


//        public MyAdapter(work work, ArrayList<String> itemlist, ArrayList<String> itemlist1, ArrayList<String> itemlist2, ArrayList<String> itemlist3) {
//            super();
//        }



}

