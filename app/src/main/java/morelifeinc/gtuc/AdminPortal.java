package morelifeinc.gtuc;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminPortal extends AppCompatActivity {

    Button button, button2;
    AlertDialog.Builder sett;
    EditText username, studentid, password, lastname1, email, lecusername, leclastname1, lecemail, lecid, lecpassword;
    View mview;
    FloatingActionButton sposted, forward;
    String sendlogin;
    Spinner s1,s2, ss2, ss3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_portal);

        button = (Button) findViewById(R.id.button);
        forward = (FloatingActionButton) findViewById(R.id.forward);

        forward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent startMainScreen = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(startMainScreen);

            }

        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        sett = new AlertDialog.Builder(AdminPortal.this);
                        mview = getLayoutInflater().inflate(R.layout.lecsignup,null);

                        sposted = (FloatingActionButton) mview.findViewById(R.id.button3);

                        lecusername  = (EditText) mview.findViewById(R.id.leceditText);
                        leclastname1  = (EditText) mview.findViewById(R.id.leclastname1);
                        lecemail  = (EditText) mview.findViewById(R.id.lecemail);
                        lecid  = (EditText) mview.findViewById(R.id.lecwatts);
                        lecpassword  = (EditText) mview.findViewById(R.id.lecnum);
                        s1 = (Spinner) mview.findViewById(R.id.lecfaculty);
                        s2 = (Spinner) mview.findViewById(R.id.lecprogram);


                String[] items1 = new String[]{"SELECT FACULTY FIRST", "Engineering", "IT Business", "Computing and Information Systems",};
                ArrayAdapter<String> adapters1 = new ArrayAdapter<>(AdminPortal.this, android.R.layout.simple_spinner_dropdown_item, items1);
                s1.setAdapter(adapters1);

                s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                               long arg3) {

                        // TODO Auto-generated method stub
                        String sp1 = String.valueOf(s1.getSelectedItem());
                        //Toast.makeText(this, sp1, Toast.LENGTH_SHORT).show();
                        if (sp1.contentEquals("Engineering")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("Bsc Computer Engineering");
                            list.add("Bsc Telecommunication Engineering");
                            list.add("Diploma in Telecommunication Engineering");
                            list.add("Bsc Science in Solar Engineering");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AdminPortal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("IT Business")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("Bsc Procurement and Logistics");
                            list.add("Banking and Finance");
                            list.add("Bsc Accounting");
                            list.add("Bsc Economics");
                            list.add("Bsc Human Resource Management");
                            list.add("Bsc Management");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AdminPortal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                        if (sp1.contentEquals("Computing and Information Systems")) {
                            List<String> list = new ArrayList<String>();
                            list.add("SELECT PROGRAM");
                            list.add("Bsc Information Technology");
                            list.add("Diploma in Information Technology");
                            list.add("Bsc Mobile Internet Communication");

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AdminPortal.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dataAdapter.notifyDataSetChanged();
                            s2.setAdapter(dataAdapter);

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }

                });

                        sett.setView(mview);
                        final AlertDialog dialog = sett.create();
                        sposted.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String str3 = lecusername.getText().toString();
                                String lname = leclastname1.getText().toString();
                                String emai = lecemail.getText().toString();
                                String send = lecid.getText().toString().trim();
                                String send1 = lecpassword.getText().toString();

                                if (TextUtils.isEmpty(str3)) {
                                    lecusername.setError("Field cant be Empty");
                                    lecusername.requestFocus();
                                    return;
                                }

                                if (TextUtils.isEmpty(lname)) {
                                    leclastname1.setError("Field cant be Empty");
                                    leclastname1.requestFocus();
                                    return;
                                }

                                if (TextUtils.isEmpty(emai)) {
                                    lecemail.setError("Field cant be Empty");
                                    lecemail.requestFocus();
                                    return;
                                }

                                String str1 = s1.getSelectedItem().toString();
                                //onActivityResult();
                                if (str1.contentEquals("SELECT FACULTY FIRST")) {

                                    SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
                                    su.setTitleText("Inappropriate FACULTY selection");
                                    su.show();
                                    return;
                                }

                                String str2 = s2.getSelectedItem().toString();
                                //onActivityResult();
                                if (str2.contentEquals("SELECT PROGRAM")) {

                                    SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
                                    su.setTitleText("Inappropriate PROGRAM selection");
                                    su.show();
                                    return;
                                }

                                if (TextUtils.isEmpty(send)) {
                                    lecid.setError("Field cant be Empty");
                                    lecid.requestFocus();
                                    return;
                                }

                                if (TextUtils.isEmpty(send1)) {
                                    lecpassword.setError("Field cant be Empty");
                                    lecpassword.requestFocus();
                                    return;
                                }

                                if (!(send.contains("TU") || send.contains("tu"))) {
                                    SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
                                    su.setTitleText("Invalid Lecturer ID");
                                    su.show();
                                    return;
                                }

                                if (!(send.contains("-"))) {
                                    SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
                                    su.setTitleText("Invalid Lecturer ID");
                                    su.show();
                                    return;
                                }

