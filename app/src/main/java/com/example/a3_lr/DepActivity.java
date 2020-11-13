package com.example.a3_lr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DepActivity extends AppCompatActivity {

    private Button btnBackDep,btnSaveDep;
    private EditText etNameDep;

    private DatabaseHelper sqlHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep);

        btnBackDep = (Button)findViewById(R.id.btnBackDep);
        btnSaveDep = (Button)findViewById(R.id.btnSaveDep);
        etNameDep = (EditText)findViewById(R.id.etNameDep);

        btnBackDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
        btnSaveDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDep();
            }
        });

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();
    }
    private void closeActivity() { this.finish(); }

    private void saveDep() {
        Department dep = new Department(etNameDep.getText().toString());

        if (MainActivity.configDB) {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.D_COLUMN_NAME, dep.getName());
            db.insert(DatabaseHelper.DEP_TABLE, null, cv);
            db.close();
        } else {
            MainActivity.deps.add(dep);
        }
        Toast.makeText(getApplicationContext(), "Отдел добавлен",  Toast.LENGTH_SHORT).show();
        closeActivity();
    }

}
