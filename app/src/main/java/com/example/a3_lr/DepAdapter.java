package com.example.a3_lr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class DepAdapter  extends ArrayAdapter<Department> {
    private List<Department> departments;
    private int layout;
    private LayoutInflater inflater;


    public DepAdapter(@NonNull Context context, int resource, @NonNull List<Department> departments) {
        super(context, resource, departments);
        this.departments = departments;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView tvNameDep = (TextView) view.findViewById(R.id.tvNameDep);

        Department department = departments.get(position);

        tvNameDep.setText(department.getName());

        return view;
    }
}
