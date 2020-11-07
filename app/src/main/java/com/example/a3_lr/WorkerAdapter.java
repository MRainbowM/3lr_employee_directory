package com.example.a3_lr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class WorkerAdapter extends ArrayAdapter<Worker> {

    private List<Worker> workers;
    private int layout;
    private LayoutInflater inflater;


    public WorkerAdapter(@NonNull Context context, int resource, @NonNull List<Worker> workers) {
        super(context, resource, workers);
        this.workers = workers;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView tvSurname = (TextView) view.findViewById(R.id.tvSurname);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvPatronymic = (TextView) view.findViewById(R.id.tvPatronymic);

        Worker worker = workers.get(position);

        tvSurname.setText(worker.getSurname());
        tvName.setText(worker.getName());
        tvPatronymic.setText(worker.getPatronymic());

        return view;
    }
}
