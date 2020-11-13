package com.example.a3_lr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.GregorianCalendar;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "4lr.db"; // название бд
    private static final int SCHEMA = 3; // версия базы данных
    static final String WORKERS_TABLE = "workers"; // название таблицы в бд
    // названия столбцов
    public static final String W_COLUMN_ID = "_id";
    public static final String W_COLUMN_NAME = "name";
    public static final String W_COLUMN_SURNAME = "surname";
    public static final String W_COLUMN_PATRONYMIC = "patronymic";
    public static final String W_COLUMN_POSITION = "position";
    public static final String W_COLUMN_STARTDATE = "start_date";
    public static final String W_COLUMN_DEP_ID = "dep_id";

    static final String DEP_TABLE = "departments"; // название таблицы в бд
    public static final String D_COLUMN_ID = "_id";
    public static final String D_COLUMN_NAME = "name";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + WORKERS_TABLE + " ("
                + W_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + W_COLUMN_NAME + " TEXT, "
                + W_COLUMN_SURNAME + " TEXT, "
                + W_COLUMN_PATRONYMIC + " TEXT, "
                + W_COLUMN_POSITION + " TEXT, "
                + W_COLUMN_STARTDATE + " TEXT, "
                + W_COLUMN_DEP_ID + " INTEGER);");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DEP_TABLE + " ("
                + D_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + D_COLUMN_NAME + " TEXT);");

        // добавление начальных данных

        db.execSQL("INSERT INTO " + WORKERS_TABLE + " ("
                + W_COLUMN_SURNAME + ", "
                + W_COLUMN_NAME + ", "
                + W_COLUMN_PATRONYMIC + ", "
                + W_COLUMN_POSITION + ", "
                + W_COLUMN_STARTDATE + ", "
                + W_COLUMN_DEP_ID
                + " ) VALUES ('Иванов', 'Иван', 'Иванович', 'Инженер', '2020|1|1', 1);");
        db.execSQL("INSERT INTO " + DEP_TABLE + " (" + D_COLUMN_NAME + ") VALUES ('Отдел продаж');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WORKERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DEP_TABLE);
        onCreate(db);
    }
}
