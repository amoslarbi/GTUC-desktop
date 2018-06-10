package morelifeinc.gtuc;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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

public class Courselist extends AppCompatActivity implements SearchView.OnQueryTextListener {

    TextView textView5;
    ListView listView;
    SearchView search;
    FloatingActionButton aboutback;
    EditText editText, editText1, watts;
    ArrayList<String> depa;
    ArrayList<String> ccode;
    ArrayList<String> lec;
    // String[] depa;
    MyAdapter adapter;
    //ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courselist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Student Portal");

        textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setText(getIntent().getStringExtra("food"));

        
        listView = (ListView) findViewById(R.id.listview);
        depa = new ArrayList<>();
        ccode = new ArrayList<>();
        lec = new ArrayList<>();

        adapter = new MyAdapter(getApplicationContext(), depa, ccode, lec);
        //adapter = new ArrayAdapter<String>(this, depa);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(this);

        HashMap<String, String> postData = new HashMap<String, String>();

        postData.put("depa", textView5.getText().toString());

        PostResponseAsyncTask task = new PostResponseAsyncTask(Courselist.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String str) {
                //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

                if (str.contains("success")) {

                    try {
                        JSONArray jArray = new JSONArray(str);

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject data = jArray.getJSONObject(i);
                            Log.d("JSONResponse", String.valueOf(data));

                            depa.add(data.getString("courses"));
                            ccode.add(data.getString("course_code"));
                            lec.add(data.getString("Lecturer"));

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

                    SweetAlertDialog su = new SweetAlertDialog(Courselist.this, SweetAlertDialog.ERROR_TYPE);
                    su.setTitleText("Exception handler failed");
                    su.show();

                }

            }
        });

        //task.execute("http://aroma.one957.com/upload.php");
        //task.execute("http://192.168.137.1:8012/client/upload.php");
        task.execute("http://gtuc.one957.com/bringcourses.php");
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){

//                String food = String.valueOf(parent.getItemAtPosition(position));
//                //Toast.makeText(getApplicationContext(), food, Toast.LENGTH_SHORT).show();
//                Intent StartIntent = new Intent(getApplicationContext(), Courselist.class);
//
//                StartIntent.putExtra("food",food);
//
//                startActivity(StartIntent);

            }

        });


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

    static class MyAdapter extends ArrayAdapter {
        ArrayList<String> applianceArray;
        ArrayList<String> ccodeArray;
        ArrayList<String> lecArray;

        public MyAdapter(Context applicationContext, ArrayList<String> depa, ArrayList<String> ccode, ArrayList<String> lec) {
            super(applicationContext, R.layout.depalist, R.id.number1, depa);
            this.applianceArray = depa;
            this.ccodeArray = ccode;
            this.lecArray = lec;

        }


        @NotNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            //if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.depalist, parent, false);
            // }

            TextView appliance = (TextView) view.findViewById(R.id.number1);
            TextView ccode = (TextView) view.findViewById(R.id.appliance);
            TextView lec = (TextView) view.findViewById(R.id.watts);
            appliance.setText(applianceArray.get(position));
            ccode.setText(ccodeArray.get(position));
            lec.setText(lecArray.get(position));

            return view;
        }
    }

}
