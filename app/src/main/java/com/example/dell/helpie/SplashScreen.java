package com.example.dell.helpie;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.dell.helpie.Activities.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class SplashScreen extends AppCompatActivity {

    CircleImageView splashImage;
    Animation uptodown;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        splashImage = (CircleImageView)findViewById(R.id.splashImage);


        splashImage.setAnimation(uptodown);

        uptodown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();



            }

            /**
             * <p>Notifies the repetition of the animation.</p>
             *
             * @param animation The animation which was repeated.
             */
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}
