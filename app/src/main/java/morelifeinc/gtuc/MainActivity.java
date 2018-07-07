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
    TextView textView2, textView3, textView4, textView5, scroll;
    AlertDialog.Builder sett;
    EditText username, studentid, password, lstudentid, lpassword;
    View mview;
    FloatingActionButton sposted;
    String sendlogin;
    private Session session;//global variable
    private FloatingActionButton nLogOutBtn;

    public static final String hello = "hellol";
    public static final String qhello = "hellolol";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //session = new Session(cntx); //in oncreate
//        session.setusename("USERNAME");

        lstudentid  = (EditText) findViewById(R.id.editText2);
        lpassword  = (EditText) findViewById(R.id.watts2);
        textView3  = (TextView) findViewById(R.id.textView3);
        textView4  = (TextView) findViewById(R.id.textView4);
        textView5  = (TextView) findViewById(R.id.textView5);
        scroll  = (TextView) findViewById(R.id.textView7);

        scroll.setSelected(true);

        nLogOutBtn = (FloatingActionButton) findViewById(R.id.LogOutBtn);

        nLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startMainScreen = new Intent(getApplicationContext(),Userguide.class);
                startActivity(startMainScreen);

            }
        });


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

                                try {
                                    JSONArray jArray = new JSONArray(str);


                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject data = jArray.getJSONObject(i);
                                        Log.d("JSONResponse", String.valueOf(data));

                                        textView3.setText(data.getString("stdID"));
                                        textView4.setText(data.getString("Username"));
                                        textView5.setText(data.getString("Lecdepa"));

                                        String kiev = textView3.getText().toString();

                                        if(!(kiev.contains("-") || kiev.contains("-"))) {
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

//

                                        if(kiev.contains("-") || kiev.contains("-")) {
                                            //Toast.makeText(getApplicationContext(), textView3.getText().toString(), Toast.LENGTH_SHORT).show();
                                            Intent startMainScreen = new Intent(getApplicationContext(),Lecportal.class);

                                            startMainScreen.putExtra("id",textView3.getText().toString());
                                            startMainScreen.putExtra("name",textView4.getText().toString());
                                            startMainScreen.putExtra("ldepa",textView5.getText().toString());

                                            SharedPreferences sh = getSharedPreferences(hello , 0);
                                            SharedPreferences.Editor edit = sh.edit();
                                            edit.putString("id",textView3.getText().toString());
                                            edit.putString("name",textView4.getText().toString());
                                            edit.putString("ldepa",textView5.getText().toString());

                                            edit.commit();

                                            startActivity(startMainScreen);

                                        }

                                        if(kiev.contains("Admin") || kiev.contains("admin")) {
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

            }


        }
