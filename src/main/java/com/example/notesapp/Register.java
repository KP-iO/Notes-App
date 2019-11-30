package com.example.notesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.Service.MusicService;

/**
 * Created by khrishawn
 */
public class Register extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button _btnreg,_btnlogin;
    EditText _txtpass, _txtemail;
    //Button webhtml;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        startService(new Intent(this, MusicService.class));
        openHelper=new DatabaseHelper(this);
        _btnlogin = (Button)findViewById(R.id.Signedup);
        _btnreg = (Button)findViewById(R.id.btnreg);
        _txtpass = (EditText)findViewById(R.id.password);
        _txtemail = (EditText)findViewById(R.id.username);
        //webhtml = (Button) findViewById(R.id.Instructions);


        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=openHelper.getWritableDatabase();
                String pass=_txtpass.getText().toString();
                String email=_txtemail.getText().toString();
                insertdata(pass,email);
                Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_LONG).show();
            }
        });
        _btnlogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openloginscreen();

            }

        });

//        webhtml.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openWebView();
//
//            }
//        });


//        _userG.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openinstructions();
//            }
//        });
    }
    public void openloginscreen(){
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }


//    public void openinstructions(){
//        Intent intent = new Intent(RegisterPage.this, Instructions.class);
//        startActivity(intent);
//    }

    public void insertdata(String email, String pass){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2,pass);
        contentValues.put(DatabaseHelper.COL_3,email);
        long id = db.insert(DatabaseHelper.TABLE_NAME,null, contentValues);
    }
}