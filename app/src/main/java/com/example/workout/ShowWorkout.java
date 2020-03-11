package com.example.workout;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShowWorkout extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity3);
        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DBHandler dbHandler = new DBHandler(getApplication().getApplicationContext());
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        recyclerViewAdapter = new MyAdapter(db);
        recyclerView.setAdapter(recyclerViewAdapter);
        Cursor cursor2 = db.query(DBHandler.TB_PROGRAMS, null, null, null, null, null, null);

        try {
            Scanner read = new Scanner(new File("data.txt"));
            read.useDelimiter(",");
            String week,day,exec,set,rep,max;
            List<String> prog = new ArrayList<>();
            while(read.hasNextLine()){
                week=read.next();
                day=read.next();
                exec=read.next();
                set=read.next();
                max=read.next();
                prog.add(week);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
