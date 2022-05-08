package com.example.masszazs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LatestBookingActivity extends AppCompatActivity {


    private TextView utsoBook;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser aktualUser;
    private FirebaseAuth auth;
    private String aktualUserEmail;
    private static int booking_key = 124353421;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_booking);

        int key = getIntent().getIntExtra("key",0);
        if (key == 5432345)
        {
            utsoBook = findViewById(R.id.utolsoBook);
            aktualUser = FirebaseAuth.getInstance().getCurrentUser();

            if (aktualUser != null)
            {
                auth =  FirebaseAuth.getInstance();
                aktualUserEmail=aktualUser.getEmail();
            }
        }
        else
        {
            Intent backToLogin = new Intent(this, MainActivity.class);
            startActivity(backToLogin);
        }

        utsoFrissit();


    }

    private void utsoFrissit() {

        db.collection("Appointments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                if ( document.get("email").equals(aktualUserEmail) )
                                {
                                    utsoBook.setText( document.get("idopont").toString() );
                                }
                            }
                        }
                    }
                });

    }

    public void cancelBook(View view) {

        db.collection("Appointments").document(aktualUserEmail)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        utsoBook.setText("Nincs foglalásod :(");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        utsoBook.setText(utsoBook.getText() + "Hiba történt a törlés során");
                    }
                });

    }

    public void updateBook(View view) {
        goToBooking();

    }

    private void goToBooking() {
        Intent bookingIntent = new Intent(this, BookingActivity.class);
        bookingIntent.putExtra("key",booking_key);
        startActivity(bookingIntent);
    }


    public void logout(View view) {
        auth.signOut();
        Intent backToLogin = new Intent(this, MainActivity.class);
        startActivity(backToLogin);
    }
}