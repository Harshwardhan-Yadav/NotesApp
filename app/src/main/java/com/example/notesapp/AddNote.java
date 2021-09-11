package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class AddNote extends AppCompatActivity {

    EditText note;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("now",note.getText().toString());
        setResult(1,intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Intent intent = getIntent();
        String before = intent.getStringExtra("before");
        note=(EditText)findViewById(R.id.editTextTextPersonName);
        note.setText(before);
    }
}