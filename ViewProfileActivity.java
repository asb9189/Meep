package com.example.aleksei.meep;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;

public class ViewProfileActivity extends AppCompatActivity implements View.OnClickListener
{

    private TextView view_profile_name;
    private TextView view_profile_email;
    private TextView view_profile_age;
    private TextView view_profile_gender;
    private TextView view_profile_profileComplete;
    private ImageView profilePicture;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Set app to full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_view_profile);

        bottomNavigationView = findViewById(R.id.view_profile_navigation);

        //Grab firebaseAuth objects
        String currentUserId = firebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users/" + currentUserId);

        view_profile_name = findViewById(R.id.view_profile_name);
        final TextView view_profile_email = findViewById(R.id.view_profile_email);
        final TextView view_profile_age = findViewById(R.id.view_profile_age);
        final TextView view_profile_gender = findViewById(R.id.view_profile_gender);
        profilePicture = findViewById(R.id.profile_picture);

        profilePicture.setOnClickListener(this);

        final User user = new User();



         databaseReference.addValueEventListener(new ValueEventListener()
         {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot)
             {
                 user.setFirstName(dataSnapshot.getValue(User.class).getFirstName());
                 user.setLastName(dataSnapshot.getValue(User.class).getLastName());

                 view_profile_name.setText(user.getFirstName() + " " + user.getLastName());

                 user.setEmail(dataSnapshot.getValue(User.class).getEmail());
                 view_profile_email.setText(user.getEmail().toString());

                 user.setAge(dataSnapshot.getValue(User.class).getAge());
                 view_profile_age.setText(Integer.toString(user.getAge()));

                 user.setGender(dataSnapshot.getValue(User.class).getGender());
                 view_profile_gender.setText(user.getGender());

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError)
             {

             }
         });


         //Create Listener For Bottom Navigation Bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.action_profile:
                        Toast.makeText(ViewProfileActivity.this, "Loading Your profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_home:
                        Toast.makeText(ViewProfileActivity.this, "Loading Your home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_social:
                        Toast.makeText(ViewProfileActivity.this, "Loading Your friends list", Toast.LENGTH_SHORT).show();
                        break;

                }

                return true;
            }

        });


    }

    @Override
    public void onClick(View view)
    {
        if (view == profilePicture)
        {
            chooseImage();
        }
    }

    private void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a picture"), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try
            {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                profilePicture.setImageBitmap(bitmap);
            }

            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
