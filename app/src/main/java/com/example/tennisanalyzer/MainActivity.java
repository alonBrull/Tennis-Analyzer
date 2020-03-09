package com.example.tennisanalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nipunbirla.swipe3drotateview.Swipe3DRotateView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Button main_BTN_start, main_BTN_prev;
    RelativeLayout main_RLY_root, main_RLY_prevBackground;
    FrameLayout main_FRM_previousSessions;

    private boolean prevIsOpen; // previous sessions fragment opened/closed indication

    private VideoView main_VID_background;

    private Swipe3DRotateView main_3d_rotation;

    private String sessionType = "match"; // default session type
    private boolean isMatch = true;

    private PreviousSessionsFragment previousSessionsFragment;

    private ArrayList<Session> previousSessions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPreviousSessionsList();

        findViews();

        setOnClickListeners();

        sessionTypeListener();

    }

    private void initPreviousSessionsList() {
        //root ref
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //user phone number (my UID)
        String user = "" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        //query
        Query userRef = rootRef.child("user").child(user).orderByPriority();

        userRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // add each session to list
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Session session = new Session(postSnapshot.getKey(),
                                    postSnapshot.child("swingCount").getValue(),
                                    postSnapshot.child("swingAverage").getValue(),
                                    postSnapshot.child("swingMax").getValue(),
                                    postSnapshot.child("sessionLength").getValue());

                            previousSessions.add(session);
                        }

                        initPreviousSessionsFragment();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    private void initPreviousSessionsFragment() {
        previousSessionsFragment = new PreviousSessionsFragment();
        previousSessionsFragment.setCallback(callback_previousSessions);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_FRM_previousSessions, previousSessionsFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    final Callback_previousSessions callback_previousSessions = new Callback_previousSessions() {
        @Override
        public ArrayList<Session> getPreviousSessions() {
            return previousSessions;
        }
    };

    private void sessionTypeListener() {
        main_3d_rotation.setIsXRotationAllowed(true);
        main_3d_rotation.setIsYRotationAllowed(true);
        main_3d_rotation.setAnimationDuration(500);
        main_3d_rotation.setHalfAnimationCompleteListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (isMatch) {
                    sessionType = "training";
                    isMatch = false;
                } else {
                    sessionType = "match";
                    isMatch = true;
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast toast = Toast.makeText(MainActivity.this, sessionType, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.setMargin(0, (float) 0.1);
                toast.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        backGroundVideoLoop();
    }

    private void backGroundVideoLoop() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vid_tennisball);
        main_VID_background.setVideoURI(uri);
        main_VID_background.start();
        main_VID_background.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                main_VID_background.start();
            }
        });
    }

    private void setOnClickListeners() {

        main_BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSessionActivity();
            }
        });

        main_BTN_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreviousSession();
                prevIsOpen = true;
            }
        });

        main_RLY_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prevIsOpen) {
                    closePreviousSession();
                    prevIsOpen = false;
                }
            }
        });
    }

    private void closePreviousSession() {
        ViewCompat.animate(main_RLY_prevBackground).scaleX(0).scaleY(0).setDuration(150).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                main_FRM_previousSessions.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(View view) {
                main_BTN_prev.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });
    }

    private void openPreviousSession() {
        ViewCompat.animate(main_RLY_prevBackground).scaleX(1).scaleY(1).setDuration(150).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                main_BTN_prev.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(View view) {
                main_FRM_previousSessions.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });
    }

    private void gotoSessionActivity() {
        Intent intent = new Intent(MainActivity.this, SessionActivity.class);
        intent.putExtra("sessionType", isMatch);
        startActivity(intent);

        this.finish();
    }

    private void findViews() {

        main_BTN_start = findViewById(R.id.main_BTN_start);
        main_BTN_prev = findViewById(R.id.main_BTN_prev);
        main_RLY_root = findViewById(R.id.main_RLY_root);
        main_RLY_prevBackground = findViewById(R.id.main_RLY_prevBackground);
        ViewCompat.animate(main_RLY_prevBackground).scaleX(0).scaleY(0).setDuration(0);
        main_VID_background = findViewById(R.id.main_VID_background);
        main_3d_rotation = findViewById(R.id.main_rotateView_sessionType);
        main_FRM_previousSessions = findViewById(R.id.main_FRM_previousSessions);

    }
}
