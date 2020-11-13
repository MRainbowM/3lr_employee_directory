package com.example.a3_lr;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Worker {
    private String Surname, Name, Patronymic, Position;
    private Integer ID;
    private Calendar StartDate;
    private Department Dep;

    @Override
    public String toString() {
        String name = this.Surname + " " + this.Name + " " + this.Patronymic;
        return name;
    }

    public Worker(String Surname, String Name, String Patronymic, String Position, Department Dep, Calendar StartDate) {

        this.Surname = Surname;
        this.Name = Name;
        this.Patronymic = Patronymic;
        this.Position = Position;
        this.Dep = Dep;
        this.StartDate = StartDate;
    }

    public Worker(Integer ID, String Surname, String Name, String Patronymic, String Position, Department Dep, Calendar StartDate) {
        this.ID = ID;
        this.Surname = Surname;
        this.Name = Name;
        this.Patronymic = Patronymic;
        this.Position = Position;
        this.Dep = Dep;
        this.StartDate = StartDate;
    }

    public Integer getID() {
        return this.ID;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSurname() {
        return this.Surname;
    }

    public void setSurname(String Surname) {
        this.Surname = Surname;
    }

    public String getPatronymic() {
        return this.Patronymic;
    }

    public void setPatronymic(String Patronymic) {
        this.Patronymic = Patronymic;
    }

    public String getPosition() {
        return this.Position;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }

    public Department getDep() {
        return this.Dep;
    }

    public void setDep(Department Dep) {
        this.Dep = Dep;
    }

    public Calendar getStartDate() {
        return this.StartDate;
    }

    public void setStartDate(Calendar Date) {
        this.StartDate = Date;
    }

}
