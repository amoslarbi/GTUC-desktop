package morelifeinc.gtuc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class Userguide extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userguide);

        ImageView imageViewmor = (ImageView) findViewById(R.id.image3View3);
        imageViewmor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder sett = new AlertDialog.Builder(Userguide.this);
                View mview = getLayoutInflater().inflate(R.layout.userguideimg1,null);

                ImageView userguideimg1 = (ImageView) mview.findViewById(R.id.userguideimg1);

                sett.setView(mview);
                final AlertDialog dialog = sett.create();

                userguideimg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });
                dialog.show();

            }
        });


        ImageView imageViewmor2 = (ImageView) findViewById(R.id.imageView32);
        imageViewmor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder sett = new AlertDialog.Builder(Userguide.this);
                View mview = getLayoutInflater().inflate(R.layout.userguideimg2,null);

                ImageView userguideimg2 = (ImageView) mview.findViewById(R.id.userguideimg2);

                sett.setView(mview);
                final AlertDialog dialog = sett.create();

                userguideimg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });
                dialog.show();

            }
        });



        ImageView imageViewmor3 = (ImageView) findViewById(R.id.imageViewmor);
        imageViewmor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder sett = new AlertDialog.Builder(Userguide.this);
                View mview = getLayoutInflater().inflate(R.layout.userguideimg3,null);

                ImageView userguideimg3 = (ImageView) mview.findViewById(R.id.userguideimg3);

                sett.setView(mview);
                final AlertDialog dialog = sett.create();

                userguideimg3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });
                dialog.show();

            }
        });


        ImageView imageViewmor4 = (ImageView) findViewById(R.id.imageViewmor33);
        imageViewmor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder sett = new AlertDialog.Builder(Userguide.this);
                View mview = getLayoutInflater().inflate(R.layout.userguideimg4,null);

                ImageView userguideimg4 = (ImageView) mview.findViewById(R.id.userguideimg4);

                sett.setView(mview);
                final AlertDialog dialog = sett.create();

                userguideimg4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });
                dialog.show();

            }
        });



    }

}