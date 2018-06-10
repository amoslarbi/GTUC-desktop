package morelifeinc.gtuc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import javax.security.auth.callback.Callback;


public class view extends AppCompatActivity {

    PDFView pdfView;
    TextView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);


       // pdfView = (PDFView)findViewById(R.id.pdfView);
        vv = (TextView)findViewById(R.id.textView9);
        vv.setText(getIntent().getStringExtra("name"));
        //vv.setText("https://www.tutorialspoint.com/cplusplus/cpp_tutorial.pdf");

        //String doc1="<iframe src='http://docs.google.com/viewer?url=https://www.tutorialspoint.com/cplusplus/cpp_tutorial.pdf&embedded=true' width='100%' height='100%'  style='border: none;'></iframe>";

        //String doc="<iframe src='" + vv.getText().toString() + "&embedded=true' width='100%' height='100%'  style='border: none;'></iframe>";
        String doc="<iframe src='http://docs.google.com/viewer?url=" + vv.getText().toString() + "&embedded=true' width='100%' height='100%'  style='border: none;'></iframe>";
        Toast.makeText(getApplicationContext(), String.valueOf(doc), Toast.LENGTH_LONG).show();


        WebView  wv = (WebView)findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
        //wv.setWebViewClient(new Callback());
        wv.loadData(doc, "text/html", "UTF-8");


    }

}