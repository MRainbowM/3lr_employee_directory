package com.example.a3_lr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CardActivity extends AppCompatActivity {
    static public Worker SelWorker;
    static public String Method;
    static public Spinner spDepSel;

    private Department selDep;
    private Calendar selDate;

    private Button btnBack, btnSave, btnDel;
    private EditText etF, etI, etO, etNumDep, etPosition, etStartDate;
    private CalendarView calendarView;

    private DatabaseHelper sqlHelper;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDel = (Button) findViewById(R.id.btnDel);

        etF = (EditText) findViewById(R.id.etF);
        etI = (EditText) findViewById(R.id.etI);
        etO = (EditText) findViewById(R.id.etO);
//        etNumDep = (EditText)findViewById(R.id.etNumDep);
        etPosition = (EditText) findViewById(R.id.etPosition);
        etStartDate = (EditText) findViewById(R.id.etStartDate);

        spDepSel = (Spinner) findViewById(R.id.spDepSel);

        calendarView = (CalendarView) findViewById(R.id.calendarView);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Method == "create") {
                    AddWorker();
                } else if (Method == "edit") {
                    SaveWorker();
                }
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DelWorker();
            }
        });

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                Department dep = (Department) parent.getItemAtPosition(position);
                selDep = dep;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spDepSel.setOnItemSelectedListener(itemSelectedListener);
        renderSpinner();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                String selectedDate = new StringBuilder().append(mDay).append(".").append(mMonth + 1).append(".").append(mYear).append(" ").toString();
                Toast.makeText(getApplicationContext(), selectedDate, Toast.LENGTH_LONG).show();

                selDate = new GregorianCalendar(mYear, mMonth, mDay);
                mMonth = mMonth + 1;
                etStartDate.setText(mDay + "." + mMonth + "." + mYear);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Method == "edit") {
            if (SelWorker.getSurname() != null) etF.setText(SelWorker.getSurname());
            if (SelWorker.getName() != null) etI.setText(SelWorker.getName());
            if (SelWorker.getPatronymic() != null) etO.setText(SelWorker.getPatronymic());
            if (SelWorker.getPosition() != null) etPosition.setText(SelWorker.getPosition());

            if (SelWorker.getStartDate() != null) {
                Calendar date;
                if (SelWorker.getStartDate() != null) {
                    date = SelWorker.getStartDate();
                } else {
                    date = new GregorianCalendar(2020, 0, 1);
                }

                Integer year = date.get(date.YEAR);
                Integer month = date.get(date.MONTH) + 1;
                Integer day = date.get(date.DAY_OF_MONTH);
                etStartDate.setText(day.toString() + "." + month.toString() + "." + year.toString());
                calendarView.setDate(date.getTimeInMillis(), true, true);
            }
            selectValueDep(SelWorker.getDep());
        }
    }


    private void AddWorker() {
        Worker worker = new Worker(etF.getText().toString(), etI.getText().toString(), etO.getText().toString(), etPosition.getText().toString(), selDep, selDate);

        if (MainActivity.configDB) {
            ContentValues cv = getContentValByWorker(worker);
            db.insert(DatabaseHelper.WORKERS_TABLE, null, cv);
            db.close();
        } else {
            MainActivity.workers.add(worker);
        }
        Toast.makeText(getApplicationContext(), "Сотрудник добавлен", Toast.LENGTH_SHORT).show();
        closeActivity();
    }

    private void SaveWorker() {
        MainActivity main = new MainActivity();
        for (Worker worker : main.workers) {
            if (worker == SelWorker) {

                worker.setSurname(etF.getText().toString());
                worker.setName(etI.getText().toString());
                worker.setPatronymic(etO.getText().toString());
                worker.setPosition(etPosition.getText().toString());
                worker.setDep(selDep);
                worker.setStartDate(selDate);

                if (MainActivity.configDB) {
                    ContentValues cv = getContentValByWorker(worker);
                    db.update(DatabaseHelper.WORKERS_TABLE, cv, DatabaseHelper.W_COLUMN_ID + "=" + String.valueOf(worker.getID()), null);
                    db.close();
                }

                Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                closeActivity();
                return;
            }
        }
    }

    private ContentValues getContentValByWorker(Worker worker) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.W_COLUMN_SURNAME, worker.getSurname());
        cv.put(DatabaseHelper.W_COLUMN_NAME, worker.getName());
        cv.put(DatabaseHelper.W_COLUMN_PATRONYMIC, worker.getPatronymic());
        cv.put(DatabaseHelper.W_COLUMN_POSITION, worker.getPosition());
        cv.put(DatabaseHelper.W_COLUMN_DEP_ID, worker.getDep().getID());
        if (worker.getStartDate() != null) {
            Calendar date = worker.getStartDate();
            Integer year = date.get(date.YEAR);
            Integer month = date.get(date.MONTH);
            Integer day = date.get(date.DAY_OF_MONTH);

            cv.put(DatabaseHelper.W_COLUMN_STARTDATE, year.toString() + ":" + month.toString() + ":" + day.toString());
        } else {
            cv.put(DatabaseHelper.W_COLUMN_STARTDATE, new GregorianCalendar(2020, 0, 1).toString());
        }
        return cv;
    }

    private void DelWorker() {
        for (Worker worker : MainActivity.workers) {
            if (worker == SelWorker) {
                if (MainActivity.configDB) {
                    db.delete(DatabaseHelper.WORKERS_TABLE, "_id = ?", new String[]{String.valueOf(SelWorker.getID())});
                    db.close();
                } else {
                    MainActivity.workers.remove(worker);
                }

                Toast.makeText(getApplicationContext(), "Удалено", Toast.LENGTH_SHORT).show();
                closeActivity();
                return;
            }
        }
    }

    private void renderSpinner() {
        MainActivity main = new MainActivity();
        DepAdapter adapter = new DepAdapter(this, R.layout.list_deps, main.deps);
        spDepSel.setAdapter(adapter);
    }

    private void selectValueDep(Department dep) {
        for (int i = 0; i < spDepSel.getCount(); i++) {
            if (spDepSel.getItemAtPosition(i).equals(dep)) {
                spDepSel.setSelection(i);
                break;
            }
        }
    }

    private void closeActivity() {
        this.finish();
    }
}
