package com.google.healthme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DoctorPatient extends AppCompatActivity {
    CardView doctorCard, patientCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient);
        doctorCard=findViewById(R.id.doctor_card);
        patientCard=findViewById(R.id.patient_card);

        doctorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorPatient.this,DoctorRegister.class));
                finish();
            }
        });

        patientCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorPatient.this,SignUp.class));
                finish();
            }
        });
    }


}
