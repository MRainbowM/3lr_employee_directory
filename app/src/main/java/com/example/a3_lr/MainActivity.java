package com.example.a3_lr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnAdd, btnAddDep;
    private ListView lvWorkers;
    private Spinner spDeps;

    static public List<Worker> workers = new ArrayList();
    static public List<Department> deps = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button)findViewById(R.id.btnAddWorker);
        btnAddDep = (Button)findViewById(R.id.btnAddDep);
        spDeps = (Spinner)findViewById(R.id.spDeps);

        // начальная инициализация списка
        setInitialData();
        // получаем элемент ListView
        lvWorkers = (ListView)findViewById(R.id.lvWorkers);

        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // получаем выбранный пункт
                Worker selectedWorker = (Worker)parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Выбран: " + selectedWorker.getSurname() + " " + selectedWorker.getName() + " " + selectedWorker.getPatronymic(),  Toast.LENGTH_SHORT).show();
                editWorker(selectedWorker);
            }
        };
        lvWorkers.setOnItemClickListener(itemListener);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                Department dep = (Department)parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Выбран: "+ dep.getName(),  Toast.LENGTH_SHORT).show();
                filterListView(dep);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        spDeps.setOnItemSelectedListener(itemSelectedListener);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWorker();
            }
        });

        btnAddDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDep();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        renderListView();
        renderSpinner();
    }

    private void createWorker(){
        CardActivity card = new CardActivity();
        card.Method = "create";
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);
    }

    private void editWorker(Worker worker){
        CardActivity card = new CardActivity();
        card.Method = "edit";
        card.SelWorker = worker;
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);
    }

    private void createDep() {
        Intent intent = new Intent(this, DepActivity.class);
        startActivity(intent);
    }

    private void renderListView(){
        // создаем адаптер
        WorkerAdapter workerAdapter = new WorkerAdapter(this, R.layout.list_workers, workers);
        // устанавливаем адаптер
        lvWorkers.setAdapter(workerAdapter);
    }

    private void filterListView(Department dep){
        List<Worker> filterWorkers = new ArrayList();
        for(Worker worker : workers) {
            if(worker.getDep() == dep) {
                filterWorkers.add(worker);
            }
        }
        filterWorkers = sortWorkers(filterWorkers);

        // создаем адаптер
        WorkerAdapter workerAdapter = new WorkerAdapter(this, R.layout.list_workers, filterWorkers);
        // устанавливаем адаптер
        lvWorkers.setAdapter(workerAdapter);
    }


    private void renderSpinner(){
        // создаем адаптер
        DepAdapter adapter = new DepAdapter(this, R.layout.list_deps, deps);
        // устанавливаем адаптер
        spDeps.setAdapter(adapter);
    }

    private List<Worker> sortWorkers(List<Worker> workers) {
        for (int i = 0; (i < workers.toArray().length - 1); i++) {
            for (int j = 0; (j < workers.toArray().length - 1); j++) {
                if (workers.get(j).getStartDate().compareTo(workers.get(j + 1).getStartDate()) > 0) {
                    Collections.swap(workers, j, j + 1);
                }
            }
        }
        return workers;
    }

    private void setInitialData(){
        deps.add(new Department("Отдел продаж"));
        deps.add(new Department("Отдел маркетинга"));

        workers.add(new Worker ("Иванов", "Иван", "Иванович", "Инженер", deps.get(0), new GregorianCalendar(2020, 1 , 20)));
        workers.add(new Worker ("Петров", "Иван", "Иванович", "Инженер", deps.get(1), new GregorianCalendar(2017, 0 , 01)));
        workers.add(new Worker ("Сидоров", "Иван", "Иванович", "Инженер", deps.get(1), new GregorianCalendar(2018, 11 , 25)));
        workers.add(new Worker ("Сидоров2", "Иван", "Иванович", "Инженер", deps.get(1), new GregorianCalendar(2016, 11 , 25)));
    }
}
