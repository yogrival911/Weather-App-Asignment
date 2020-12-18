package com.yogdroidtech.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
Button saveButton;
EditText editTextUserName;
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        saveButton = (Button)findViewById(R.id.buttonSave);
        editTextUserName = (EditText)findViewById(R.id.editTextUserName);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = editTextUserName.getText().toString();
                if(userName.trim().length()>0){
                    String formatUserName = userName.substring(0,1).toUpperCase()+userName.substring(1).toLowerCase();
                    sharedPreferences.edit().putString("userName",formatUserName ).commit();
                    Toast.makeText(getApplicationContext(), "User Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}