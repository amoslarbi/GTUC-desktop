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

public class Leccourselist extends AppCompatActivity implements SearchView.OnQueryTextListener {

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

    //pdf adapter

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
    //public static final String UPLOAD_URL = "http://gtuc.one957.com/pdf.php";
    public static final String PDF_FETCH_URL = "http://192.168.43.234/pdf/getPdfs.php";


    //Uri to store the image uri
    private Uri filePath;
    SwipeRefreshLayout swiperefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leccourselist);

        //Requesting storage permission
        requestStoragePermission();
        listView = (ListView) findViewById(R.id.listview);
        swiperefresh=(SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setText(getIntent().getStringExtra("food"));
        //Toast.makeText(getApplicationContext(), textView5.getText().toString(), Toast.LENGTH_SHORT).show();
        HashMap<String, String> postData = new HashMap<String, String>();

        postData.put("depa", textView5.getText().toString());
        //postData.put("mobile","android");

        PostResponseAsyncTask task = new PostResponseAsyncTask(Leccourselist.this,postData, new AsyncResponse() {
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

                            pdfAdapter=new PdfAdapter(Leccourselist.this,R.layout.depalist, pdfList);
                            listView.setAdapter(pdfAdapter);
                            pdfAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                }
                if(str.contains("Failed")){

                    SweetAlertDialog su = new SweetAlertDialog(Leccourselist.this, SweetAlertDialog.ERROR_TYPE);
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

                PostResponseAsyncTask task = new PostResponseAsyncTask(Leccourselist.this,postData, new AsyncResponse() {
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

                                pdfAdapter=new PdfAdapter(Leccourselist.this,R.layout.depalist, pdfList);
                                listView.setAdapter(pdfAdapter);
                                pdfAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        if(str.contains("Failed")){

                            SweetAlertDialog su = new SweetAlertDialog(Leccourselist.this, SweetAlertDialog.ERROR_TYPE);
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

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                // TODO Auto-generated method stub
//
//                Pdf pdf = (Pdf) parent.getItemAtPosition(position);
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse(pdf.getUrl()));
//                startActivity(intent);
//
//
//                return true;
//            }
//        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lecturer Portal");

        textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setText(getIntent().getStringExtra("food"));

        forward = (FloatingActionButton) findViewById(R.id.forward);

        forward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                sett = new AlertDialog.Builder(Leccourselist.this);
                mview = getLayoutInflater().inflate(R.layout.uploadpdf,null);

                sposted = (FloatingActionButton) mview.findViewById(R.id.button3);
                s1 = (Spinner) mview.findViewById(R.id.editText2);
                s2 = (Spinner) mview.findViewById(R.id.watts2);
                s3  = (Spinner) mview.findViewById(R.id.num2);
                edin  = (EditText) mview.findViewById(R.id.num33);
                choose  = (Button) mview.findViewById(R.id.button2);
                scroll  = (TextView) mview.findViewById(R.id.textView7);
                textView8  = (TextView) mview.findViewById(R.id.textView8);

                textView8.setText(getIntent().getStringExtra("lname"));

                scroll.setSelected(true);

                String [] acyearspin = {"SELECT ACADEMIC YEAR", "2000/2001", "2001/2002", "2002/2003", "2003/2004", "2004/2005",
                        "2005/2006", "2006/2007", "2007/2008", "2008/2009", "2009/2010"
                ,"2010/2011", "2011/2012", "2012/2013", "2013/2014", "2014/2015", "2015/2016"
                , "2016/2017", "2017/2018", "2018/2019"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Leccourselist.this, android.R.layout.simple_spinner_dropdown_item, acyearspin);
                s3.setAdapter(adapter);

//                String[] items1 = new String[]{"SELECT DEPARTMENT FIRST", "BSC Business Administration", "BSC Computer Engineering", "BSC Information Technology",
//                        "BSC Telecom Engineering", "MBA Strategic Management","BED Mathematics", "BED Religious studies", "BED Management", "BED Accounting",
//                        "BBA Human Resourse Management", "BBA Marketing", "MAB Banking and Finance", "MPHIL Curriculum and instruction", "MED Educational Administration and leadership",
//                        "PGD in Pastoral Ministry", "BBA Accounting", "BSC Agribusiness", "BSC Midwifery","BSC Mathematics and Statistics","BSC Nursing","BSC Computer Science", "BBA Accounting",
//                        "BED English Language"};
//                ArrayAdapter<String> adapters1 = new ArrayAdapter<>(Leccourselist.this, android.R.layout.simple_spinner_dropdown_item, items1);
//                s1.setAdapter(adapters1);

                String[] items1 = new String[]{"SELECT DEPARTMENT FIRST", "BSC Business Administration", "BSC Computer Engineering", "BSC Information Technology",
                        "BSC Telecom Engineering", "MBA Strategic Management",
                "BBA Human Resourse Management", "BBA Marketing", "MAB Banking and Finance", "MED Educational Administration and leadership"
                , "BBA Accounting", "BSC Agribusiness", "BSC Midwifery","BSC Mathematics and Statistics","BSC Nursing","BSC Computer Science",
                "BED English Language"};
                ArrayAdapter<String> adapters1 = new ArrayAdapter<>(Leccourselist.this, android.R.layout.simple_spinner_dropdown_item, items1);
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
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Computer Engineering")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Information Technology")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Telecom Engineering")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("MBA Strategic Management")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BBA Human Resourse Management")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BBA Marketing")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("MAB Banking and Finance")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("MED Educational Administration and leadership")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BBA Accounting")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Agribusiness")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Midwifery")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Mathematics and Statistics")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Nursing")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BSC Computer Science")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("BED English Language")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("ONE");
                            list.add("TWO");
                            list.add("THREE");
                            list.add("FOUR");
                            list.add("FIVE");
                            list.add("SIX");
                            list.add("SEVEN");
                            list.add("EIGHT");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Leccourselist.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }


                        if (sp1.contentEquals("SELECT DEPARTMENT")) {
                            List<String> list = new ArrayList<String>();
                            list.add("");

                            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(Leccourselist.this,
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

                sposted.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        //onActivityResult();
                        String str1 = s1.getSelectedItem().toString();
                        //onActivityResult();
                        if (str1.contentEquals("SELECT DEPARTMENT")) {

                            SweetAlertDialog su = new SweetAlertDialog(Leccourselist.this, SweetAlertDialog.ERROR_TYPE);
                            su.setTitleText("Inappropriate Department selection");
                            su.show();
                            return;
                        }

                        String str2 = s2.getSelectedItem().toString();

                        if (str2.contentEquals("SELECT PROGRAM")) {

                            SweetAlertDialog su = new SweetAlertDialog(Leccourselist.this, SweetAlertDialog.ERROR_TYPE);
                            su.setTitleText("Inappropriate Program selection");
                            su.show();
                            return;
                        }

                        if (filePath == null || filePath.equals("")) {

                            SweetAlertDialog su = new SweetAlertDialog(Leccourselist.this, SweetAlertDialog.ERROR_TYPE);
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


                        path = FilePath.getPath(Leccourselist.this, filePath);

                        if (path == null) {

                            Toast.makeText(Leccourselist.this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
                        }

                        else {
                            //Uploading code
                            try {
                                String uploadId = UUID.randomUUID().toString();

                                //Creating a multi part request
                                new MultipartUploadRequest(Leccourselist.this, uploadId, UPLOAD_URL)
                                        .addFileToUpload(path, "pdf") //Adding file
                                        .addParameter("name", name) //Adding text parameter to the request
                                        .addParameter("department", s1.getSelectedItem().toString()) //Adding text parameter to the request
                                        .addParameter("program", s2.getSelectedItem().toString()) //Adding text parameter to the request
                                        .addParameter("academicyear", s3.getSelectedItem().toString()) //Adding text parameter to the request
                                        .addParameter("lname", textView8.getText().toString()) //Adding text parameter to the request
                                        .setNotificationConfig(new UploadNotificationConfig())
                                        .setMaxRetries(2)
                                        .startUpload(); //Starting the upload



                            } catch (Exception exc) {
                                Toast.makeText(Leccourselist.this, exc.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }


                    }
                });


//                sett.setView(mview);
//                AlertDialog dialog = sett.create();
                dialog.show();
            }
        });



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
