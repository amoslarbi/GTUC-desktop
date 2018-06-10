package morelifeinc.gtuc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    Animation translate;
    ImageView lolo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Regular.ttf");
        setContentView(R.layout.splash);

        lolo = (ImageView)findViewById(R.id.logo);
        translate = AnimationUtils.loadAnimation(this, R.anim.translate);

        Thread myThread = new Thread(){

            @Override
            public void run(){

                try {
                    lolo.setAnimation(translate);

                    sleep(3000);
                    Intent startMainScreen = new Intent(getApplicationContext(),WelcomeActivity.class);
                    startActivity(startMainScreen);
                    finish();
                }
                catch (InterruptedException e){
                    e.printStackTrace();

                }

            }

        };
        myThread.start();


    }
}
