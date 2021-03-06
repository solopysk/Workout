package com.example.Workout.ui.Program;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.Workout.DBHandler;
import com.example.Workout.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProgramAdapter extends ArrayAdapter<WeekData> {
    private ArrayList<WeekData> dataSet;
    Context myContext;
    String rep;
    private int position;
    private boolean buttonenabled;
    private boolean containsMaxSet=false;

    public static class MyViewHolder {
        TextView textweek;
        TextView textday;
        TextView textexec;
        TextView textset;
        TextView textrep;
        TextView textweight;
        EditText textincweight;

    }


    public ProgramAdapter(ArrayList<WeekData> data, Context context) {
        super(context, R.layout.row_workout_item, data);
        this.dataSet = data;
        this.myContext = context;
    }

    public boolean weightIncrease(String name, int amount) {
        DBHandler dbHandler = new DBHandler(getContext());
        if (amount > 1 && amount < 4) {
            dbHandler.updateAmount(name, (float) 2.5);
        } else if (amount > 4) {
            dbHandler.updateAmount(name, (float) 5);
        } else {
            dbHandler.updateAmount(name, (float) 5);
        }
        return true;
    }

    public boolean isButtonEnabled() {
        return buttonenabled;
    }
    public boolean containsMaxSet(){
        return containsMaxSet;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final WeekData data = getItem(position);
        final MyViewHolder viewHolder;
        float getWeight = 0;
        boolean isNotSuper = false;
        Map<String, Float> getMax = new HashMap<>();
        final DBHandler dbHandler = new DBHandler(getContext());
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        //getting max weight that the user sets
        String b = String.join(" ", data.getExec());
        if (b.toLowerCase().contains("squat")) {
            b = "Squat";
        } else if (b.toLowerCase().contains("deadlift")) {
            b = "Deadlift";
        } else if (b.toLowerCase().contains("bench press")) {
            b = "Bench press";
        } else {
            isNotSuper = true;
        }
        if (!isNotSuper) {
            String selectquery = "SELECT " + dbHandler.COL_MAXES_WEIGHT + " FROM " + dbHandler.TB_MAXES + " WHERE " + String.join(" ", dbHandler.COL_MAXES_EXEC) + "=" + "'" + b + "'";
            Cursor c = db.rawQuery(selectquery, null);
            if (c.moveToFirst()) {
                getMax.put(b, c.getFloat(c.getColumnIndex(dbHandler.COL_MAXES_WEIGHT)));

            }
            c.close();
            if (!getMax.get(b).equals(0)) {
                getWeight = (getMax.get(b) * Float.parseFloat(data.getMax())) / 100;
            } else {
                getWeight = 0;
            }
        } else {
            getWeight = Float.parseFloat(data.getMax());
        }


        db.close();
        dbHandler.close();
        viewHolder = new MyViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (data.getType()) {
            containsMaxSet=true;
            this.position = position;
            convertView = inflater.inflate(R.layout.row_workout_weightincrease, parent, false);
            viewHolder.textexec = convertView.findViewById(R.id.textViewExec2);
            viewHolder.textset = convertView.findViewById(R.id.textViewSets2);
            viewHolder.textincweight = convertView.findViewById(R.id.textViewReps2);
            viewHolder.textweight = convertView.findViewById(R.id.textViewAmount2);
            viewHolder.textincweight.setHint(data.getRep() + "+");

            viewHolder.textincweight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String getReps = viewHolder.textincweight.getText().toString();
                        ProgramFragment.text = getReps;

                        if (!getReps.equals("")) {
                            weightIncrease(viewHolder.textexec.getText().toString(), Integer.parseInt(getReps));
                            viewHolder.textincweight.setText(getReps);
                            buttonenabled = true;

                        } else {
                            buttonenabled = false;
                        }
                    }

                }
            });
           // viewHolder.textincweight.setText(rep);
        } else if (!data.getType()) {
            convertView = inflater.inflate(R.layout.row_workout_item, parent, false);
            viewHolder.textexec = convertView.findViewById(R.id.textViewExec);
            viewHolder.textset = convertView.findViewById(R.id.textViewSets);
            viewHolder.textrep = convertView.findViewById(R.id.textViewReps);
            viewHolder.textweight = convertView.findViewById(R.id.textViewAmount);
            viewHolder.textrep.setText(data.getRep());
        }
        convertView.setTag(viewHolder);
        String a = String.valueOf(getWeight);
        viewHolder.textexec.setText(data.getExec());
        viewHolder.textset.setText(data.getSet());
        viewHolder.textweight.setText(a);

        return convertView;
    }

    public void setContainsMaxSet(boolean containsMaxSet) {
        this.containsMaxSet = containsMaxSet;
    }

    public void setButtonenabled(boolean buttonenabled) {
        this.buttonenabled = buttonenabled;
    }
}
