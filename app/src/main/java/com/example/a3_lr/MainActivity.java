package com.example.a3_lr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

    static public Boolean configDB = Boolean.FALSE;
//    static public Boolean configDB = Boolean.TRUE;

    private DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor dbCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAddWorker);
        btnAddDep = (Button) findViewById(R.id.btnAddDep);
        spDeps = (Spinner) findViewById(R.id.spDeps);

        dbHelper = new DatabaseHelper(getApplicationContext());

        // начальная инициализация списка
        setInitialData();
        // получаем элемент ListView
        lvWorkers = (ListView) findViewById(R.id.lvWorkers);

        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // получаем выбранный пункт
                Worker selectedWorker = (Worker) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Выбран: " + selectedWorker.getSurname() + " " + selectedWorker.getName() + " " + selectedWorker.getPatronymic() + " " + selectedWorker.getID(), Toast.LENGTH_SHORT).show();
                editWorker(selectedWorker);
            }
        };
        lvWorkers.setOnItemClickListener(itemListener);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                Department dep = (Department) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Выбран: " + dep.getName(), Toast.LENGTH_SHORT).show();
                filterListView(dep);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
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
        if (configDB) {
            workers = new ArrayList();
            dbHelper = new DatabaseHelper(getApplicationContext());
            Integer k = 0;
            // открываем подключение
            db = dbHelper.getReadableDatabase();
            Cursor dbCursor = db.rawQuery("SELECT w._id, w.surname, w.name, w.patronymic, w.position, w.start_date, d._id as dep_id, d.name as dep_name FROM workers w INNER JOIN departments d ON d._id = dep_id", null);
            Department dep = new Department("fgfgf");
            Calendar date;
            String dateStr = "";
            Integer year = 0;
            Integer month = 0;
            Integer day = 0;
            if (dbCursor.moveToFirst()) {
                do {
                    k = 0;
                    for (Department d : deps) {
                        if (d.getID() == dbCursor.getInt(6)) {
                            dep = d;
                            k++;
                            break;
                        }
                    }
                    if (k == 0) {
                        dep = new Department(dbCursor.getInt(6), dbCursor.getString(7));
                        deps.add(dep);
                    }
                    dateStr = dbCursor.getString(5);
                    String[] dd = dateStr.split(":");
                    if (dd.length > 1) {
                        year = Integer.parseInt(dd[0]);
                        month = Integer.parseInt(dd[1]);
                        day = Integer.parseInt(dd[2]);
                        date = new GregorianCalendar(year, month, day);
                    } else {
                        date = new GregorianCalendar(2020, 1, 1);
                    }

                    workers.add(new Worker(dbCursor.getInt(0),
                            dbCursor.getString(1),
                            dbCursor.getString(2),
                            dbCursor.getString(3),
                            dbCursor.getString(4),
                            dep, date));
                }
                while (dbCursor.moveToNext());
            }
            dbCursor = db.rawQuery("SELECT _id,  name FROM departments ", null);
            if (dbCursor.moveToFirst()) {
                do {
                    k = 0;
                    for (Department d : deps) {
                        if (d.getID() == dbCursor.getInt(0)) {
                            k++;
                            break;
                        }
                    }
                    if (k == 0) {
                        deps.add(new Department(dbCursor.getInt(0), dbCursor.getString(1)));
                    }
                }
                while (dbCursor.moveToNext());
            }

            dbCursor.close();
            db.close();
        }

        renderListView();
        renderSpinner();
    }

    private void createWorker() {
        CardActivity card = new CardActivity();
        card.Method = "create";
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);
    }

    private void editWorker(Worker worker) {
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

    private void renderListView() {
        // создаем адаптер
        WorkerAdapter workerAdapter = new WorkerAdapter(this, R.layout.list_workers, workers);
        // устанавливаем адаптер
        lvWorkers.setAdapter(workerAdapter);
    }

    private void filterListView(Department dep) {
        List<Worker> filterWorkers = new ArrayList();
        for (Worker worker : workers) {
            if (worker.getDep() == dep) {
                filterWorkers.add(worker);
            }
        }
        filterWorkers = sortWorkers(filterWorkers);

        // создаем адаптер
        WorkerAdapter workerAdapter = new WorkerAdapter(this, R.layout.list_workers, filterWorkers);
        // устанавливаем адаптер
        lvWorkers.setAdapter(workerAdapter);
    }


    private void renderSpinner() {
        DepAdapter adapter = new DepAdapter(this, R.layout.list_deps, deps);
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

    private void setInitialData() {
        if (configDB) {
//            dbHelper = new DatabaseHelper(getApplicationContext());
//
//            // открываем подключение
//            db = dbHelper.getReadableDatabase();
//            //получаем данные из бд в виде курсора
//            Cursor query = db.rawQuery("select * from " + DatabaseHelper.WORKERS_TABLE, null);
//
//            if (query.moveToFirst()) {
//                do {
//
//                    Department dep = new Department(query.getInt(6), query.getString(7));
//                    deps.add(dep);
//                    workers.add(new Worker(query.getInt(0), query.getString(1), query.getString(2), query.getString(3), query.getString(4), dep, new GregorianCalendar(2020, 1, 20)));
//                }
//                while (query.moveToNext());
//                query.close();
//                db.close();
//            }
            // определяем, какие столбцы из курсора будут выводиться в ListView
//            String[] headers = new String[]{DatabaseHelper.W_COLUMN_SURNAME, DatabaseHelper.W_COLUMN_NAME, DatabaseHelper.W_COLUMN_PATRONYMIC};
//            // создаем адаптер, передаем в него курсор
//            workersAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, workersCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
//            header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));
//            userList.setAdapter(userAdapter);

//            openOrCreateDB();
//            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("4lr.db", MODE_PRIVATE, null);
//            Cursor query = db.rawQuery("SELECT w.id, w.surname, w.name, w.patronymic, w.position, w.startDate, d.id as dep_id, d.name as dep_name FROM workers w INNER JOIN departments d ON d.id = dep_id;", null);
//            if (query.moveToFirst()) {
//                do {
//
//                    Department dep = new Department(query.getInt(6), query.getString(7));
//                    deps.add(dep);
//                    workers.add(new Worker(query.getInt(0), query.getString(1), query.getString(2), query.getString(3), query.getString(4), dep, new GregorianCalendar(2020, 1, 20)));
//                }
//                while (query.moveToNext());
//                query.close();
//                db.close();
//            }
        } else {
            deps.add(new Department("Отдел продаж"));
            deps.add(new Department("Отдел маркетинга"));

            workers.add(new Worker("Иванов", "Иван", "Иванович", "Инженер", deps.get(0), new GregorianCalendar(2020, 1, 20)));
            workers.add(new Worker("Петров", "Иван", "Иванович", "Инженер", deps.get(1), new GregorianCalendar(2017, 0, 01)));
            workers.add(new Worker("Сидоров", "Иван", "Иванович", "Инженер", deps.get(1), new GregorianCalendar(2018, 11, 25)));
            workers.add(new Worker("Сидоров2", "Иван", "Иванович", "Инженер", deps.get(1), new GregorianCalendar(2016, 11, 25)));
        }
    }

    // DB
    private void openOrCreateDB() {
//        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("4lr.db", MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS workers (" +
//                "id INTEGER," +
//                "surname TEXT, " +
//                "name TEXT, " +
//                "patronymic TEXT, " +
//                "position TEXT, " +
//                "startDate TEXT," +
//                "dep_id INTEGER)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS departments (" +
//                "id INTEGER," +
//                "name TEXT)");
//        db.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        dbCursor.close();
    }
}
