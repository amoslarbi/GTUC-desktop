package morelifeinc.gtuc;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

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
import java.util.List;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Courselist extends AppCompatActivity implements SearchView.OnQueryTextListener {

    TextView textView5, scroll, textView8;
    ListView listView;
    SearchView search;
    FloatingActionButton forward;
    EditText acyear, edin;
    Button choose;
    ArrayList<String> depa;
    ArrayList<String> ccode;
    ArrayList<String> lec;
    //an array to hold the different pdf objects
    ArrayList<Pdf> pdfList= new ArrayList<Pdf>();

    PdfAdapter pdfAdapter;
    //MyAdapter adapter;
    AlertDialog.Builder sett;
    View mview;
    FloatingActionButton sposted;
    Spinner s1, s2, s3;
    String path;

    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    public static final String UPLOAD_URL = "http://192.168.43.234/pdf/pdf.php";
    public static final String PDF_FETCH_URL = "http://192.168.43.234/pdf/getPdfs.php";


    //Uri to store the image uri
    private Uri filePath;
    SwipeRefreshLayout swiperefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courselist);

        //Requesting storage permission
        requestStoragePermission();
        swiperefresh=(SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        listView = (ListView) findViewById(R.id.listview);

        textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setText(getIntent().getStringExtra("food"));
        //Toast.makeText(getApplicationContext(), textView5.getText().toString(), Toast.LENGTH_SHORT).show();
        HashMap<String, String> postData = new HashMap<String, String>();

        postData.put("depa", textView5.getText().toString());
        //postData.put("mobile","android");

        PostResponseAsyncTask task = new PostResponseAsyncTask(Courselist.this,postData, new AsyncResponse() {
            @Override
            public void processFinish(String str) {

                if(str.contains("success")) {

                    try {
                        JSONArray jArray = new JSONArray(str);
                        //Toast.makeText(getApplicationContext(), String.valueOf(str), Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jsonObject = jArray.getJSONObject(i);


                            //Declaring a Pdf object to add it to the ArrayList  pdfList
                            Pdf pdf  = new Pdf();
                            String pdfName = jsonObject.getString("name");
                            String pdfUrl = jsonObject.getString("url");
                            String pdfDepartment = jsonObject.getString("coursename");
                            String pdfProgram = jsonObject.getString("program");
                            String pdfAcademicyear = jsonObject.getString("academicyear");
                            String pdfLname = jsonObject.getString("lname");
                            pdf.setUrl(pdfUrl);
                            pdf.setDepartment(pdfDepartment);
                            pdf.setProgram(pdfProgram);
                            pdf.setAcademicyear(pdfAcademicyear);
                            pdf.setLname(pdfLname);
                            pdfList.add(pdf);

                        }

                        pdfAdapter=new PdfAdapter(Courselist.this,R.layout.depalist, pdfList);
                        listView.setAdapter(pdfAdapter);
                        pdfAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if(str.contains("Failed")){

                    SweetAlertDialog su = new SweetAlertDialog(Courselist.this, SweetAlertDialog.ERROR_TYPE);
                    su.setTitleText("Sorry no questions available");
                    su.show();

                }

            }
        });

        //task.execute( "http://gtuc.one957.com/getPdfs.php");
        //task.execute("http://192.168.137.1:8012/client/upload.php");
        task.execute( "http://192.168.43.234/pdf/getPdfs.php");
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


        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // code to refresh

                pdfList.clear();
                HashMap<String, String> postData = new HashMap<String, String>();

                postData.put("depa", textView5.getText().toString());
                //postData.put("mobile","android");

                PostResponseAsyncTask task = new PostResponseAsyncTask(Courselist.this,postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String str) {

                        if(str.contains("success")) {

                            try {
                                JSONArray jArray = new JSONArray(str);
                                //Toast.makeText(getApplicationContext(), String.valueOf(str), Toast.LENGTH_SHORT).show();

                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject jsonObject = jArray.getJSONObject(i);


                                    //Declaring a Pdf object to add it to the ArrayList  pdfList
                                    Pdf pdf  = new Pdf();
                                    String pdfName = jsonObject.getString("name");
                                    String pdfUrl = jsonObject.getString("url");
                                    String pdfDepartment = jsonObject.getString("coursename");
                                    String pdfProgram = jsonObject.getString("program");
                                    String pdfAcademicyear = jsonObject.getString("academicyear");
                                    String pdfLname = jsonObject.getString("lname");
                                    pdf.setUrl(pdfUrl);
                                    pdf.setDepartment(pdfDepartment);
                                    pdf.setProgram(pdfProgram);
                                    pdf.setAcademicyear(pdfAcademicyear);
                                    pdf.setLname(pdfLname);
                                    pdfList.add(pdf);

                                }

                                pdfAdapter=new PdfAdapter(Courselist.this,R.layout.depalist, pdfList);
                                listView.setAdapter(pdfAdapter);
                                pdfAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        if(str.contains("Failed")){

                            SweetAlertDialog su = new SweetAlertDialog(Courselist.this, SweetAlertDialog.ERROR_TYPE);
                            su.setTitleText("Sorry no questions available");
                            su.show();

                        }

                    }
                });

                //task.execute( "http://gtuc.one957.com/getPdfs.php");
                //task.execute("http://192.168.137.1:8012/client/upload.php");
                task.execute( "http://192.168.43.234/pdf/getPdfs.php");
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

                swiperefresh.setRefreshing(false);   //code to stop refresh animation

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Pdf pdf = (Pdf) parent.getItemAtPosition(position);
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse(pdf.getUrl()));
//                startActivity(intent);

                Intent startMainScreen = new Intent(getApplicationContext(),view.class);
                startMainScreen.putExtra("name", pdf.getUrl());

                startActivity(startMainScreen);

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Student Portal");

        textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setText(getIntent().getStringExtra("food"));


