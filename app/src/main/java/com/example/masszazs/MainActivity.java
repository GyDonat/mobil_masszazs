package com.example.masszazs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private static final String activityNev = MainActivity.class.getName();
    private static final String package_nev = MainActivity.class.getPackage().toString();
    private static int register_key = 123454321;
    private static int booking_key = 124353421;

    EditText emailET;
    EditText passwordET;
    TextView uzenet;

    private  Intent bookingIntent;

    private FirebaseAuth auth;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       emailET = findViewById(R.id.editTextEmail);
       passwordET = findViewById(R.id.editTextPassword);

       uzenet = findViewById(R.id.uzenet);

       auth =  FirebaseAuth.getInstance();

       bookingIntent = new Intent(this, BookingActivity.class);

       adatokBetolt();

    }

    public void login(View view) {
        uzenet.setText("");

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    bookingIntent.putExtra("email",emailET.getText().toString());
                    bookingIntent.putExtra("key", booking_key);
                    startActivity(bookingIntent);
                }
                else
                {
                    uzenet.setText("Hibás adatok");
                }
            }
        });

        Log.i(activityNev,"Bejelentkezett: " + email + ", jelszó: " + password);

    }

    public void register(View view) {

        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerIntent.putExtra("key", register_key);

        startActivity(registerIntent);

    }

    private void goToBooking() {
        Intent bookingIntent = new Intent(this, BookingActivity.class);
        bookingIntent.putExtra("email",emailET.getText().toString());
        bookingIntent.putExtra("key", booking_key);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adatokBetolt();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adatokLement();
    }

    private void adatokBetolt() {
        //Shared preferences lekérése
        preferences = getSharedPreferences(package_nev,MODE_PRIVATE);
        emailET.setText(preferences.getString("email",""));
        passwordET.setText(preferences.getString("password",""));
    }

    private void adatokLement() {
        //Shared preferences lementése
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email",emailET.getText().toString());
        editor.putString("password",passwordET.getText().toString());
        editor.apply();
    }

}