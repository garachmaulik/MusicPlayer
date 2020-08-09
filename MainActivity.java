package com.mg.musicplayer;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

public class MainActivity extends AppCompatActivity {
   private static int timeout=1500;
  private CircularImageView m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m=(CircularImageView)findViewById(R.id.crc);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,Home.class);
                startActivity(i);
                finish();
            }
        },timeout);

        Animation myanim=AnimationUtils.loadAnimation(this,R.anim.mysanim);
        m.startAnimation(myanim);
    }


}