//                                String str2 = s1.getSelectedItem().toString();
//                                if (str2.contentEquals("SELECT DEPARTMENT")) {
//
//                                    SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
//                                    su.setTitleText("Inappropriate Department");
//                                    su.show();
//                                    return;
//
//                                }

                                HashMap<String, String> postData = new HashMap<String, String>();

                                postData.put("username", lecusername.getText().toString());
                                postData.put("lastname", leclastname1.getText().toString());
                                postData.put("email", lecemail.getText().toString());
                                postData.put("faculty", s1.getSelectedItem().toString());
                                 postData.put("program", s2.getSelectedItem().toString());
                                postData.put("studentid", lecid.getText().toString());
                                postData.put("password", lecpassword.getText().toString());
                                //postData.put("lecdepa", s1.getSelectedItem().toString());
                                //postData.put("mobile","android");

                                PostResponseAsyncTask task = new PostResponseAsyncTask(AdminPortal.this,postData, new AsyncResponse() {
                                    @Override
                                    public void processFinish(String str) {
                                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();


                                        if(str.contains("success")) {

                                            SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.SUCCESS_TYPE);
                                            su.setTitleText("Account created");
                                            su.show();
                                            dialog.dismiss();

                                        }

                                        if(str.contains("already")){

                                            SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
                                            su.setTitleText("Account already exits");
                                            su.show();
                                            dialog.dismiss();

                                        }

                                        if(str.contains("Failed")){

                                            SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
                                            su.setTitleText("Exception handler failed");
                                            su.show();
                                            dialog.dismiss();

                                        }

                                    }
                                });

                                //task.execute("http://aroma.one957.com/upload.php");
                                //task.execute("http://192.168.137.1:8012/client/upload.php");
                                task.execute( "http://gtuc.one957.com/upload.php");
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




                            }
                        });

//                sett.setView(mview);
//                AlertDialog dialog = sett.create();
                        dialog.show();


            }
        });


        button2  = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sett = new AlertDialog.Builder(AdminPortal.this);
                mview = getLayoutInflater().inflate(R.layout.signup,null);

                sposted = (FloatingActionButton) mview.findViewById(R.id.button3);

                username  = (EditText) mview.findViewById(R.id.editText);
                lastname1  = (EditText) mview.findViewById(R.id.lastname1);
                email  = (EditText) mview.findViewById(R.id.email);
                studentid  = (EditText) mview.findViewById(R.id.watts);
                password  = (EditText) mview.findViewById(R.id.num);
                ss2 = (Spinner) mview.findViewById(R.id.faculty);
               // ss3 = (Spinner) mview.findViewById(R.id.program);


                String[] items1 = new String[]{"SELECT FACULTY FIRST", "Engineering", "IT Business", "Computing and Information Systems",};
                ArrayAdapter<String> adapters1 = new ArrayAdapter<>(AdminPortal.this, android.R.layout.simple_spinner_dropdown_item, items1);
                ss2.setAdapter(adapters1);

                ss2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                               long arg3) {
//
//                        // TODO Auto-generated method stub
//                        String sp1 = String.valueOf(ss2.getSelectedItem());
//                        //Toast.makeText(this, sp1, Toast.LENGTH_SHORT).show();
//                        if (sp1.contentEquals("BSC Business Administration")) {
//                            List<String> list = new ArrayList<String>();
//                            list.add("SELECT COURSE");
//                            list.add("ONE");
//                            list.add("TWO");
//                            list.add("THREE");
//                            list.add("FOUR");
//                            list.add("FIVE");
//                            list.add("SIX");
//                            list.add("SEVEN");
//                            list.add("EIGHT");
//
//                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AdminPortal.this,
//                                    android.R.layout.simple_spinner_item, list);
//                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            dataAdapter.notifyDataSetChanged();
//                            ss3.setAdapter(dataAdapter);
//
//                        }
//
//                        if (sp1.contentEquals("BSC Computer Engineering")) {
//                            List<String> list = new ArrayList<String>();
//                            list.add("SELECT COURSE");
//                            list.add("ONE");
//                            list.add("TWO");
//                            list.add("THREE");
//                            list.add("FOUR");
//                            list.add("FIVE");
//                            list.add("SIX");
//                            list.add("SEVEN");
//                            list.add("EIGHT");
//
//                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AdminPortal.this,
//                                    android.R.layout.simple_spinner_item, list);
//                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            dataAdapter.notifyDataSetChanged();
//                            ss3.setAdapter(dataAdapter);
//
//                        }
//
//                        if (sp1.contentEquals("BSC Computer Engineering")) {
//                            List<String> list = new ArrayList<String>();
//                            list.add("SELECT COURSE");
//                            list.add("ONE");
//                            list.add("TWO");
//                            list.add("THREE");
//                            list.add("FOUR");
//                            list.add("FIVE");
//                            list.add("SIX");
//                            list.add("SEVEN");
//                            list.add("EIGHT");
//
//                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AdminPortal.this,
//                                    android.R.layout.simple_spinner_item, list);
//                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            dataAdapter.notifyDataSetChanged();
//                            ss3.setAdapter(dataAdapter);
//
//                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }

                });

                sett.setView(mview);
                final AlertDialog dialog = sett.create();
                sposted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String str3 = username.getText().toString();
                        String lname = lastname1.getText().toString();
                        String emaill = email.getText().toString();
                        String send = studentid.getText().toString().trim();
                        String send1 = password.getText().toString();

                        if (TextUtils.isEmpty(str3)) {
                            username.setError("Field cant be Empty");
                            username.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(lname)) {
                            lastname1.setError("Field cant be Empty");
                            lastname1.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(emaill)) {
                            email.setError("Field cant be Empty");
                            email.requestFocus();
                            return;
                        }

                        String str1 = ss2.getSelectedItem().toString();
                        //onActivityResult();
                        if (str1.contentEquals("SELECT FACULTY FIRST")) {

                            SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
                            su.setTitleText("Inappropriate FACULTY selection");
                            su.show();
                            return;
                        }

