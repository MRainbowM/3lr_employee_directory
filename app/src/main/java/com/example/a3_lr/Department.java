package com.example.a3_lr;

public class Department {

    private Integer ID;
    private String Name;

    public Department(String Name) {
        this.Name = Name;
    }

    public Department(Integer ID, String Name) {
        this.ID = ID;
        this.Name = Name;
    }


    @Override
    public String toString() {
        String name = this.Name;
        return name;
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
}
