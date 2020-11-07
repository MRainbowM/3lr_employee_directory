package com.example.a3_lr;

public class Department {

    private String Name;

    public Department(String Name){
        this.Name=Name;
    }

    @Override
    public String toString()
    {
        String name = this.Name;
        return name;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}
