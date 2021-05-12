package com.example.teodora;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.IllegalFormatCodePointException;

public class sms extends AppCompatActivity {

    private EditText phoneNumber, message;
    private Button btnSend, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        phoneNumber=findViewById(R.id.phoneNumber);
        message=findViewById(R.id.message);
        btnSend=findViewById(R.id.btnSend);
        btnBack=findViewById(R.id.btnBack);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                        sendSMS();
                    }else{
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);

                    }
                }

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sms.this, MainActivity.class));
            }
        });
    }
    private void sendSMS(){
        String phoneNo = phoneNumber.getText().toString().trim();
        String SMS = message.getText().toString().trim();

        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, SMS, null,null);
            Toast.makeText(this, "Съобщението е изпратено", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Съобщението не беше изпратено", Toast.LENGTH_SHORT).show();
        }


    }
}