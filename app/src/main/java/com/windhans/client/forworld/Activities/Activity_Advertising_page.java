package com.windhans.client.forworld.Activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.windhans.client.forworld.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Activity_Advertising_page extends AppCompatActivity implements Runnable {

    VideoView videoView;
    MediaController mediacontroller;
    Uri uri;
    Button btn_close;
    public int counter;
    ProgressBar simpleProgressBar;
    int progress = 0;
    private int progressCount;
    private SeekBar mSeekBar;
    private int mInterval = 5;
    MediaPlayer mp;
    private Handler mHandler;
    int duration_final;
    private Runnable mRunnable;
    int stopPosition;
    boolean wasPlaying;
    int current_position, current_position1;
    String urlPath;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__advertising_page);
        btn_close = findViewById(R.id.btn_close);
        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        mSeekBar = findViewById(R.id.seek_bar);
        mHandler = new Handler();


        urlPath = "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
        uri = Uri.parse(urlPath);


        mp = new MediaPlayer();
        try {
            mp.setDataSource(Activity_Advertising_page.this, uri);
            mSeekBar.setMax(mp.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String durartion = String.valueOf(mp.getDuration());
        Log.d("position", "onCreate: " + durartion);
        String[] duration1 = durartion.split("-");
        duration_final = Integer.parseInt(duration1[1]);
        String time = String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(duration_final),
                TimeUnit.MILLISECONDS.toSeconds(duration_final) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration_final))
        );
        progress = duration_final;
        simpleProgressBar.setMax(progress); // 100 maximum value for the progress value
        simpleProgressBar.setProgress(0);
        //String time3 = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(current_position));
        setProgressValue(progress);
        // 50 default progress value for the progress bar
        int time1 = (duration_final);
        Log.d("time", "onCreate: " + time1);

        videoView = (VideoView) findViewById(R.id.exerciseVideo);
        mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);
        current_position = videoView.getCurrentPosition();
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        mediacontroller.setMediaPlayer(videoView);
        initializeSeekBar();


        new CountDownTimer(time1, 1000) {

            public void onTick(long millisUntilFinished) {
          /*  counter++;
                Toast.makeText(Activity_Advertising_page.this, "remining", Toast.LENGTH_SHORT).show();
                //here you can have your logic to set text to edittext
                btn_close.setVisibility(View.GONE);
*/

                long sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                Log.e("time", "onTick: " + sec);
                //   tv_timer.setText(String.format("( %02d SEC )", sec));
                if (sec == 1) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //  tv_timer.setText("( 00 SEC )");
                            btn_close.setVisibility(View.VISIBLE);
                        }
                    }, 1000);
                }

            }

            public void onFinish() {
                // mTextField.setText("done!");
                Toast.makeText(Activity_Advertising_page.this, "Done", Toast.LENGTH_SHORT).show();
                btn_close.setVisibility(View.VISIBLE);
            }

        }.start();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress2, boolean b) {
                if (progress > 0 && mp != null && !mp.isPlaying()) {
                    //  clearMediaPlayer();
                    Activity_Advertising_page.this.mSeekBar.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mp != null && mp.isPlaying()) {
                    mp.seekTo(seekBar.getProgress());
                }
            }
        });


    }


    public void run() {

        int currentPosition = mp.getCurrentPosition();
        int total = mp.getDuration();

        while (mp != null && mp.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mp.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }

            mSeekBar.setProgress(currentPosition);
        }
    }


    private void initializeSeekBar() {
        Log.d("pos", "initializeSeekBar: " + duration_final);
        mSeekBar.setMax(duration_final);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mp != null) {
                    current_position = videoView.getCurrentPosition();
                    String time1 = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(current_position));

                    //  int mCurrentPosition1 = current_position/mInterval; // In milliseconds
                  /*  int mCurrentPosition = mp.getCurrentPosition(); // In milliseconds
                    Log.d("value", "run: "+mCurrentPosition);*/
                    Log.d("value", "data: " + time1);
                    mSeekBar.setProgress(Integer.parseInt(time1));

                }
                mHandler.postDelayed(mRunnable, mInterval);
            }
        };
        mHandler.postDelayed(mRunnable, mInterval);
    }


    private void setProgressValue(int progress) {
        simpleProgressBar.setProgress(progress);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressValue(progress + 1);
            }

        });
        thread.start();


    }

    public void onResume() {

        super.onResume();
        videoView.seekTo(stopPosition);
        //  videoView.requestFocus();
        videoView.start();
        //  videoView.resume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
        // Disable back button..............
    }

    @Override
    protected void onUserLeaveHint() {
        //  onPause();
        //   current_position1=mp.getCurrentPosition();
        Toast.makeText(Activity_Advertising_page.this, "Home buton pressed", Toast.LENGTH_LONG).show();
        super.onUserLeaveHint();
    }

    @Override
    public void onPause() {
        Log.d("pause", "onPause called");
        super.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        Log.d("stopPosition", "onPause: " + stopPosition);
        videoView.pause();
    }


}
