package com.example.tennisanalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import io.github.deweyreed.digitalwatchview.DigitalWatchView;
import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

public class SessionActivity extends AppCompatActivity {

    // session date
    private String sessionDate;

    private Button session_BTN_startPause, session_BTN_stop;
    // stopper
    private DigitalWatchView digitalWatchView;

    private StatsFragment statsFragment;
    // linear acceleration sensor
    private LinearAcceleration linearAcceleration;

    private boolean isPause;

    private double secondsD; // seconds in double
    private int seconds, minutes, hours;

    private int endSessionImage;
    private String sessionType;
    private boolean isMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        setSessionDate();

        Intent intent = getIntent();
        isMatch = intent.getBooleanExtra("sessionType", isMatch);
        if(isMatch) {
            sessionType = "match";
            endSessionImage = R.drawable.ic_session_tennis_world_cup;
        } else {
            sessionType = "training";
            endSessionImage = R.drawable.ic_session_watter_bottle;
        }

        findViews();
        setOnClickListeners();
        openStatsFragment();

        linearAcceleration = new LinearAcceleration(this);
        linearAcceleration.setSensorCallback(sensorCallback);

        startStopper();
    }

    private void setSessionDate() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String currentHour = dateFormat.format(date);
        sessionDate = currentDate + " - " + currentHour;

        Log.d("pttt", sessionDate);
    }

    final SensorCallback sensorCallback = new SensorCallback() {
        @Override
        public void sendAccelerationSize(double size) {
            statsFragment.updateStats((int) size);
        }
    };

    public void startStopper() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                hours = (int) secondsD / 3600;
                minutes = (int) (secondsD % 3600) / 60;
                seconds = (int) secondsD % 60;
                digitalWatchView.setTime(hours, minutes, seconds);
                if (!isPause) {
                    secondsD += 0.1;
                }
                handler.postDelayed(this, 100);
            }
        });
    }

    private void openStatsFragment() {
        statsFragment = new StatsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.session_FRM_stats, statsFragment, "stats fragment");
        fragmentTransaction.commit();
    }

    private void setOnClickListeners() {

        session_BTN_startPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPause) {
                    session_BTN_startPause.setBackground(getDrawable(R.drawable.ic_session_pause_button));
                    resumeSession();
                } else {
                    session_BTN_startPause.setBackground(getDrawable(R.drawable.ic_session_play_button));
                    pauseSession();
                }
            }
        });

        session_BTN_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseSession();
                endSessionDialog(v);
            }
        });
    }

    private void endSessionDialog(final View view) {
        new AndExAlertDialog.Builder(SessionActivity.this)
                .setMessage("finish " + sessionType + "?")
                .setPositiveBtnText("yes")
                .setNegativeBtnText("no")
                .setImage(endSessionImage, 40)
                .setCancelableOnTouchOutside(true)
                .OnPositiveClicked(new AndExAlertDialogListener() {
                    @Override
                    public void OnClick(String input) {
                        saveSessionDialog(view);
                    }
                })
                .OnNegativeClicked(new AndExAlertDialogListener() {
                    @Override
                    public void OnClick(String input) {
                        resumeSession();
                    }
                })
                .build();

    }

    private void saveSessionDialog(View view) {
        new AndExAlertDialog.Builder(SessionActivity.this)
                .setMessage("save session" + "?")
                .setPositiveBtnText("yes")
                .setNegativeBtnText("no")
                .setImage(R.drawable.ic_session_save, 40)
                .setCancelableOnTouchOutside(true)
                .OnPositiveClicked(new AndExAlertDialogListener() {
                    @Override
                    public void OnClick(String input) {
                        saveSession();
                        gotoMainActivity();
                    }
                })
                .OnNegativeClicked(new AndExAlertDialogListener() {
                    @Override
                    public void OnClick(String input) {
                        gotoMainActivity();
                    }
                })
                .build();
    }

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    private void saveSession() {

        String user = "" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        rootRef.child("user").child(user).child(sessionDate).child("swingCount").setValue(statsFragment.getSwingCount());
        rootRef.child("user").child(user).child(sessionDate).child("swingAverage").setValue(statsFragment.getSwingAverage());
        rootRef.child("user").child(user).child(sessionDate).child("swingMax").setValue(statsFragment.getSwingMax());
        String sessionLength = "" + hours + ":" + (minutes);
        rootRef.child("user").child(user).child(sessionDate).child("sessionLength").setValue(sessionLength);
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(SessionActivity.this, MainActivity.class);
        startActivity(intent);

        this.finish();
    }

    private void resumeSession() {
        linearAcceleration.resume();
        isPause = false;
    }

    private void pauseSession() {
        linearAcceleration.pause();
        isPause = true;
    }

    private void findViews() {
        session_BTN_startPause = findViewById(R.id.session_BTN_startpause);
        session_BTN_stop = findViewById(R.id.session_BTN_stop);
        digitalWatchView = findViewById(R.id.digitalWatchView);
    }
}