//                        String str2 = s1.getSelectedItem().toString();
//                        //onActivityResult();
//                        if (str2.contentEquals("SELECT PROGRAM FIRST")) {
//
//                            SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
//                            su.setTitleText("Inappropriate PROGRAM selection");
//                            su.show();
//                            return;
//                        }


                        if (TextUtils.isEmpty(send)) {
                            studentid.setError("Field cant be Empty");
                            studentid.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(send1)) {
                            password.setError("Field cant be Empty");
                            password.requestFocus();
                            return;
                        }

                        if (!(send.contains("TU") || send.contains("tu"))) {
                            SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
                            su.setTitleText("Invalid Student ID");
                            su.show();
                            return;
                        }

                        HashMap<String, String> postData = new HashMap<String, String>();

                        postData.put("username", username.getText().toString());
                        postData.put("lastname", lastname1.getText().toString());
                        postData.put("email", email.getText().toString());
                        postData.put("faculty", ss2.getSelectedItem().toString());
                       // postData.put("program", ss3.getSelectedItem().toString());
                        postData.put("studentid", studentid.getText().toString());
                        postData.put("password", password.getText().toString());
                        //postData.put("mobile","android");

                        PostResponseAsyncTask task = new PostResponseAsyncTask(AdminPortal.this,postData, new AsyncResponse() {
                            @Override
                            public void processFinish(String str) {


                                if(str.contains("success")) {

                                    SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.SUCCESS_TYPE);
                                    su.setTitleText("Account created");
                                    su.show();
                                    dialog.dismiss();

                                }

                                if(str.contains("already")){

                                    SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
                                    su.setTitleText("Account already exits");
                                    su.show();

                                }

                                if(str.contains("Failed")){

                                    SweetAlertDialog su = new SweetAlertDialog(AdminPortal.this, SweetAlertDialog.ERROR_TYPE);
                                    su.setTitleText("Server down contact Admin");
                                    su.show();
                                    dialog.dismiss();

                                }

                            }
                        });

                        task.execute("http://gtuc.one957.com/upload.php");
                        //task.execute("http://192.168.137.1:8012/client/upload.php");
                        //task.execute( "http://192.168.43.63/gtuc/upload.php");
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




                    }
                });

//                sett.setView(mview);
//                AlertDialog dialog = sett.create();
                dialog.show();
            }
        });


    }

    @Override
    public void onBackPressed() {

    }

}

//do for validating textview, edittext, spinner, checkbox
//do for search...the whatsapp search
