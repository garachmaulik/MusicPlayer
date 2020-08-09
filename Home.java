package com.mg.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Home extends AppCompatActivity {
    ListView lv;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lv=(ListView) findViewById(R.id.mylv);
        runtimepermission();
    }
    public void runtimepermission(){
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                display();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.cancelPermissionRequest();
            }
        }).check();
    }
    public ArrayList<File> findsong(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files= file.listFiles();
        for(File singleFile:files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findsong(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3")|| singleFile.getName().endsWith(".wav")){
                    arrayList.add(singleFile);
                }
            }
        }



        return arrayList;
    }


    void display(){
        final ArrayList<File> mysongs= findsong(Environment.getExternalStorageDirectory());
        Collections.sort(mysongs);
        items = new String[mysongs.size()];
        for (int i=0;i<mysongs.size();i++) {
            items[i] = mysongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }

        lv.setAdapter(new ArrayAdapter(this,R.layout.list,items));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String songName=lv.getItemAtPosition(i).toString();

                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("songs",mysongs)
                        .putExtra("songname",songName)
                        .putExtra("pos",i));


            }
        });

    }
}
