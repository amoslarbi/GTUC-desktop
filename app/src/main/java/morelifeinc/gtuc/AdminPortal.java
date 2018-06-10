package morelifeinc.gtuc;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminPortal extends AppCompatActivity {

    Button button;
    AlertDialog.Builder sett;
    EditText username, studentid, password, lstudentid, lpassword;
    View mview;
    FloatingActionButton sposted, forward;
    String sendlogin;

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

    }

    @Override
    public void onBackPressed() {

    }

}
