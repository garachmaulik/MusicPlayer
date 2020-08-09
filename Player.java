package com.mg.musicplayer;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {

    Button btnnext,btnprevious,btnpause,btnshuffle,btnrepeat;

    TextView songtitle;

    SeekBar sb;

    static MediaPlayer mp;

    int pos;

    ArrayList<File> mysongs;

    Thread updateseekbar;

    String sname;

    private static int c=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnnext = (Button) findViewById(R.id.next);

        btnpause = (Button) findViewById(R.id.pause);

        btnprevious = (Button) findViewById(R.id.previous);


        songtitle = (TextView) findViewById(R.id.st);

        sb = (SeekBar) findViewById(R.id.seekbar);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateseekbar = new Thread() {

            @Override
            public void run() {

                int totalduration = mp.getDuration();
                int cpos = 0;
                while (cpos < totalduration) {
                    try {
                        sleep(500);
                        cpos = mp.getCurrentPosition();
                        sb.setProgress(cpos);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        if (mp != null) {
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();

        Bundle b = i.getExtras();

        mysongs = (ArrayList) b.getParcelableArrayList("songs");

        sname = mysongs.get(pos).getName().toString();

        final String songName = i.getStringExtra("songname");

        songtitle.setText(songName);

        songtitle.setSelected(true);

        pos = b.getInt("pos", 0);

        Uri u = Uri.parse(mysongs.get(pos).toString());

        mp = MediaPlayer.create(getApplicationContext(), u);

        mp.start();
        sb.setMax(mp.getDuration());

        updateseekbar.start();

        sb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.seek), PorterDuff.Mode.MULTIPLY);

        sb.getThumb().setColorFilter(getResources().getColor(R.color.thumb), PorterDuff.Mode.SRC_IN);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });

        btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.pause();
                pos = ((pos - 1) < 0) ? (mysongs.size() - 1) : (pos - 1);
                Uri u = Uri.parse(mysongs.get(pos).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                sname = mysongs.get(pos).getName().toString();
                songtitle.setText(sname);
                mp.start();
            }
        });

        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sb.setMax(mp.getDuration());

                if (mp.isPlaying()) {

                    btnpause.setBackgroundResource(R.drawable.ic_play);

                    mp.pause();
                } else {
                    btnpause.setBackgroundResource(R.drawable.ic_pause);
                    mp.start();
                }


            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.pause();
                pos = ((pos + 1) % mysongs.size());
                Uri u = Uri.parse(mysongs.get(pos).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                sname = mysongs.get(pos).getName().toString();
                songtitle.setText(sname);
                mp.start();

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}