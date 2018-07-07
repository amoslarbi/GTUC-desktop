package morelifeinc.gtuc;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Button;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import static java.security.AccessController.getContext;

public class Lecportal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private TextView names, wtf;
    public static final String hello = "hellol";
    public static final String qhello = "hellolol";

//    ListView listView;
//    SearchView search;
//    FloatingActionButton aboutback;
//    EditText editText, editText1, watts;
//    ArrayList<String> depa;
//    SwipeRefreshLayout swiperefresh;
    // String[] depa;
    //MyAdapter adapter;
    ArrayAdapter<String> adapter;

    TextView textView5, scroll, textView8;
    ListView listView;
    SearchView search;
    FloatingActionButton forward;
    EditText naa, edin;
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
    //public static final String UPLOAD_URL = "http://192.168.43.234/pdf/pdf.php";
    public static final String UPLOAD_URL = "http://gtuc.one957.com/pdf.php";


    //Uri to store the image uri
    private Uri filePath;
    SwipeRefreshLayout swiperefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecportal);

        swiperefresh=(SwipeRefreshLayout) findViewById(R.id.swiperefresh);
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

        textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setText(getIntent().getStringExtra("ldepa"));
        //Toast.makeText(getApplicationContext(), textView5.getText().toString(), Toast.LENGTH_SHORT).show();
        HashMap<String, String> postData = new HashMap<String, String>();

        postData.put("depa", textView5.getText().toString());
        //postData.put("mobile","android");

        PostResponseAsyncTask task = new PostResponseAsyncTask(Lecportal.this,postData, new AsyncResponse() {
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
                            String pdfDepartment = jsonObject.getString("program");
                            String pdfProgram = jsonObject.getString("coursename");
                            //String pdfAcademicyear = jsonObject.getString("academicyear");
                            String pdfLname = jsonObject.getString("lname");
                            pdf.setUrl(pdfUrl);
                            pdf.setDepartment(pdfDepartment);
                            pdf.setProgram(pdfProgram);
                            //pdf.setAcademicyear(pdfAcademicyear);
                            pdf.setLname(pdfLname);
                            pdfList.add(pdf);

                        }

                        pdfAdapter=new PdfAdapter(Lecportal.this,R.layout.depalist, pdfList);
                        listView.setAdapter(pdfAdapter);
                        pdfAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                if(str.contains("Failed")){

                    SweetAlertDialog su = new SweetAlertDialog(Lecportal.this, SweetAlertDialog.ERROR_TYPE);
                    su.setTitleText("Sorry no questions available");
                    su.show();

                }

            }
        });

        task.execute( "http://gtuc.one957.com/getPdfs.php");
        //task.execute( "http://192.168.43.234/pdf/getPdfs.php");
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


        ///////////////////////////////////////////////////
        ///////// Bringing .pdf and .docx from server /////
        //////////// in listview with AsyncTask  //////////
        ///////////////////////////////////////////////////


        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // code to refresh

                pdfList.clear();
                HashMap<String, String> postData = new HashMap<String, String>();

                postData.put("depa", textView5.getText().toString());
                //postData.put("mobile","android");

                PostResponseAsyncTask task = new PostResponseAsyncTask(Lecportal.this,postData, new AsyncResponse() {
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
                                    String pdfDepartment = jsonObject.getString("program");
                                    String pdfProgram = jsonObject.getString("coursename");
                                    //String pdfAcademicyear = jsonObject.getString("academicyear");
                                    String pdfLname = jsonObject.getString("lname");
                                    pdf.setUrl(pdfUrl);
                                    pdf.setDepartment(pdfDepartment);
                                    pdf.setProgram(pdfProgram);
                                    //pdf.setAcademicyear(pdfAcademicyear);
                                    pdf.setLname(pdfLname);
                                    pdfList.add(pdf);

                                }

                                pdfAdapter=new PdfAdapter(Lecportal.this,R.layout.depalist, pdfList);
                                listView.setAdapter(pdfAdapter);
                                pdfAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        if(str.contains("Failed")){

                            SweetAlertDialog su = new SweetAlertDialog(Lecportal.this, SweetAlertDialog.ERROR_TYPE);
                            su.setTitleText("Sorry no questions available");
                            su.show();

                        }

                    }
                });

                task.execute( "http://gtuc.one957.com/getPdfs.php");
                //task.execute("http://192.168.137.1:8012/client/upload.php");
                //task.execute( "http://192.168.43.234/pdf/getPdfs.php");
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

        ///////////////////////////////////////////////////
        /////////// Intent to go to and view .pdf and /////
        //////////// .docx in web view  ///////////////////
        ///////////////////////////////////////////////////


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Pdf pdf = (Pdf) parent.getItemAtPosition(position);

                Intent startMainScreen = new Intent(getApplicationContext(),view.class);
                startMainScreen.putExtra("name", pdf.getUrl());

                startActivity(startMainScreen);

            }
        });


        forward = (FloatingActionButton) findViewById(R.id.forward);

        forward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                sett = new AlertDialog.Builder(Lecportal.this);
                mview = getLayoutInflater().inflate(R.layout.uploadpdf,null);

                sposted = (FloatingActionButton) mview.findViewById(R.id.button3);
                s1 = (Spinner) mview.findViewById(R.id.editText2);
                s2 = (Spinner) mview.findViewById(R.id.watts2);
                //s3  = (Spinner) mview.findViewById(R.id.num2);
                edin  = (EditText) mview.findViewById(R.id.num33);
                naa  = (EditText) mview.findViewById(R.id.naa3);
                choose  = (Button) mview.findViewById(R.id.button2);
                scroll  = (TextView) mview.findViewById(R.id.textView7);
                textView8  = (TextView) mview.findViewById(R.id.textView8);

                textView8.setText(getIntent().getStringExtra("lname"));

                scroll.setSelected(true);