//        listView = (ListView) findViewById(R.id.listview);
//        depa = new ArrayList<>();
//        ccode = new ArrayList<>();
//        lec = new ArrayList<>();

//        adapter = new MyAdapter(getApplicationContext(), depa, ccode, lec);
//        //adapter = new ArrayAdapter<String>(this, depa);
//        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(this);

//        HashMap<String, String> postData = new HashMap<String, String>();
//
//        postData.put("depa", textView5.getText().toString());
//
//        PostResponseAsyncTask task = new PostResponseAsyncTask(Leccourselist.this, postData, new AsyncResponse() {
//            @Override
//            public void processFinish(String str) {
//                //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
//
//                if (str.contains("success")) {
//
//                    try {
//                        JSONArray jArray = new JSONArray(str);
//
//                        for (int i = 0; i < jArray.length(); i++) {
//                            JSONObject data = jArray.getJSONObject(i);
//                            Log.d("JSONResponse", String.valueOf(data));
//
//                            depa.add(data.getString("courses"));
//                            ccode.add(data.getString("course_code"));
//                            lec.add(data.getString("Lecturer"));
//
//                            adapter.notifyDataSetChanged();
//                            //String kiev = textView3.getText().toString();
//                        }
//
//                    } catch (JSONException e) {
//
//                        e.printStackTrace();
//                    }
//
//
////                    SweetAlertDialog su = new SweetAlertDialog(Lecportal.this, SweetAlertDialog.SUCCESS_TYPE);
////                    su.setTitleText("Account created");
////                    su.show();
//
//                }
//
//                if (str.contains("Failed")) {
//
//                    SweetAlertDialog su = new SweetAlertDialog(Leccourselist.this, SweetAlertDialog.ERROR_TYPE);
//                    su.setTitleText("Exception handler failed");
//                    su.show();
//
//                }
//
//            }
//        });
//
//        //task.execute("http://aroma.one957.com/upload.php");
//        //task.execute("http://192.168.137.1:8012/client/upload.php");
//        task.execute("http://gtuc.one957.com/bringcourses.php");
//        task.setEachExceptionsHandler(new EachExceptionsHandler() {
//            @Override
//            public void handleIOException(IOException e) {
//                Toast.makeText(getApplicationContext(), "Cannot Connect to server  ", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void handleMalformedURLException(MalformedURLException e) {
//                Toast.makeText(getApplicationContext(), "URL Error ", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void handleProtocolException(ProtocolException e) {
//                Toast.makeText(getApplicationContext(), "Protocol Error ", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
//                Toast.makeText(getApplicationContext(), "Encoding Error ", Toast.LENGTH_SHORT).show();
//
//            }
//
//        });

//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
//
////                String food = String.valueOf(parent.getItemAtPosition(position));
////                //Toast.makeText(getApplicationContext(), food, Toast.LENGTH_SHORT).show();
////                Intent StartIntent = new Intent(getApplicationContext(), Courselist.class);
////
////                StartIntent.putExtra("food",food);
////
////                startActivity(StartIntent);
//
//            }
//
//        });


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        pdfAdapter.getFilter().filter(newText);

        return false;
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Tap to download selected document");
        menu.add(0,v.getId(),0,"Download");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if (item.getTitle() == "Download"){

            //Pdf pdf = (Pdf) parent.getItemAtPosition(position);
            Pdf pdf = (Pdf) listView.getItemAtPosition(info.position);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(pdf.getUrl()));
            startActivity(intent);


        }

        return super.onContextItemSelected(item);


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

//    static class MyAdapter extends ArrayAdapter {
//        ArrayList<String> applianceArray;
//        ArrayList<String> ccodeArray;
//        ArrayList<String> lecArray;
//
//        public MyAdapter(Context applicationContext, ArrayList<String> depa, ArrayList<String> ccode, ArrayList<String> lec) {
//            super(applicationContext, R.layout.depalist, R.id.number1, depa);
//            this.applianceArray = depa;
//            this.ccodeArray = ccode;
//            this.lecArray = lec;
//
//        }
//
//
//        @NotNull
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View view = convertView;
//            //if (view == null) {
//            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(R.layout.depalist, parent, false);
//            // }
//
//            TextView appliance = (TextView) view.findViewById(R.id.number1);
//            TextView ccode = (TextView) view.findViewById(R.id.appliance);
//            TextView lec = (TextView) view.findViewById(R.id.watts);
//            appliance.setText(applianceArray.get(position));
//            ccode.setText(ccodeArray.get(position));
//            lec.setText(lecArray.get(position));
//
//            return view;
//        }
//    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            if (filePath != null ) {
                scroll.setText(String.valueOf(filePath));
                //Toast.makeText(Leccourselist.this, String.valueOf(path), Toast.LENGTH_LONG).show();

            }

        }


    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}
