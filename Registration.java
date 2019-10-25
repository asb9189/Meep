package com.example.aleksei.meep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener
{
    private TextView registerTitle;
    private EditText registerEmail;
    private EditText registerPassword;
    private TextView registerLogin;
    private Button registerButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private ProgressDialog progressDialog;

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

        setContentView(R.layout.activity_registration);

        registerTitle = findViewById(R.id.welcome_title);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerLogin = findViewById(R.id.register_login);
        registerButton = findViewById(R.id.register_button);

        registerLogin.setOnClickListener(this);
        registerButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view)
    {
        if (view == registerLogin)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if (view == registerButton)
        {
            String email = registerEmail.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("One moment while we set up your new account!");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        User user = new User();
                        user.setFirstName("");
                        user.setLastName("");
                        user.setGender("");
                        user.setAge(0);
                        user.setEmail("");
                        user.setProfileComplete(false);

                        progressDialog.cancel();
                        Toast.makeText(Registration.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                        //create node with false as profileComplete and check for this when logging in.
                        firebaseDatabase.getReference().child("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

                        Intent intent = new Intent(Registration.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                        progressDialog.cancel();
                        Toast.makeText(Registration.this, "Oops there was an error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
