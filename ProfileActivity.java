package com.example.aleksei.meep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener
{
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private EditText firstName;
    private EditText lastName;
    private CheckBox male;
    private CheckBox female;
    private EditText age;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Grab firebaseAuth objects
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();

        //Set app to full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile);

        //Get all views
        firstName = findViewById(R.id.profile_firstName);
        lastName = findViewById(R.id.profile_lastName);
        male = findViewById(R.id.profile_male);
        female = findViewById(R.id.profile_female);
        age = findViewById(R.id.profile_age);
        button = findViewById(R.id.profile_button);

        button.setOnClickListener(this);

        //If no user is logged in send them back to Login page
        if (firebaseUser == null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void createUser()
    {
        String first_name = firstName.getText().toString().trim();
        String last_name = lastName.getText().toString().trim();

        String gender = "";

        if (male.isChecked())
        {
            gender = "male";
        }

        if (female.isChecked())
        {
            gender = "female";
        }

        if (male.isChecked() && female.isChecked())
        {
            Toast.makeText(this, "Please only select one gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (male.isChecked() == false && female.isChecked() == false)
        {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        int years;

        if (age.getText().toString().length() == 0)
        {
            years = 0;
        }

        else
        {
            years = Integer.parseInt(age.getText().toString());
        }



        boolean profileComplete;

        //Check if profileComplete should be true or false
        if (first_name.length() == 0 || last_name.length() == 0 || gender.length() == 0 || years == 0 || gender.length() == 0)
        {
            profileComplete = false;
        }

        else
        {
            profileComplete = true;
        }

        User user = new User(first_name, last_name, gender, years, FirebaseAuth.getInstance().getCurrentUser().getEmail(), profileComplete);

        //Create a new node for each individual user
        firebaseDatabase.getReference().child("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

        //Bring them into the welcome screen
        Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View view)
    {
        if (view == button)
        {
            createUser();
        }
    }
}
