package com.example.lab4thltdd;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class PlayerInService extends Service implements View.OnClickListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    private WeakReference<ImageView> btnPlay;
    private WeakReference<ImageView> btnStop;
    private WeakReference<ImageView> btnLyrics;

    public static WeakReference<TextView> textViewSongTime;
    public static WeakReference<TextView> textViewLyrics;
    public static WeakReference<SeekBar> songProgressBar;
    static Handler progressBarHandler = new Handler();

    public static MediaPlayer mp;
    private boolean isPause = false;
    private boolean isShowingLyrics = false;
    private boolean isResetPlay =true;

    @Override
    public void onCreate() {
        mp = new MediaPlayer();
        mp.reset();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initUI();
        super.onStart(intent, startId);
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initUI() {

        btnPlay = new WeakReference<>(MainActivity.btnPlay);
        btnStop = new WeakReference<>(MainActivity.btnStop);
        btnLyrics = new WeakReference<>(MainActivity.mbtnLyrics);
        textViewSongTime = new WeakReference<>(MainActivity.textViewSongTime);
        textViewLyrics = new WeakReference<>(MainActivity.tvLyrics);
        songProgressBar = new WeakReference<>(MainActivity.seekBar);
        songProgressBar.get().setOnSeekBarChangeListener(this);

        btnPlay.get().setOnClickListener(this::onClick);
        btnStop.get().setOnClickListener(this::onClick);
        btnLyrics.get().setOnClickListener(this::onClick);
        mp.setOnCompletionListener(this::onCompletion);
        textViewLyrics.get().setVisibility(View.INVISIBLE);

    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        songProgressBar.get().setProgress(0);
        progressBarHandler.removeCallbacks(mUpdateTimeTask);
        btnPlay.get().setBackgroundResource(R.drawable.ic_play);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlay:
                if (isResetPlay) {
                    playSong();
                    isResetPlay=false;
                }
                if (mp.isPlaying()) {
                    mp.pause();
                    isPause = true;
                    progressBarHandler.removeCallbacks(mUpdateTimeTask);
                    btnPlay.get().setBackgroundResource(R.drawable.ic_play);
                }
                else{
                    mp.start();
                    isPause=false;
                    updateProgressBar();
                    btnPlay.get().setBackgroundResource(R.drawable.ic_pause);
                }
//                if (isPause) {
//                    mp.start();
//                    isPause = false;
//                    updateProgressBar();
//                    btnPlay.get().setBackgroundResource(R.drawable.ic_pause);
//                    return;
//                }

                break;
            case R.id.btnStop:
                mp.stop();
                onCompletion(mp);
                isResetPlay=true;
                textViewSongTime.get().setText("0.00/0.00");
                break;
            case R.id.btnLyrics:
                if (!isShowingLyrics) {

                    textViewLyrics.get().setVisibility(View.VISIBLE);
                    isShowingLyrics = true;


                } else {
                    textViewLyrics.get().setVisibility(View.INVISIBLE);
                    isShowingLyrics = false;

                }
                break;
        }
    }

    private void updateProgressBar() {
        try {
            progressBarHandler.postDelayed(mUpdateTimeTask, 100);
        } catch (Exception e) {

        }
    }

    static Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            long totalDuration = 0;
            long currentDuration = 0;
            try {
                totalDuration = mp.getDuration();
                currentDuration = mp.getCurrentPosition();
                textViewSongTime.get().setText(Utility.milliSecondsToTimer(currentDuration) + "/" + Utility.milliSecondsToTimer(totalDuration));
                int progress = (int) (Utility.getProgressPercentage(currentDuration, totalDuration));
                songProgressBar.get().setProgress(progress);
                progressBarHandler.postDelayed(this, 100);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        progressBarHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        progressBarHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = Utility.progressToTimer(seekBar.getProgress(), totalDuration);
        mp.seekTo(currentPosition);
        updateProgressBar();
    }


    public void playSong() {
        Utility.initNotification("Playing (Intentions).... ", this);
        try {
            mp.reset();
            Uri myUri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.intentions);
            mp.setDataSource(this, myUri);
            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    try {
                        mp.start();
                        updateProgressBar();
                        btnPlay.get().setBackgroundResource(R.drawable.ic_pause);
                    } catch (Exception e) {
                        Log.i("Exception", "" + e.getMessage());
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