//
//                String [] acyearspin = {"SELECT ACADEMIC YEAR", "2000/2001", "2001/2002", "2002/2003", "2003/2004", "2004/2005",
//                        "2005/2006", "2006/2007", "2007/2008", "2008/2009", "2009/2010"
//                ,"2010/2011", "2011/2012", "2012/2013", "2013/2014", "2014/2015", "2015/2016"
//                , "2016/2017", "2017/2018", "2018/2019"};
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Leccourselist.this, android.R.layout.simple_spinner_dropdown_item, acyearspin);
//                s3.setAdapter(adapter);

//                String[] items1 = new String[]{"SELECT DEPARTMENT FIRST", "BSC Business Administration", "BSC Computer Engineering", "BSC Information Technology",
//                        "BSC Telecom Engineering", "MBA Strategic Management","BED Mathematics", "BED Religious studies", "BED Management", "BED Accounting",
//                        "BBA Human Resourse Management", "BBA Marketing", "MAB Banking and Finance", "MPHIL Curriculum and instruction", "MED Educational Administration and leadership",
//                        "PGD in Pastoral Ministry", "BBA Accounting", "BSC Agribusiness", "BSC Midwifery","BSC Mathematics and Statistics","BSC Nursing","BSC Computer Science", "BBA Accounting",
//                        "BED English Language"};
//                ArrayAdapter<String> adapters1 = new ArrayAdapter<>(Leccourselist.this, android.R.layout.simple_spinner_dropdown_item, items1);
//                s1.setAdapter(adapters1);

                String[] items1 = new String[]{textView5.getText().toString()};
                ArrayAdapter<String> adapters1 = new ArrayAdapter<>(Lecportal.this, android.R.layout.simple_spinner_dropdown_item, items1);
                s1.setAdapter(adapters1);


                s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                               long arg3) {

                        // TODO Auto-generated method stub
                        String sp1 = String.valueOf(s1.getSelectedItem());
                        //Toast.makeText(this, sp1, Toast.LENGTH_SHORT).show();
                        if (sp1.contentEquals("BSC Business Administration")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Computer Engineering")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Information Technology")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("Advanced System Analysis and Design");
                            list.add("Artificial Intelligence");
                            list.add("Data Communication and Networking");
                            list.add("Mobile Application and Development");
                            list.add("Distributed Systems");
                            list.add("Computer and Cyber Forencics");
                            list.add("Computer Security");
                            list.add("Programming with C++");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Telecom Engineering")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("MBA Strategic Management")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BBA Human Resourse Management")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BBA Marketing")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("MAB Banking and Finance")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("MED Educational Administration and leadership")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BBA Accounting")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Agribusiness")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }



                        if (sp1.contentEquals("BSC Mathematics and Statistics")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }



                        if (sp1.contentEquals("BSC Computer Science")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BED English Language")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT COURSE");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }


                        if (sp1.contentEquals("SELECT DEPARTMENT")) {
                            List<String> list = new ArrayList<String>();
                            list.add("");

                            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(Lecportal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter3.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter3);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });

                sett.setView(mview);
                final AlertDialog dialog = sett.create();

                choose.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.setType("application/pdf");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);

                    }

                });

                ///////////////////////////////////////////////////
                /////////// Button for pusing .pdf and .docx //////
                //////////// to server with volley  ///////////////
                ///////////////////////////////////////////////////

                sposted.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        //onActivityResult();
                        String str1 = s1.getSelectedItem().toString();
                        //onActivityResult();
                        if (str1.contentEquals("SELECT DEPARTMENT FIRST")) {

                            SweetAlertDialog su = new SweetAlertDialog(Lecportal.this, SweetAlertDialog.ERROR_TYPE);
                            su.setTitleText("Inappropriate Department selection");
                            su.show();
                            return;
                        }

                        String str2 = s2.getSelectedItem().toString();

                        if (str2.contentEquals("SELECT COURSE")) {

                            SweetAlertDialog su = new SweetAlertDialog(Lecportal.this, SweetAlertDialog.ERROR_TYPE);
                            su.setTitleText("Inappropriate Course selection");
                            su.show();
                            return;
                        }

                        if (filePath == null || filePath.equals("")) {

                            SweetAlertDialog su = new SweetAlertDialog(Lecportal.this, SweetAlertDialog.ERROR_TYPE);
                            su.setTitleText("No file chosen");
                            su.show();
                            return;
                        }

                        String name = edin.getText().toString().trim();
                        //String send1 = password.getText().toString();

                        if (TextUtils.isEmpty(name)) {
                            edin.setError("Field cant be Empty");
                            edin.requestFocus();
                            return;
                        }

                        String course = naa.getText().toString().trim();
                        //String send1 = password.getText().toString();

                        if (TextUtils.isEmpty(course)) {
                            naa.setError("Field cant be Empty");
                            naa.requestFocus();
                            return;
                        }


                        path = FilePath.getPath(Lecportal.this, filePath);

                        if (path == null) {

                            Toast.makeText(Lecportal.this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
                        }

//                        else {
//
//                        HashMap<String, String> postData = new HashMap<String, String>();
//
//                        postData.put("pdf", path); //Adding file
//                        postData.put("name", name); //Adding text parameter to the request
//                        postData.put("department", s1.getSelectedItem().toString()); //Adding text parameter to the request
//                        postData.put("program", s2.getSelectedItem().toString()); //Adding text parameter to the request
//                        postData.put("coursecode", naa.getText().toString()); //Adding text parameter to the request
//                        postData.put("lname", textView8.getText().toString()); //Adding text parameter to the request
//                        //postData.put("mobile","android");
//
//                        PostResponseAsyncTask task = new PostResponseAsyncTask(Leccourselist.this,postData, new AsyncResponse() {
//                            @Override
//                            public void processFinish(String str) {
//                                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
////                                if(str.contains("already")){
////
////                                    SweetAlertDialog su = new SweetAlertDialog(Leccourselist.this, SweetAlertDialog.ERROR_TYPE);
////                                    su.setTitleText("Account already exits");
////                                    su.show();
////                                    dialog.dismiss();
////
////                                }
//
//                            }
//                        });
//
//                        //task.execute("http://aroma.one957.com/upload.php");
//                        //task.execute("http://192.168.137.1:8012/client/upload.php");
//                        task.execute( "http://gtuc.one957.com/pdf.php");
//                        task.setEachExceptionsHandler(new EachExceptionsHandler() {
//                            @Override
//                            public void handleIOException(IOException e) {
//                                Toast.makeText(getApplicationContext(), "Cannot Connect to server  ", Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            public void handleMalformedURLException(MalformedURLException e) {
//                                Toast.makeText(getApplicationContext(), "URL Error ", Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            public void handleProtocolException(ProtocolException e) {
//                                Toast.makeText(getApplicationContext(), "Protocol Error ", Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
//                                Toast.makeText(getApplicationContext(), "Encoding Error ", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//
//                        }




                        else {
                            //Uploading code
                            try {
                                String uploadId = UUID.randomUUID().toString();

                                //Creating a multi part request
                                new MultipartUploadRequest(Lecportal.this, uploadId, UPLOAD_URL)
                                        .addFileToUpload(path, "pdf") //Adding file
                                        .addParameter("name", name) //Adding text parameter to the request
                                        .addParameter("department", s1.getSelectedItem().toString()) //Adding text parameter to the request
                                        .addParameter("program", s2.getSelectedItem().toString()) //Adding text parameter to the request
                                        .addParameter("coursecode", naa.getText().toString()) //Adding text parameter to the request
                                        //.addParameter("academicyear", s3.getSelectedItem().toString()) //Adding text parameter to the request
                                        .addParameter("lname", wtf.getText().toString()) //Adding text parameter to the request
                                        .setNotificationConfig(new UploadNotificationConfig())
                                        .setMaxRetries(2)
                                        .startUpload(); //Starting the upload


                                Toast.makeText(Lecportal.this,"Upload in progress", Toast.LENGTH_SHORT).show();

                            }

                            catch (Exception exc) {
                                Toast.makeText(Lecportal.this, exc.getMessage(), Toast.LENGTH_SHORT).show();

                            }


                        }


                    }
                });


