package com.example.teodora;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public abstract class DBActivity extends AppCompatActivity {
    protected boolean hasNewVersion=true;
    protected void initDb() throws SQLException {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath()+"/contacts.db",
                null
        );
        if (hasNewVersion)
        {
            String q="DROP TABLE if exists Contacts; ";
            db.execSQL(q);
            hasNewVersion=false;

        }
        String createQ="CREATE TABLE if not exists CONTACTS("+
                "ID integer PRIMARY KEY AUTOINCREMENT, " +
                "Name text not null," +
                "Nick text not null," +
                "Phone text not null," +
                "Email text not null," +
                "Street text not null," +
                "unique(Phone, Name, Email)" +

                ")";
        db.execSQL(createQ);
        db.close();
    }

    protected void SelectSQL(String SelectQ,
                             String[] args,
                             OnSelectSuccess success
                             ) throws  Exception{
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath()+"/contacts.db",
                null);
        db.beginTransaction();
        Cursor cursor=db.rawQuery(SelectQ, args);
        while(cursor.moveToNext()){
            String ID = cursor.getString(cursor.getColumnIndex("ID"));
            String Name = cursor.getString(cursor.getColumnIndex("Name"));
            String Nick = cursor.getString(cursor.getColumnIndex("Nick"));
            String Phone = cursor.getString(cursor.getColumnIndex("Phone"));
            String Email = cursor.getString(cursor.getColumnIndex("Email"));
            String Street = cursor.getString(cursor.getColumnIndex("Street"));
            success.OnElementSelected(ID, Name, Nick, Phone, Email, Street);
        }
        db.endTransaction();
        db.close();
    }

    protected void ExecSQL
            (String SQL, Object[] args, OnQuerySuccessError successError)
    throws SQLException
    {
        SQLiteDatabase db=null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase(
                    getFilesDir().getPath()+"/contacts.db",
                    null
            );
            String Q= SQL;
            if(args!=null)
                db.execSQL(Q, args);
            else
                db.execSQL(Q);
            successError.OnSuccess();

        }catch (Exception e){
            successError.OnError(e.getMessage().toString());
        }finally {
            if(db!=null)
            db.close();
        }
    }

    protected interface OnQuerySuccessError{
        public void OnSuccess();
        public void OnError(String error);
    }


    protected interface OnSelectSuccess{
        public void OnElementSelected(String ID,
                                      String Name,
                                      String Nick,
                                      String Phone,
                                      String Email,
                                      String Street
                                      );

        void OnSuccess();
    }
}
