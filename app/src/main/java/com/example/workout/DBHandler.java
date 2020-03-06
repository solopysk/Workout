package com.example.workout;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHandler extends SQLiteOpenHelper {
    //database information
    private static final int DATABASE_VERSION = 1;
    public static final String DB_NAME = "WorkoutPrograms";
    public static final String TB_EXERCISES = "exercise";
    public static final String TB_WEEKS = "weeks";
    public static final String TB_PROGRAMS = "programs";

    public static final String COL_EXERCISES_ID = "ExerciseID";
    public static final String COL_EXERCISES_NAME = "ExerciseName";
    public static final String COL_EXERCISES_MUSCLE_TARGET = "Muscle_target";

    public static final String COL_PROGRAM_ID = "ProgramID";
    public static final String COL_PROGRAM_NAME = "ProgramName";

    public static final String COL_WEEK_PID = "ParentID";
    public static final String COL_WEEK_WEEKID = "WeekID";
    public static final String COL_WEEK_DAYID = "DayID";
    public static final String COL_WEEK_EXEC = "Exercise";
    public static final String COL_WEEK_SETNUM = "Set_num";
    public static final String COL_WEEK_REPNUM = "Repetition_num";
    SQLiteDatabase dB;

    //initialization
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        dB = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbl_cre = "CREATE TABLE IF NOT EXISTS ";
        String EXEC_TABLE = tbl_cre + "exercise" + "( " + "ExerciseID" + "  INTEGER PRIMARY KEY, " + "ExerciseName" + " TEXT )";
        String PROGRAM_TABLE = tbl_cre + TB_PROGRAMS + "( " + COL_PROGRAM_ID + " INTEGER PRIMARY KEY, " + COL_PROGRAM_NAME + " TEXT )";
        String WEEK_TABLE = tbl_cre + TB_WEEKS + "( " + COL_WEEK_PID + " INTEGER PRIMARY KEY," + COL_WEEK_WEEKID + " INTEGER," + COL_WEEK_DAYID + " INTEGER," + COL_WEEK_EXEC + " TEXT, " + COL_WEEK_SETNUM + " TEXT, " + COL_WEEK_REPNUM + " TEXT, " + "FOREIGN KEY(" + COL_WEEK_EXEC + ") REFERENCES " + TB_EXERCISES + "(" + COL_EXERCISES_NAME + ")," + "FOREIGN KEY(" + COL_WEEK_PID + ") REFERENCES " + TB_PROGRAMS + "(" + COL_PROGRAM_ID + "));";

        db.execSQL(EXEC_TABLE);
        db.execSQL(PROGRAM_TABLE);
        db.execSQL(WEEK_TABLE);

        Map<Integer,String> programs = new HashMap<>();
        programs.put(1,"Coje");
        programs.put(2,"5/3/1 BBB");
        programs.put(3,"Jacked & Tan");
        for(int i =0;i<programs.size();i++) {
            ContentValues val = new ContentValues();
            val.put(COL_PROGRAM_ID, i);
            val.put(COL_EXERCISES_NAME, programs.get(i));
            db.insert(TB_PROGRAMS, null, val);

            System.out.println(programs.size());
        }
        // exercise insert
        List<String> list = new ArrayList<>();
        list.add("Bench press");
        list.add("Shoulder press");
        list.add("Close grip bench press");
        list.add("Overhead triceps extension");
        list.add("Triceps pushdown");
        list.add("Incline bench press");
        list.add("Chest flyes");
        list.add("Skullcrushers");
        list.add("Push-ups");
        list.add("Pec deck");
        list.add("Cable row");
        list.add("Barbell row");
        list.add("Dumbbell row");
        list.add("Lat pulldown");
        list.add("Pullover");
        list.add("Shrugs");
        list.add("Conventional deadlift");
        list.add("Stiff leg deadlift");
        list.add("Snatch grip deadlift");
        list.add("Sumo deadlift");
        list.add("T-bar row");
        list.add("Machine row");
        list.add("Lateral raises");
        list.add("Rear delt flye");
        list.add("Front delt raises");
        list.add("Ez bar curl");
        list.add("Barbell curl");
        list.add("Dumbbell curl");
        list.add("Spider curl");
        list.add("Incline curl");
        list.add("Preacher curl");
        list.add("Hammer curl");
        list.add("Squat");
        list.add("Front squat");
        list.add("Wide-stance squat");
        list.add("Leg press");
        list.add("Leg extension");
        list.add("Leg curl");
        list.add("Glute raises");
        list.add("Lunges");
        list.add("Straight leg calf raise");
        list.add("Seated calf raise");
        list.add("Abs");
        for(int i =0;i<list.size();i++) {
            ContentValues val = new ContentValues();
            val.put(COL_EXERCISES_ID, i + 1);
            val.put(COL_EXERCISES_NAME, list.get(i));
            db.insert(TB_EXERCISES, null, val);
        }
       // db.insert(TB_PROGRAMS, COL_PROGRAM_ID, COL_PROGRAM_NAME, 1, "nSuns");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(dB);
    }

    public String loadHandler(String table_name) {
        String result = "";
        String SELECT_QUERY = "SELECT * FROM " + table_name;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        while (cursor.moveToNext()) {
            int res = cursor.getInt(0);
            String res2 = cursor.getString(2);
            result = String.valueOf(res) + " " + res2 + System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        return result;
    }

    public long addExercise(int id, String name) {
        ContentValues cv = new ContentValues();
        cv.put(COL_EXERCISES_ID, name);
        cv.put(COL_EXERCISES_NAME, name);
        return dB.insert(TB_EXERCISES, null, cv);

    }

    public long addWeek(int weekID, int dayID, String exercise, String sets, String reps) {
        ContentValues cv = new ContentValues();
        cv.put(COL_WEEK_WEEKID, weekID);
        cv.put(COL_WEEK_DAYID, dayID);
        cv.put(COL_WEEK_EXEC, exercise);
        cv.put(COL_WEEK_SETNUM, sets);
        cv.put(COL_WEEK_REPNUM, reps);
        return dB.insert(TB_WEEKS, null, cv);
    }

    public long addProgram(String name) {
        ContentValues cv = new ContentValues();
        cv.put(COL_PROGRAM_NAME, name);
        return dB.insert(TB_PROGRAMS, null, cv);
    }
}



