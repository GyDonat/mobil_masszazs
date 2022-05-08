package com.example.masszazs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private static int appointments_key = 5432345;

    Spinner orak;
    TextView visszajelzes;
    private FirebaseUser aktualUser;
    private String aktualUserEmail;

    private static final String package_nev = BookingActivity.class.getPackage().toString();

    private String selectedNap;
    private String selectedOra;
    private String vegsoIdopont;
    private boolean foglalt=false;

    private NotificationClass notification;

    private Intent myAppointments;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Date most;
    DatePicker date;

    private static final String activityNev = BookingActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);


        int key = getIntent().getIntExtra("key",0);

        if (key == 124353421)
        {

            visszajelzes = findViewById(R.id.uzenet);
            //Orak spinner + adapter beállítások
            orak = findViewById(R.id.orakSpinner);
            orak.setOnItemSelectedListener(this);
            orak.setSelection(2);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.orak, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            orak.setAdapter(adapter);

            //Múltra ne foglaljon már, és mára sem
            date = findViewById(R.id.datePicker);
            most = new Date();
            most.setDate(most.getDate()+1);
            long alsoKorlatMilliSec = most.getTime();
            date.setMinDate(alsoKorlatMilliSec);

            notification = new NotificationClass(this);

            aktualUser = FirebaseAuth.getInstance().getCurrentUser();

            if (aktualUser != null)
            {

                visszajelzes.setText("");
                aktualUserEmail=aktualUser.getEmail();
                Log.i(activityNev, aktualUserEmail + " ---- " + aktualUserEmail.toString());
            }
            else
            {
                visszajelzes.setText("Illegálban vagy heló");
                //finish();
            }

        }
        else
        {
            //finish();
            Intent backToLogin = new Intent(this, MainActivity.class);
            startActivity(backToLogin);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        selectedOra = " " + adapterView.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Ha véletlen nem választódna alapból ki semmi
        visszajelzes.setText("Kérlek válassz időpontot");
    }

    public void book(View view) {
        //long valami = date.get;
        foglalt = false;
       long mostMilliSec = new Date().getTime();
        int month = date.getMonth();
        if ( (month+1)<10)
        {
            selectedNap = Integer.toString(date.getYear()) + "-0" + Integer.toString(date.getMonth()+1) + "-" + Integer.toString(date.getDayOfMonth());
        }
        else
        {
            selectedNap = Integer.toString(date.getYear()) + "-" + Integer.toString(date.getMonth()+1) + "-" + Integer.toString(date.getDayOfMonth());
        }
        if (date.getDayOfMonth()<10)
        {
            selectedNap = selectedNap.substring(0,8) + "0" + selectedNap.substring(8);
        }

        vegsoIdopont = selectedNap+selectedOra;
        Log.i(activityNev,vegsoIdopont);
        checkFoglaltE();
        if (foglalt)
        {
            visszajelzes.setText("Időpont foglalt!");
        }
        else
        {
            Appointment foglalas = new Appointment(aktualUserEmail,vegsoIdopont);
            Map<String, String> vegsoFoglalas = new HashMap<>();
            vegsoFoglalas.put("email", foglalas.getEmail());
            vegsoFoglalas.put("idopont", foglalas.getIdopont());

            db.collection("Appointments").document(aktualUserEmail)
                    .set(vegsoFoglalas)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            visszajelzes.setText("Sikeres foglalás!");
                            notification.notify(foglalas.getIdopont());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(activityNev, "Valami hiba történt, kérlek próbáld újra!", e);
                        }
                    });
        }

    }

    private void checkFoglaltE() {
        db.collection("Appointments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                if ( document.get("idopont").equals(vegsoIdopont) )
                                {
                                    foglalt = true;
                                }
                            }
                        }
                    }
                });
    }

    public void goToMyAppointment(View view) {
        myAppointments = new Intent(this, LatestBookingActivity.class);
        myAppointments.putExtra("key", appointments_key);
        startActivity(myAppointments);
    }
}