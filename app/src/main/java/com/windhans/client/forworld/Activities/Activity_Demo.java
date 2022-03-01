package com.windhans.client.forworld.Activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.VideoView;


import com.windhans.client.forworld.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Activity_Demo extends AppCompatActivity {
    private SeekBar seekBar;
    private Handler threadHandler = new Handler();
    String urlPath;
    Uri uri;
    private MediaPlayer mediaPlayer;
    VideoView videoView;
    MediaController mediacontroller;
    ProgressBar simpleProgressBar;
    int progress = 0;
    private int current_position;
    int duration_final;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__demo);

        this.seekBar= (SeekBar) this.findViewById(R.id.seekbar);
        this.seekBar.setClickable(false);
        simpleProgressBar=(ProgressBar)findViewById(R.id.simpleProgressBar);

        urlPath ="http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
        uri = Uri.parse(urlPath);
        videoView = (VideoView)findViewById(R.id.exerciseVideo);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(Activity_Demo.this,uri);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);
        current_position=videoView.getCurrentPosition();
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
        String  durartion = String.valueOf(mediaPlayer.getDuration());
        Log.d("position", "onCreate: "+current_position);
        String[] duration1=durartion.split("-");
        duration_final = Integer.parseInt(duration1[1]);
        String time = String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(duration_final),
                TimeUnit.MILLISECONDS.toSeconds(duration_final) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration_final))
        );
        progress=duration_final;

        simpleProgressBar.setMax(progress); // 100 maximum value for the progress value
        simpleProgressBar.setProgress(0);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(Activity_Demo.this,uri);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int duration = this.mediaPlayer.getDuration();

        int currentPosition = this.mediaPlayer.getCurrentPosition();
        if(currentPosition== 0)  {
            this.seekBar.setMax(duration);
            String maxTimeString = this.millisecondsToString(duration);

        } else if(currentPosition== duration)  {
            // Resets the MediaPlayer to its uninitialized state.
            this.mediaPlayer.reset();
        }
        this.mediaPlayer.start();
        // Create a thread to update position of SeekBar.
        UpdateSeekBarThread updateSeekBarThread= new UpdateSeekBarThread();
        threadHandler.postDelayed(updateSeekBarThread,50);

    }



    private String millisecondsToString(int milliseconds)  {
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds =  TimeUnit.MILLISECONDS.toSeconds((long) milliseconds) ;
        return minutes+":"+ seconds;
    }


    private class UpdateSeekBarThread implements Runnable{
        public void run()  {
            int currentPosition = videoView.getCurrentPosition();
            Log.d("value", "run: "+currentPosition);
            String currentPositionStr = millisecondsToString(currentPosition);


            seekBar.setProgress(currentPosition);
            // Delay thread 50 milisecond.
            threadHandler.postDelayed(this, 50);
        }
    }
}
