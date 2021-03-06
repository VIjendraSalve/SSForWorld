package com.windhans.client.forworld.Activities;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.cooltechworks.views.ScratchImageView;
import com.cooltechworks.views.ScratchTextView;
import com.windhans.client.forworld.R;

public class Activity_My_Rewards extends AppCompatActivity {
    private ScratchImageView sample_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__my__rewards);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.myrewards_offers));

        sample_image=findViewById(R.id.sample_image);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ScratchTextView scratchTextView = new ScratchTextView(this);

        sample_image.setRevealListener(new ScratchImageView.IRevealListener() {
            @Override
            public void onRevealed(ScratchImageView iv) {

            }

            @Override
            public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
                siv.clear();
                //Toast.makeText(Activity_My_Rewards.this, ""+(percent*100), Toast.LENGTH_SHORT).show();
                if(percent >= 0.5){
                    siv.reveal();
                }
            }
        });

        scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
            @Override
            public void onRevealed(ScratchTextView tv) {
                //on reveal
            }


            @Override
            public void onRevealPercentChangedListener(ScratchTextView stv, float percent) {
                Toast.makeText(Activity_My_Rewards.this, ""+percent, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }


       /* if (item.getItemId() == R.id.reply_thread) {
            ReplayDialogue();
        }*/
        //overridePendingTransition(R.animator.left_right, R.animator.right_left);
        return super.onOptionsItemSelected(item);
    }
}
