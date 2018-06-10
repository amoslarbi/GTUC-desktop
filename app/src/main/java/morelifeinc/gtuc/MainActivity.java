package morelifeinc.gtuc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;
import com.kosalgeek.asynctask.ExceptionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.R.id.text1;

public class MainActivity extends AppCompatActivity {

    CircularProgressButton hy, hy2;
    TextView textView2, textView3, textView4;
    AlertDialog.Builder sett;
    EditText username, studentid, password, lstudentid, lpassword;
    View mview;
    FloatingActionButton sposted;
    String sendlogin;

    public static final String hello = "hellol";
    public static final String qhello = "hellolol";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        lstudentid  = (EditText) findViewById(R.id.editText2);
        lpassword  = (EditText) findViewById(R.id.watts2);
        textView3  = (TextView) findViewById(R.id.textView3);
        textView4  = (TextView) findViewById(R.id.textView4);

        hy = (CircularProgressButton) findViewById(R.id.hy);
        hy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //hy.startAnimation();

//                Intent startMainScreen = new Intent(getApplicationContext(),uploadpdf.class);
//                startActivity(startMainScreen);

                sendlogin = lstudentid.getText().toString();
                String send1 = lpassword.getText().toString();

                if (TextUtils.isEmpty(sendlogin)) {
                    lstudentid.setError("Field cant be Empty");
                    lstudentid.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(send1)) {
                    lpassword.setError("Field cant be Empty");
                    lpassword.requestFocus();
                    return;
                }

                HashMap<String, String> postData = new HashMap<String, String>();

                postData.put("studentid", lstudentid.getText().toString());
                postData.put("password", lpassword.getText().toString());
                //postData.put("mobile","android");

                PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this,postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String str) {

                        if(str.contains("success")) {

//                            Intent startMainScreen = new Intent(getApplicationContext(),Studentportal.class);
//                            startActivity(startMainScreen);
//                            Toast.makeText(getApplicationContext(), "URL Error ", Toast.LENGTH_SHORT).show();

                                //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                                try {
                                    JSONArray jArray = new JSONArray(str);


                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject data = jArray.getJSONObject(i);
                                        Log.d("JSONResponse", String.valueOf(data));

                                        textView3.setText(data.getString("stdID"));
                                        textView4.setText(data.getString("Username"));

                                        String kiev = textView3.getText().toString();

                                        if(kiev.contains("gt") || kiev.contains("GT") || kiev.contains("TU") || kiev.contains("tu")) {
                                            //Toast.makeText(getApplicationContext(), textView3.getText().toString(), Toast.LENGTH_SHORT).show();
                                            Intent startMainScreen = new Intent(getApplicationContext(),Studentportal.class);

                                            startMainScreen.putExtra("id",textView3.getText().toString());
                                            startMainScreen.putExtra("name",textView4.getText().toString());

                                            SharedPreferences sh = getSharedPreferences(hello , 0);
                                            SharedPreferences.Editor edit = sh.edit();
                                            edit.putString("id",textView3.getText().toString());
                                            edit.putString("name",textView4.getText().toString());

                                            edit.commit();

                                            startActivity(startMainScreen);

                                        }

//                                        if(kiev.contains("GT")) {
//                                            //Toast.makeText(getApplicationContext(), textView3.getText().toString(), Toast.LENGTH_SHORT).show();
//                                            Intent startMainScreen = new Intent(getApplicationContext(),Studentportal.class);
//                                            startActivity(startMainScreen);
//
//                                        }

                                        if(kiev.contains("lec") || kiev.contains("LEC")) {
                                            //Toast.makeText(getApplicationContext(), textView3.getText().toString(), Toast.LENGTH_SHORT).show();
                                            Intent startMainScreen = new Intent(getApplicationContext(),Lecportal.class);

                                            startMainScreen.putExtra("id",textView3.getText().toString());
                                            startMainScreen.putExtra("name",textView4.getText().toString());

                                            SharedPreferences sh = getSharedPreferences(hello , 0);
                                            SharedPreferences.Editor edit = sh.edit();
                                            edit.putString("id",textView3.getText().toString());
                                            edit.putString("name",textView4.getText().toString());

                                            edit.commit();

                                            startActivity(startMainScreen);

                                        }

//                                        if(kiev.contains("LEC")) {
//                                            //Toast.makeText(getApplicationContext(), textView3.getText().toString(), Toast.LENGTH_SHORT).show();
//                                            Intent startMainScreen = new Intent(getApplicationContext(),Lecportal.class);
//                                            startActivity(startMainScreen);
//
//                                        }

                                        if(kiev.contains("Admin")) {
                                            //Toast.makeText(getApplicationContext(), "Create admin portal", Toast.LENGTH_SHORT).show();
                                            Intent startMainScreen = new Intent(getApplicationContext(),AdminPortal.class);
                                            startActivity(startMainScreen);

                                        }

                                    }

                                } catch (JSONException e) {

                                    e.printStackTrace();
                                }




//                            SweetAlertDialog su = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
//                            su.setTitleText("Account created");
//                            su.show();

                        }

                        if(str.contains("Failed")){

                            SweetAlertDialog su = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                            su.setTitleText("Invalid Credentials");
                            su.show();

                        }

                    }
                });

                //task.execute("http://aroma.one957.com/upload.php");
                //task.execute("http://192.168.137.1:8012/client/upload.php");
                task.execute( "http://gtuc.one957.com/login.php");
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



        textView2  = (TextView) findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sett = new AlertDialog.Builder(MainActivity.this);
                mview = getLayoutInflater().inflate(R.layout.signup,null);

                sposted = (FloatingActionButton) mview.findViewById(R.id.button3);
                username  = (EditText) mview.findViewById(R.id.editText);
                studentid  = (EditText) mview.findViewById(R.id.watts);
                password  = (EditText) mview.findViewById(R.id.num);
                sett.setView(mview);
                final AlertDialog dialog = sett.create();
                sposted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String str3 = username.getText().toString();
                        String send = studentid.getText().toString().trim();
                        String send1 = password.getText().toString();

                        if (TextUtils.isEmpty(str3)) {
                            username.setError("Field cant be Empty");
                            username.requestFocus();
                            return;
                        }

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

                        HashMap<String, String> postData = new HashMap<String, String>();

                        postData.put("username", username.getText().toString());
                        postData.put("studentid", studentid.getText().toString());
                        postData.put("password", password.getText().toString());
                        //postData.put("mobile","android");

                        PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this,postData, new AsyncResponse() {
                            @Override
                            public void processFinish(String str) {


                                if(str.contains("success")) {

                                    SweetAlertDialog su = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                    su.setTitleText("Account created");
                                    su.show();
                                    dialog.dismiss();

                                }

                                if(str.contains("already")){

                                    SweetAlertDialog su = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                                    su.setTitleText("Account already exits");
                                    su.show();
                                    dialog.dismiss();

                                }

                                if(str.contains("Failed")){

                                    SweetAlertDialog su = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                                    su.setTitleText("Exception handler failed");
                                    su.show();
                                    dialog.dismiss();

                                }

                            }
                        });

                        //task.execute("http://aroma.one957.com/upload.php");
                        //task.execute("http://192.168.137.1:8012/client/upload.php");
                        task.execute( "http://192.168.43.63/gtuc/upload.php");
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


        }
