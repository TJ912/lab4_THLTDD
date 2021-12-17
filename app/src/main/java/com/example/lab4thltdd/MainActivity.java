package com.example.lab4thltdd;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static ImageView btnPlay,btnStop,mbtnLyrics;
    public static TextView textViewSongTime,tvLyrics;
    private Intent playerService;
    public static SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        playerService=new Intent(MainActivity.this,PlayerInService.class);
        startService(playerService);
    }
    public void initView(){
        btnPlay= findViewById(R.id.btnPlay);
        btnStop=findViewById(R.id.btnStop);
        mbtnLyrics=findViewById(R.id.btnLyrics);
        seekBar=findViewById(R.id.seekbar);
        tvLyrics=findViewById(R.id.tvLyrics);
        textViewSongTime=findViewById(R.id.textViewSongTime);
        btnPlay.setBackgroundResource(R.drawable.ic_play);



    }
    @Override
    protected void onDestroy(){
        if(!PlayerInService.mp.isPlaying()){
            PlayerInService.mp.stop();
            stopService(playerService);
        }
        else{
            btnPlay.setBackgroundResource(R.drawable.ic_pause);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        try{
            if(!PlayerInService.mp.isPlaying()) {
                   btnPlay.setBackgroundResource(R.drawable.ic_play);

            }else{
              btnPlay.setBackgroundResource(R.drawable.ic_pause);

            }

        }  catch(Exception e){
            Log.e("Exception",""+e.getMessage()+e.getStackTrace()+e.getCause());
        }

        super.onResume();
    }
}