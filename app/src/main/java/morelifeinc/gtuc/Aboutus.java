package morelifeinc.gtuc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;


public class Aboutus extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);


        pdfView = (PDFView)findViewById(R.id.pdfView);

        pdfView.fromAsset("amberaboutus.pdf").load();

    }

}