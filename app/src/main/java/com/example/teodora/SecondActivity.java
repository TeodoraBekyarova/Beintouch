package com.example.teodora;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;

import java.sql.SQLException;
import java.util.ArrayList;

public class SecondActivity extends DBActivity {

    protected EditText editName, editNick, editPhone, editEmail, editStreet;
    protected Button btnAdd, btnBack;
    protected ListView simpleList;



    public void SelectAndFillListView(){
        try {
            final ArrayList<String> listResult=new ArrayList<>();
            SelectSQL("SELECT * FROM CONTACTS ORDER BY Name",
                    null,
                    new OnSelectSuccess() {
                        @Override
                        public void OnElementSelected(String ID, String Name, String Nick, String Phone, String Email, String Street) {
                            listResult.add(ID+"\t"+Name+"\t"+Nick+"\t"+Phone+"\t"+Email+"\t"+Street+"\n");
                        }

                        @Override
                        public void OnSuccess() {

                        }
                    }
            );
            simpleList.clearChoices();
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.activity_listview,
                    R.id.textView,
                    listResult

                    );
        simpleList.setAdapter(arrayAdapter);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        SelectAndFillListView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        editName=findViewById(R.id.editName);
        editNick=findViewById(R.id.editNick);
        editPhone=findViewById(R.id.editPhone);
        editEmail=findViewById(R.id.editEmail);
        editStreet=findViewById(R.id.editStreet);
        simpleList=findViewById(R.id.simpleList);
        btnAdd=findViewById(R.id.btnAdd);
        btnBack=findViewById(R.id.btnBack);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected="";
                selected=((TextView)(findViewById(R.id.textView))).getText().toString();
                String[] elements=selected.split("\t");
                String ID=elements[0];
                String Name=elements[1];
                String Nick=elements[2];
                String Phone=elements[3];
                String Email=elements[4];
                String Street=elements[5].trim();
                Intent intent=new Intent(SecondActivity.this, UpdateDelete.class);
                Bundle b=new Bundle();
                b.putString("ID", ID);
                b.putString("Name", Name);
                b.putString("Nick", Nick);
                b.putString("Phone", Phone);
                b.putString("Email", Email);
                b.putString("Street", Street);
                intent.putExtras(b);

                startActivityForResult(intent, 200, b);


            }
        });

        try {
            initDb();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(),
                    e.getMessage().toString(),
                    Toast.LENGTH_LONG
                    ).show();
        }
        SelectAndFillListView();
        btnAdd.setOnClickListener((view)->{
            String name = editName.getText().toString();
            String nick = editNick.getText().toString();
            String phone = editPhone.getText().toString();
            String email = editEmail.getText().toString();
            String street = editStreet.getText().toString();

            boolean check = info(name, nick, phone, email, street);
            if (check==true){
                Toast.makeText(getApplicationContext(), "Данните са валидни",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),"Невалидни данни", Toast.LENGTH_SHORT).show();
            }

            try {
                ExecSQL("INSERT INTO CONTACTS (Name, Nick, Phone, Email, Street)" +
                                "VALUES(?, ?, ?, ?, ?)",

                        new Object[]{
                                editName.getText().toString(),
                                editNick.getText().toString(),
                                editPhone.getText().toString(),
                                editEmail.getText().toString(),
                                editStreet.getText().toString()
                        },
                        new OnQuerySuccessError() {
                            @Override
                            public void OnSuccess() {
                                Toast.makeText(SecondActivity.this,
                                        "Record Inserted",
                                        Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void OnError(String error) {
                                Toast.makeText(SecondActivity.this,
                                        error,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                );
                SelectAndFillListView();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(),
                        e.getMessage().toString(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (!validateEmail()) {
                  //  return;
                //}
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
            }
        });
    }

    private boolean info(String name, String nick, String phone, String email, String street) {
        if (name.length()==0){
            editName.requestFocus();
            editName.setError("Полето е празно");
            return false;
        }else if (!name.matches("[a-zA-Z]+")){
            editName.requestFocus();
            editName.setError("Въведи само букви");
            return false;
        }else if (nick.length()==0){
            editNick.requestFocus();
            editNick.setError("Полето е празно");
            return false;
        }else if (!nick.matches("[a-zA-Z]+")){
            editNick.requestFocus();
            editNick.setError("Въведи само букви");
            return false;
        } else if (email.length()==0){
            editEmail.requestFocus();
            editEmail.setError("Полето е празно");
        }else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            editEmail.requestFocus();
            editEmail.setError("Моля въведи валиден имейл");
            return false;
        }else if(phone.length()==0){
            editPhone.requestFocus();
            editPhone.setError("Полето е празно");
            return false;
        }else if (!phone.matches("[0-9]{10,13}$")){
            editPhone.requestFocus();
            editPhone.setError("Моля въведи валиден телефон");
            return false;
        }
            return true;
    }

}