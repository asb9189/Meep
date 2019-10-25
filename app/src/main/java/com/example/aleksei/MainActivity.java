package com.example.aleksei.meep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private TextView loginTitle;
    private EditText loginEmail;
    private EditText loginPassword;
    private TextView loginRegister;
    private Button loginButton;
    private ProgressDialog progressDialog;

    public boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Grab firebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();



        //Set app to full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        loginTitle = (TextView) findViewById(R.id.welcome_title);
        loginEmail = (EditText) findViewById(R.id.register_email);
        loginPassword = (EditText) findViewById(R.id.register_password);
        loginRegister = (TextView) findViewById(R.id.login_register);
        loginButton = (Button) findViewById(R.id.register_button);

        loginButton.setOnClickListener(this);
        loginRegister.setOnClickListener(this);


    }

    @Override
    public void onClick(View view)
    {
        if (view == loginButton)
        {
            login();
        }

        if (view == loginRegister)
        {
            gotoRegistrationActivity();
        }
    }

    private void login()
    {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (email == null || email.length() <= 0)
        {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
        }

        if (password == null || password.length() <= 0)
        {
            Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_LONG).show();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we work hard to log you in!");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    final User user = new User();

                    Toast.makeText(MainActivity.this, "Successful login!", Toast.LENGTH_SHORT).show();

                    //Dialog related to alerting the user we are logging them in
                    progressDialog.cancel();

                    //Check if logging in user has completed profile or not.
                    databaseReference = firebaseDatabase.getReference("Users/" + firebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            if (loggedIn == false)
                            {
                                user.setProfileComplete(dataSnapshot.getValue(User.class).getProfileComplete());

                                //The profileComplete method returns TRUE, so we can skip the profile creation.
                                if(user.getProfileComplete())
                                {
                                    Intent intent = new Intent(MainActivity.this, ViewProfileActivity.class);
                                    startActivity(intent);
                                    loggedIn = true;
                                }

                                //the profileComplete method returns FALSE, we must send the user to the profile creation.
                                else
                                {
                                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                    loggedIn = true;

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
                            Log.d("MainActivity", "COULD NOT RECIEVE PROFILECOMPLETE FROM DATABASE");
                        }
                    });

                }

                else
                {
                    Toast.makeText(MainActivity.this, "Failed login, Please try again.", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();

                }
            }
        });

    }

    private void gotoRegistrationActivity()
    {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
        finish();
    }

}