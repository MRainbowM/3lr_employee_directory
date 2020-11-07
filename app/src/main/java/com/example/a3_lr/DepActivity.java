package com.example.a3_lr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DepActivity extends AppCompatActivity {

    private Button btnBackDep,btnSaveDep;
    private EditText etNameDep;

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


    }
    private void closeActivity() { this.finish(); }

    private void saveDep() {
        Department dep = new Department(etNameDep.getText().toString());
        MainActivity main = new MainActivity();
        main.deps.add(dep);
        Toast.makeText(getApplicationContext(), "Отдел добавлен",  Toast.LENGTH_SHORT).show();
        closeActivity();
    }
}
