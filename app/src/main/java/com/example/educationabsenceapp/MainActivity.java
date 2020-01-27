package com.example.educationabsenceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView email_not_verified_txt;
    Button verify_btn;
    FirebaseAuth fAuth;
    private EditText mrecipient, msubject, mmessage;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        verify_btn = findViewById(R.id.verify_btn);
        email_not_verified_txt = findViewById(R.id.email_not_verified_txt);
        FirebaseUser user = fAuth.getCurrentUser();
        mrecipient = findViewById(R.id.recipient_txt);
        msubject = findViewById(R.id.subject_txt);
        mmessage = findViewById(R.id.message_txt);
        send = findViewById(R.id.send_btn);


        if (!user.isEmailVerified()){
            email_not_verified_txt.setVisibility(View.VISIBLE);
            verify_btn.setVisibility(View.VISIBLE);

            verify_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = fAuth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Verification email resent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email has not been sent " + e.getMessage());
                        }
                    });
                }
            });

        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

    }

    private void sendMail(){
        String recipientList = mrecipient.getText().toString();
        String[] recipients = recipientList.split(",");
        //allows for more than one email address and then splits them email 1, email 2

        String subject = msubject.getText().toString();
        String message = mmessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "choose an email client"));
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}
