package com.example.aleksei.meep;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private TextView welcomeUser;
    private ConstraintLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.i("WelcomeActivity", "We made it to the WelcomeActivity!");

        //Set app to full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        //Grab firebaseAuth objects
        String currentUserId = firebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users/" + currentUserId);

        welcomeUser = findViewById(R.id.id_welcome_user);
        background = findViewById(R.id.welcome_background);

        final User user = new User();

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                user.setFirstName(dataSnapshot.getValue(User.class).getFirstName());
                welcomeUser.setText(user.getFirstName().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        ObjectAnimator colorFade = ObjectAnimator.ofObject(background, "backgroundColor", new ArgbEvaluator(), Color.argb(255,0,191,255), 0xff98f898);
        colorFade.setDuration(9000);
        colorFade.start();

        CountDownTimer countDownTimer = new CountDownTimer(8950,1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {

            }

            @Override
            public void onFinish()
            {
                Intent intent = new Intent(WelcomeActivity.this, ViewProfileActivity.class);
                startActivity(intent);
                finish();

            }
        }.start();

    }
}