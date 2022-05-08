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

public class RegisterActivity extends AppCompatActivity {

    private static final String activityNev = RegisterActivity.class.getName();
    private static final String package_nev = RegisterActivity.class.getPackage().toString();
    private static int booking_key = 124353421;

    EditText emailET;
    EditText userNameET;
    EditText passwordET;
    EditText passwordAgainET;
    TextView uzenet;

    private SharedPreferences preferences;
    private FirebaseAuth auth;
    private Intent bookingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailET = findViewById(R.id.emailText);
        passwordET = findViewById(R.id.passwordText);
        passwordAgainET = findViewById(R.id.passwordAgainText);
        userNameET = findViewById(R.id.userNameText);

        uzenet = findViewById(R.id.uzenet);

        //Shared preferences lekérése
        preferences = getSharedPreferences(package_nev,MODE_PRIVATE);
        String emailPref = preferences.getString("email", "");
        String passwordPref = preferences.getString("password", "");

        bookingIntent = new Intent(this, BookingActivity.class);
        bookingIntent.putExtra("key", booking_key);

        auth = FirebaseAuth.getInstance();
        int key = getIntent().getIntExtra("key",0);

        //Nem-e illetéktelenül hívták meg
        if (key != 123454321) {
            finish();
        }
    }

    public void register(View view) {

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();
        String userName = userNameET.getText().toString();
        uzenet.setText("");

        if (password.equals(passwordAgain) && !(email.isEmpty()) && !(password.isEmpty()) && !(userName.isEmpty()))
        {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        startActivity(bookingIntent);
                    }
                    else
                    {
                        uzenet.setText("Valami hiba történt, kérlek próbáld újra!");
                    }
                }
            });
            Log.i(activityNev,"Beregisztrált: " + email + ", jelszó: " + password + ", becenév: " + userName);
        }
        else
        {
            uzenet.setText("Helytelen adatok!");

        }

    }

    public void cancel(View view) {
        finish();
    }

}