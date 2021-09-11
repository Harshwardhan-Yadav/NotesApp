package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*
    (*)When app is opened for the first time it should display "Example note"
    (*)In settings we can add a new note
    (*)Notes should be displayed in a listview.
    (*)When can long press a node to delete it (Alert dialog)
    */

    SharedPreferences sharedPreferences;
    ListView listView;
    ArrayList<String> notes;
    ArrayAdapter<String> arrayAdapter;
    char delimeter=(char)27;
    int replace;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            if(replace==-1)
                notes.add(data.getStringExtra("now"));
            else{
                notes.set(replace,data.getStringExtra("now"));
            }
            sharedPreferences.edit().putString("notes",encodeNotes(notes)).apply();
            makeList();
        }
    }

    public void makeList(){
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);
    }

    public ArrayList<String> decodeNotes(String notesList){
        String[] arr = notesList.split(delimeter+"");
        ArrayList<String> result=new ArrayList<>();
        for(String s: arr){
            result.add(s);
        }
        return result;
    }

    public String encodeNotes(ArrayList<String> arr){
        String arrString="";
        for(int i=0;i<arr.size();i++){
            if(i>0){
                arrString+=delimeter+arr.get(i);
            }
            else{
                arrString+=arr.get(i);
            }
        }
        return arrString;
    }

    public void addNote(String before,int Replace){
        replace=Replace;
        Intent intent = new Intent(getApplicationContext(),AddNote.class);
        intent.putExtra("before",before);
        startActivityForResult(intent,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.addNote:
                addNote("Your Note Here...",-1);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.myListView);

        String notesList = sharedPreferences.getString("notes",delimeter+"empty");

        if(notesList.compareTo(delimeter+"empty")==0){
            notes = new ArrayList<String>();
            notes.add("Example Note.");
            sharedPreferences.edit().putString("notes",encodeNotes(notes)).apply();
            makeList();
        }
        else{
            notes = decodeNotes(notesList);
            makeList();
        }
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm!!")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setMessage("Are u sure you want to remove the note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                if(notes.size()==0)
                                    sharedPreferences.edit().remove("notes").apply();
                                else
                                    sharedPreferences.edit().putString("notes",encodeNotes(notes)).apply();
                                makeList();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addNote(notes.get(position),position);
            }
        });
    }
}