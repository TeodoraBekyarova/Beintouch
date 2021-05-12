package com.example.teodora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

public class UpdateDelete extends DBActivity {
    protected EditText editName, editNick, editPhone, editEmail, editStreet;
    protected Button btnUpdate, btnDelete;
    protected String ID;
    protected void BackToMain(){
        finishActivity(200);
        Intent intent=new Intent(UpdateDelete.this, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        editName=findViewById(R.id.editName);
        editNick=findViewById(R.id.editNick);
        editPhone=findViewById(R.id.editPhone);
        editEmail=findViewById(R.id.editEmail);
        editStreet=findViewById(R.id.editStreet);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);
        Bundle b=getIntent().getExtras();
        if(b!=null){
            ID=b.getString("ID");
            editName.setText(b.getString("Name"));
            editNick.setText(b.getString("Nick"));
            editPhone.setText(b.getString("Phone"));
            editEmail.setText(b.getString("Email"));
            editStreet.setText(b.getString("Street"));
        }

        btnDelete.setOnClickListener(view -> {
            try {
                ExecSQL("DELETE FROM CONTACTS WHERE ID = ?",
                        new Object[]{ID},
                        new OnQuerySuccessError() {
                            @Override
                            public void OnSuccess() {
                                Toast.makeText(UpdateDelete.this,
                                        "Delete Successful", Toast.LENGTH_LONG).show();
                                BackToMain();
                            }
                            @Override
                            public void OnError(String error) {
                                Toast.makeText(UpdateDelete.this,
                                        error,Toast.LENGTH_LONG).show();
                            }
                        }
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        btnUpdate.setOnClickListener(view -> {
            try {
                ExecSQL("UPDATE CONTACTS SET " +
                                "Name = ?, " +
                                "Nick = ?, " +
                                "Phone = ?, " +
                                "Email = ?, " +
                                "Street = ? " +
                                "WHERE ID = ? ",
                        new Object[]{
                                editName.getText().toString(),
                                editNick.getText().toString(),
                                editPhone.getText().toString(),
                                editEmail.getText().toString(),
                                editStreet.getText().toString(),
                        },
                        new OnQuerySuccessError() {
                            @Override
                            public void OnSuccess() {
                                Toast.makeText(UpdateDelete.this,
                                        "Update Successful", Toast.LENGTH_LONG).show();
                                BackToMain();
                            }
                            @Override
                            public void OnError(String error) {
                                Toast.makeText(UpdateDelete.this,
                                        error,Toast.LENGTH_LONG).show();
                            }
                        }
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}