//                sett.setView(mview);
//                AlertDialog dialog = sett.create();
                dialog.show();
            }
        });

        registerForContextMenu(listView);

        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(this);





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


//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        String text = newText;
//        adapter.getFilter().filter(newText);
//
//        return false;
//    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.setHeaderTitle("Tap to Delete Appliance");
//        menu.add(0, v.getId(), 0, "Delete");
//
//    }



//        public MyAdapter(work work, ArrayList<String> itemlist, ArrayList<String> itemlist1, ArrayList<String> itemlist2, ArrayList<String> itemlist3) {
//            super();
//        }

    ///////////////////////////////////////////////////
    /////////// Downloading and searching/// //////////
    //////////// through listview ////////////////////
    ///////////////////////////////////////////////////

    /////////////must be on////////////////////////////

    public void filter(String chko) {
        ArrayList<Pdf> temp = new ArrayList<>();
        for (Pdf d : pdfList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            String joint = d.getDepartment().toLowerCase();
            if (joint.contains(chko)) {
                temp.add(d);
            }

        }
        pdfAdapter.setFilter(temp);

    }

    /////////////must be on////////////////////////////

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    /////////////must be on////////////////////////////

    @Override
    public boolean onQueryTextChange(String s) {

        String st = s.toString();
        filter(st);


        return false;
    }

    /////////////must be on////////////////////////////

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /////////////must be on////////////////////////////


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Tap to download selected document");
        menu.add(0,v.getId(),0,"Download");

    }

    /////////////must be on////////////////////////////

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

    ///////////////////////////////////////////////////
    /////////// Code for Uploading .pdf and //////////
    //////////// .docx to server  ////////////////////
    ///////////////////////////////////////////////////

    //////////////handling the image chooser activity result

    /////////////must be on////////////////////////////

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

    /////////////can be off////////////////////////////

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

    /////////////must be on////////////////////////////

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

